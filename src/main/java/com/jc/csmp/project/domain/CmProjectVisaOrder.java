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
 * 建设管理-工程签证单管理
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public class CmProjectVisaOrder extends BaseBean {

	private static final long serialVersionUID = 1L;
	public CmProjectVisaOrder() {
	}
	/**流程ID*/
	private String piId;
	/**审核状态*/
	private String auditStatus;
	/**单号*/
	private String code;
	/**项目id*/
	private String projectId;
	/**合同id*/
	private String contractId;
	/**签证原因*/
	private String visaReason;
	/**发生时间*/
	private Date visaDate;
	private Date visaDateBegin;
	private Date visaDateEnd;
	/**发生部位或范围*/
	private String visaScope;
	/**变更签证做法及原因*/
	private String visaChange;
	/**工程量计算费用说明*/
	private String projectAmount;
	/**签证费用*/
	private BigDecimal visaAmount;
	/**核准费用*/
	private BigDecimal checkedAmount;
	private String curUserId;

	/**机构查询条件*/
	private String deptCodeCondition;
	private String projectNumber;
	private String projectName;
	private String contractCode;
	private String contractName;
	/**变更类型*/
	private String modifyType;
	/**
	 * 附件信息
	 */
	private String attachFile;
	private String deleteAttachFile;
	private WorkflowBean workflowBean = new WorkflowBean();

	public void setPiId(String piId) {
		this.piId = piId;
	}
	public String getPiId() {
		return piId;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditStatus() {
		return auditStatus;
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
	public void setVisaReason(String visaReason) {
		this.visaReason = visaReason;
	}
	public String getVisaReason() {
		return visaReason;
	}
	public void setVisaDateBegin(Date visaDateBegin) {
		this.visaDateBegin = visaDateBegin;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getVisaDateBegin() {
		return visaDateBegin;
	}
	public void setVisaDateEnd(Date visaDateEnd) {
		if(visaDateEnd == null){
			return;
		}
		this.visaDateEnd = DateUtils.fillTime(visaDateEnd);
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getVisaDateEnd() {
		return visaDateEnd;
	}
	public void setVisaDate(Date visaDate) {
		this.visaDate = visaDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getVisaDate() {
		return visaDate;
	}
	public void setVisaScope(String visaScope) {
		this.visaScope = visaScope;
	}
	public String getVisaScope() {
		return visaScope;
	}
	public void setVisaChange(String visaChange) {
		this.visaChange = visaChange;
	}
	public String getVisaChange() {
		return visaChange;
	}
	public void setProjectAmount(String projectAmount) {
		this.projectAmount = projectAmount;
	}
	public String getProjectAmount() {
		return projectAmount;
	}
	public void setVisaAmount(BigDecimal visaAmount) {
		this.visaAmount = visaAmount;
	}
	public BigDecimal getVisaAmount() {
		return visaAmount;
	}
	public void setCheckedAmount(BigDecimal checkedAmount) {
		this.checkedAmount = checkedAmount;
	}
	public BigDecimal getCheckedAmount() {
		return checkedAmount;
	}
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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
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

	public String getAuditStatusValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.workFlowAuditStatus, this.getAuditStatus());
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

	public String getCreateUserDeptValue() {
		return DeptCacheUtil.getNameById(this.getCreateUserDept());
	}
}