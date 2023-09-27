package com.ims.process;

import java.util.ArrayList;

import com.ims.process.utils.ProcessUtils;
import com.ims.process.utils.RuleConstants;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class PostProcessor {

	public static void doProcess(JsonObject object, ArrayList<RequestProcessRule> ruleList, String methodAddress,
			Handler<AsyncResult<Object>> handler) {

		if (object.getValue(methodAddress) instanceof JsonObject) {
			JsonObject inObject = (JsonObject) object.getValue(methodAddress);
			handler.handle(Future.succeededFuture(processData((JsonObject) inObject, ruleList)));
		} else if (object.getValue(methodAddress) instanceof JsonArray) {

			JsonArray inObjectArray = (JsonArray) object.getValue(methodAddress);
 
			JsonArray outObjectArray = new JsonArray();
			inObjectArray.forEach(item -> {
				outObjectArray.add(processData((JsonObject) item, ruleList));
			});
			handler.handle(Future.succeededFuture(outObjectArray));
		}else {
			handler.handle(Future.succeededFuture(null));
		}
	}

	public static JsonObject processData(JsonObject object, ArrayList<RequestProcessRule> ruleList) {
		if (ruleList != null && ruleList.size() > 0 && object != null) {
			for (RequestProcessRule rule : ruleList) {
				if (rule.getRuleName().equals(RuleConstants.ENCRYPT_DATA)) {
					object.put(rule.getElementName(),
							ProcessUtils.encryptInput(object.getString(rule.getElementName())));
				} else if (rule.getRuleName().equals(RuleConstants.DECRYPT_DATA)) {
					object.put(rule.getElementName(),
							ProcessUtils.decryptInput(object.getString(rule.getElementName())));
				}else if (rule.getRuleName().equals(RuleConstants.MASK_DATA)) {
					object.put(rule.getElementName(),
							ProcessUtils.maskData(object.getString(rule.getElementName()),object.getInteger("maskTypeCd")));
				}
			}
		}
		return object;
	}
}
