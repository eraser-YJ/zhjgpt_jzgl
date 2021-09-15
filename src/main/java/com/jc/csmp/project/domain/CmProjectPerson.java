package com.jc.csmp.project.domain;

import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.foundation.domain.BaseBean;
import java.util.Date;
import java.math.BigDecimal;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.system.dic.IDicManager;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.UserUtils;

/**
 * 建设管理-项目人员分配
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmProjectPerson extends BaseBean {

	private static final long serialVersionUID = 1L;
	public CmProjectPerson() {
	}
	/**项目id*/
	private String projectId;
	/**单位id*/
	private String companyId;
	/**参与身份*/
	private String partakeType;
	/**负责人*/
	private String leader;
	/**负责人电话*/
	private String phone;
	/**是否显示*/
	private String canShow;
	private String deptCode;
	/**删除监管和建设单位的标记*/
	private String deleteDefault;
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setPartakeType(String partakeType) {
		this.partakeType = partakeType;
	}
	public String getPartakeType() {
		return partakeType;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhone() {
		return phone;
		}
	public String getPartakeTypeValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.companyType, this.getPartakeType());
	}
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public String getCompanyName() {
		String value = "";
		if (StringUtil.isEmpty(this.getCompanyId())) {
			return value;
		}
		try {
			Department dept = DeptCacheUtil.getDeptById(this.getCompanyId());
			if (dept != null) {
				value = dept.getName();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return value;
	}

	public String getCanShow() {
		return canShow;
	}

	public void setCanShow(String canShow) {
		this.canShow = canShow;
	}

	public String getDeleteDefault() {
		return deleteDefault;
	}

	public void setDeleteDefault(String deleteDefault) {
		this.deleteDefault = deleteDefault;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
}