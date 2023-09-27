package com.ims.process.builder;

import java.util.ArrayList;
import java.util.HashMap;

import com.ims.common.util.AddressConstants;
import com.ims.process.RequestProcessRule;
import com.ims.process.utils.RuleConstants;


public class PostProcessBuilder {

	private PostProcessBuilder() {
	}

	private static HashMap<String, ArrayList<RequestProcessRule>> postProcessRuleListMap;

	public static ArrayList<RequestProcessRule> getPostProcessList(String apiName) {

		if (postProcessRuleListMap == null) {
			buildPostProcessList();
		}
		return null;
	}

	public static void buildPostProcessList() {

		ArrayList<RequestProcessRule> ruleList = new ArrayList<RequestProcessRule>();

		postProcessRuleListMap = new HashMap<String, ArrayList<RequestProcessRule>>();

		RequestProcessRule digitalIdEncryptRule = new RequestProcessRule();
		digitalIdEncryptRule.setRuleName(RuleConstants.DECRYPT_DATA);
		digitalIdEncryptRule.setElementName("digitalId");
		digitalIdEncryptRule.setExecutionOrder(2);
		ruleList.add(digitalIdEncryptRule);

		ArrayList<RequestProcessRule> viewDigitalIdRuleList = new ArrayList<RequestProcessRule>();

		viewDigitalIdRuleList.add(digitalIdEncryptRule);

		postProcessRuleListMap.put(AddressConstants.VIEW_DIGITAL_ID, viewDigitalIdRuleList);
		
		

	}
}
