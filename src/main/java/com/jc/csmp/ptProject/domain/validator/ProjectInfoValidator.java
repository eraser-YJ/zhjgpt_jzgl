package com.jc.csmp.ptProject.domain.validator;

import com.jc.csmp.ptProject.domain.ProjectInfo;
import com.jc.foundation.util.MessageUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @Version 1.0
 */
public class ProjectInfoValidator implements Validator{
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
		return ProjectInfo.class.equals(arg0);
	}

	/**
	 * @version 1.0
	 */
	@Override
	public void validate(Object arg0, Errors arg1) {
		ProjectInfo v = (ProjectInfo) arg0;
		if (v.getProjectnumber() != null && v.getProjectnumber().length() > 255) {
			arg1.reject("projectnumber", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if (v.getProjectname() != null && v.getProjectname().length() > 255) {
			arg1.reject("projectname", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if (v.getProjectaddress() != null && v.getProjectaddress().length() > 255) {
			arg1.reject("projectaddress", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if (v.getBuilddeptid() != null && v.getBuilddeptid().length() > 255) {
			arg1.reject("builddeptid", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if (v.getBuilddept() != null && v.getBuilddept().length() > 255) {
			arg1.reject("builddept", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if (v.getProjecttype() != null && v.getProjecttype().length() > 255) {
			arg1.reject("projecttype", MessageUtils.getMessage(JC_SYS_011, new Object[]{"255"}));
		}
		if (v.getDlhDataId() != null && v.getDlhDataId().length() > 100) {
			arg1.reject("dlhDataId", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
		}
		if (v.getDlhDataSrc() != null && v.getDlhDataSrc().length() > 64) {
			arg1.reject("dlhDataSrc", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if (v.getDlhDataUser() != null && v.getDlhDataUser().length() > 100) {
			arg1.reject("dlhDataUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
		}
		if (v.getConstructionOrganization() != null && v.getConstructionOrganization().length() > 64) {
			arg1.reject("constructionOrganization", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
		if (v.getProjectmanager() != null && v.getProjectmanager().length() > 64) {
			arg1.reject("projectmanager", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
		}
	}
}
