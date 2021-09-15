package com.jc.csmp.warn.rule.domain;

import com.jc.foundation.domain.BaseBean;
import java.util.Date;
import java.math.BigDecimal;
import com.jc.foundation.util.DateUtils;

/**
 * @author lc 
 * @version 2020-03-10
 */
public class WarnRule extends BaseBean {

	private static final long serialVersionUID = 1L;
	public WarnRule() {
	}
		private String ruleCode;
		private String ruleName;
		private String ruleTxt;

		public void setRuleCode(String ruleCode) {
			this.ruleCode = ruleCode;
		}
		public String getRuleCode() {
			return ruleCode;
		}
		public void setRuleName(String ruleName) {
			this.ruleName = ruleName;
		}
		public String getRuleName() {
			return ruleName;
		}
		public void setRuleTxt(String ruleTxt) {
			this.ruleTxt = ruleTxt;
		}
		public String getRuleTxt() {
			return ruleTxt;
		}
}