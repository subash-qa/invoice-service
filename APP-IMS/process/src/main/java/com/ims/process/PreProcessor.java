package com.ims.process;

import java.util.ArrayList;

import com.ims.process.utils.ProcessUtils;
import com.ims.process.utils.RuleConstants;

import io.vertx.core.json.JsonObject;

public class PreProcessor {

	public static JsonObject doProcess(JsonObject object, ArrayList<RequestProcessRule> ruleList) {
		if (ruleList != null && ruleList.size() > 0 && object != null) {
			for (RequestProcessRule rule : ruleList) {
				if (rule.getRuleName().equals(RuleConstants.LOWER_CASE_ENCRYPT)) {
					object.put(rule.getElementName(),
							ProcessUtils.encryptInput(object.getString(rule.getElementName()).toLowerCase()));
				} else if (rule.getRuleName().equals(RuleConstants.ENCRYPT_DATA)) {
					object.put(rule.getElementName(),
							ProcessUtils.encryptInput(object.getString(rule.getElementName())));
				} else if (rule.getRuleName().equals(RuleConstants.DECRYPT_DATA)) {
					object.put(rule.getElementName(),
							ProcessUtils.decryptInput(object.getString(rule.getElementName())));
				} else if (rule.getRuleName().equals(RuleConstants.ENCRYPT_DATA_WITH_SALT)) {
					object = ProcessUtils.encryptInputWithSalt(object);
				}
			}
		}
		return object;
	}
}
