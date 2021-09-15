package com.jc.csmp.contract.info.domain.validator;

import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.foundation.util.MessageUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 建设管理-合同管理检验类
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public class CmContractInfoValidator implements Validator{
	private static final String JC_SYS_010 = "JC_SYS_010";
	private static final String JC_SYS_011 = "JC_SYS_011";
	private static final int FIFTY = 50;
	private static final int TWO_HUNDRED = 200;

	/**
	 * @description 检验类能够校验的类
	 * @param arg0  校验的类型
	 * @return 是否支持校验
	 * @Author 常鹏
	 * @version 1.0
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return CmContractInfo.class.equals(arg0);
	}

	/**
	 * @description 检验具体实现方法
	 * @param arg0  当前的实体类
	 * @param arg1  错误的信息
	 * @Author 常鹏
	 * @version 1.0
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		CmContractInfo v = (CmContractInfo) arg0;
		if (v.getId() != null && v.getId().length() > FIFTY) {
			arg1.reject("id", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + FIFTY}));
		}
		if (v.getProjectId() != null && v.getProjectId().length() > FIFTY) {
			arg1.reject("projectId", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + FIFTY}));
		}
		if (v.getContractName() != null && v.getContractName().length() > 255) {
			arg1.reject("contractName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if (v.getContractType() != null && v.getContractType().length() > FIFTY) {
			arg1.reject("contractType", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + FIFTY}));
		}
		if (v.getContractCode() != null && v.getContractCode().length() > 255) {
			arg1.reject("contractCode", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if (v.getPartybUnit() != null && v.getPartybUnit().length() > FIFTY) {
			arg1.reject("partybUnit", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + FIFTY}));
		}
		if (v.getPartyaUnit() != null && v.getPartyaUnit().length() > FIFTY) {
			arg1.reject("partyaUnit", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + FIFTY}));
		}
		if (v.getConstructionPeriod() != null && v.getConstructionPeriod().length() > 255) {
			arg1.reject("constructionPeriod", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if (v.getCivilizedLand() != null && v.getCivilizedLand().length() > FIFTY) {
			arg1.reject("civilizedLand", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + FIFTY}));
		}
		if (v.getGoalOfExcellence() != null && v.getGoalOfExcellence().length() > FIFTY) {
			arg1.reject("goalOfExcellence", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + FIFTY}));
		}
		if (v.getPaymentMethod() != null && v.getPaymentMethod().length() > FIFTY) {
			arg1.reject("paymentMethod", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + FIFTY}));
		}
		if (v.getNeedAudit() != null && v.getNeedAudit().length() > FIFTY) {
			arg1.reject("needAudit", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + FIFTY}));
		}
		if (v.getContractContent() != null && v.getContractContent().length() > 2000) {
			arg1.reject("contractContent", MessageUtils.getMessage(JC_SYS_011, new Object[]{"2000"}));
		}
		if (v.getHandleUser() != null && v.getHandleUser().length() > FIFTY) {
			arg1.reject("handleUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + FIFTY}));
		}
		if (v.getRemark() != null && v.getRemark().length() > 16383) {
			arg1.reject("remark", MessageUtils.getMessage(JC_SYS_011, new Object[]{"16383"}));
		}
		if(v.getPiId()!=null&&v.getPiId().length()>50){
			arg1.reject("piId", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
		}
		if (v.getCreateUser() != null && v.getCreateUser().length() > FIFTY) {
			arg1.reject("createUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + FIFTY}));
		}
		if (v.getCreateUserDept() != null && v.getCreateUserDept().length() > FIFTY) {
			arg1.reject("createUserDept", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + FIFTY}));
		}
		if (v.getCreateUserOrg() != null && v.getCreateUserOrg().length() > FIFTY) {
			arg1.reject("createUserOrg", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + FIFTY}));
		}
		if (v.getModifyUser() != null && v.getModifyUser().length() > FIFTY) {
			arg1.reject("modifyUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + FIFTY}));
		}
		if (v.getExtStr1() != null && v.getExtStr1().length() > TWO_HUNDRED) {
			arg1.reject("extStr1", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + TWO_HUNDRED}));
		}
		if (v.getExtStr2() != null && v.getExtStr2().length() > TWO_HUNDRED) {
			arg1.reject("extStr2", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + TWO_HUNDRED}));
		}
		if (v.getExtStr3() != null && v.getExtStr3().length() > TWO_HUNDRED) {
			arg1.reject("extStr3", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + TWO_HUNDRED}));
		}
		if (v.getExtStr4() != null && v.getExtStr4().length() > TWO_HUNDRED) {
			arg1.reject("extStr4", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + TWO_HUNDRED}));
		}
		if (v.getExtStr5() != null && v.getExtStr5().length() > TWO_HUNDRED) {
			arg1.reject("extStr5", MessageUtils.getMessage(JC_SYS_011, new Object[]{"" + TWO_HUNDRED}));
		}
	}
}
