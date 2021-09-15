package com.jc.csmp.safe.supervision.domain;

import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.csmp.dic.domain.CmCustomDic;
import com.jc.csmp.dic.util.CmCustomDicCacheUtil;
import com.jc.foundation.domain.BaseBean;
import java.util.Date;
import java.math.BigDecimal;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.domain.Department;
import com.jc.system.security.util.DeptCacheUtil;

/**
 * @author lc 
 * @version 2020-03-10
 */
public class SafetyUnit extends BaseBean {

	private static final long serialVersionUID = 1L;
	public SafetyUnit() {
	}
		private String projectId;
		private String unitName;
		private String projectLeader;
		private String phone;
		private String partakeType;

		public void setProjectId(String projectId) {
			this.projectId = projectId;
		}
		public String getProjectId() {
			return projectId;
		}
		public void setUnitName(String unitName) {
			this.unitName = unitName;
		}
		public String getUnitName() {
			return unitName;
		}
		public void setProjectLeader(String projectLeader) {
			this.projectLeader = projectLeader;
		}
		public String getProjectLeader() {
			return projectLeader;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getPhone() {
			return phone;
		}
		public void setPartakeType(String partakeType) {
			this.partakeType = partakeType;
		}
		public String getPartakeType() {
			return partakeType;
		}


	public String getUnitNameValue() {
		String value = "";
		if (StringUtil.isEmpty(this.getUnitName())) {
			return value;
		}
		try {
			Department dept = DeptCacheUtil.getDeptById(this.getUnitName());
			if (dept != null) {
				value = dept.getName();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return value;
	}

	public String getPartakeTypeValue() {

			if("projectBuild".equals(this.getPartakeType())){
				return "建设单位";
			}else if("projectSupervise".equals(this.getPartakeType())){
				return "监管单位";

			}else{
				return GlobalUtil.getDicValue(DicKeyEnum.companyType, this.getPartakeType());
				/*CmCustomDic dic = CmCustomDicCacheUtil.getById(this.getPartakeType());
				return dic == null ? "" : dic.getName();*/
		}
	}
}