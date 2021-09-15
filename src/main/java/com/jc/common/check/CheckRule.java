package com.jc.common.check;

public enum CheckRule {

	// 必输
	required("required", "true", new RuleRequired()),
	// 字典
	dic("dic", "", new RuleDic()),
	// 正则
	regular("regular", "", new RuleRegular());
	private String code;
	private String defaultValue;
	private ICheckRule rule;

	CheckRule(String inCode, String inDefaultValue, ICheckRule inRule) {
		code = inCode;
		rule = inRule;
		defaultValue = inDefaultValue;
	}

	public String getCode() {
		return code;
	}

	public ICheckRule getRule() {
		return rule;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public static CheckRule getRule(String ruleCode) {
		for (CheckRule crule : CheckRule.values()) {
			if (crule.getCode().equalsIgnoreCase(ruleCode)) {
				return crule;
			}
		}
		return null;
	}
}
