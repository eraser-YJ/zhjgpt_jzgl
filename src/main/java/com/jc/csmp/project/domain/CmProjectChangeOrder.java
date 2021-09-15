package com.jc.csmp.project.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.foundation.domain.BaseBean;
import java.util.Date;
import java.math.BigDecimal;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.workflow.external.WorkflowBean;

/**
 * 建设管理-工程变更单管理
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public class CmProjectChangeOrder extends BaseBean {

	private static final long serialVersionUID = 1L;

	public CmProjectChangeOrder() {
	}
	/**流程id*/
	private String piId;
	/**单号*/
	private String code;
	/**项目id*/
	private String projectId;
	/**合同id*/
	private String contractId;
	/**发起机构*/
	private String deptId;
	/**变更类型(字典)*/
	private String changeType;
	/**变更费用*/
	private BigDecimal changeAmount;
	/**变更日期*/
	private Date changeDate;
	private Date changeDateBegin;
	private Date changeDateEnd;
	/**变更原因*/
	private String changeReason;
	/**变更内容*/
	private String changeContent;
	/**经办人*/
	private String handleUser;
	/**变更类型*/
	private String modifyType;
	/**
	 * 待办人查询
	 */
	private String curUserId;
	private WorkflowBean workflowBean = new WorkflowBean();
	/**
	 * 审核状态
	 */
	private String auditStatus;
	/**
	 * 附件信息
	 */
	private String attachFile;
	private String deleteAttachFile;
	private String projectNumber;
	private String projectName;
	private String contractCode;
	private String contractName;
	/**登陆人部门编码权限*/
	private String deptCodeCondition;

	public WorkflowBean getWorkflowBean() {
		return workflowBean;
	}

	public void setWorkflowBean(WorkflowBean workflowBean) {
		this.workflowBean = workflowBean;
	}

	public String getCurUserId() {
		return curUserId;
	}

	public void setCurUserId(String curUserId) {
		this.curUserId = curUserId;
	}

	public void setPiId(String piId) {
		this.piId = piId;
	}

	public String getPiId() {
		return piId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptId() {
		return deptId;
	}

	public String getDeptName() {
		return DeptCacheUtil.getNameById(this.getDeptId());
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getChangeType() {
		return changeType;
	}

	public String getChangeTypeValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.projectChangeType, this.getChangeType());
	}

	public void setChangeAmount(BigDecimal changeAmount) {
		this.changeAmount = changeAmount;
	}

	public BigDecimal getChangeAmount() {
		return changeAmount;
	}

	public void setChangeDateBegin(Date changeDateBegin) {
		this.changeDateBegin = changeDateBegin;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getChangeDateBegin() {
		return changeDateBegin;
	}

	public void setChangeDateEnd(Date changeDateEnd) {
		if (changeDateEnd == null) {
			return;
		}
		this.changeDateEnd = DateUtils.fillTime(changeDateEnd);
	}

	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getChangeDateEnd() {
		return changeDateEnd;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}

	public String getChangeReason() {
		return changeReason;
	}

	public void setChangeContent(String changeContent) {
		this.changeContent = changeContent;
	}

	public String getChangeContent() {
		return changeContent;
	}

	public void setHandleUser(String handleUser) {
		this.handleUser = handleUser;
	}

	public String getHandleUser() {
		return handleUser;
	}

	public String getAttachFile() {
		return attachFile;
	}

	public void setAttachFile(String attachFile) {
		this.attachFile = attachFile;
	}

	public String getDeleteAttachFile() {
		return deleteAttachFile;
	}

	public void setDeleteAttachFile(String deleteAttachFile) {
		this.deleteAttachFile = deleteAttachFile;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditStatusValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.workFlowAuditStatus, this.getAuditStatus());
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDeptCodeCondition() {
		return deptCodeCondition;
	}

	public void setDeptCodeCondition(String deptCodeCondition) {
		this.deptCodeCondition = deptCodeCondition;
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getModifyType() {
		return modifyType;
	}

	public String getModifyTypeValue() {
		if (!StringUtil.isEmpty(this.getModifyType())) {
			if (this.getModifyType().equals("0")) {
				return "增加";
			} else if (this.getModifyType().equals("1")) {
				return "减少";
			}
		}
		return "";
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}
}