package com.ims.process.builder;

import java.util.ArrayList;
import java.util.HashMap;

import com.ims.common.util.AddressConstants;
import com.ims.process.RequestProcessRule;
import com.ims.process.utils.RuleConstants;


public class PreProcessBuilder {

	private PreProcessBuilder() {
	}

	private static HashMap<String, ArrayList<RequestProcessRule>> preprocessRuleListMap;

	public static ArrayList<RequestProcessRule> getPreProcessList(String apiName) {

		if (preprocessRuleListMap == null) {
			buildPreProcessList();
		}
		return preprocessRuleListMap.get(apiName);
	}

	public static void buildPreProcessList() {

		ArrayList<RequestProcessRule> ruleList = new ArrayList<RequestProcessRule>();

		preprocessRuleListMap = new HashMap<String, ArrayList<RequestProcessRule>>();

		RequestProcessRule mobileNoEncryptRule = new RequestProcessRule();
		mobileNoEncryptRule.setRuleName(RuleConstants.ENCRYPT_DATA);
		mobileNoEncryptRule.setElementName("mobileNo");
		ruleList.add(mobileNoEncryptRule);

		RequestProcessRule digitalIdEncryptRule = new RequestProcessRule();
		digitalIdEncryptRule.setRuleName(RuleConstants.LOWER_CASE_ENCRYPT);
		digitalIdEncryptRule.setElementName("digitalId");
		ruleList.add(digitalIdEncryptRule);
		preprocessRuleListMap.put(AddressConstants.VIEW_DIGITAL_ID, ruleList);

	}
}
