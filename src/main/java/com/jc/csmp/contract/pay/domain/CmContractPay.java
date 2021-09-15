package com.jc.csmp.contract.pay.domain;

import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.UserUtils;
import com.jc.workflow.external.WorkflowBean;

import java.math.BigDecimal;

/**
 * 建设管理-合同支付管理
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public class CmContractPay extends BaseBean {
	private static final long serialVersionUID = 1L;
	public CmContractPay() {
	}
	/**单据编号*/
	private String payNo;
	/**合同ID*/
	private String contractId;
	/**申请金额*/
	private BigDecimal applyMoney;
	/**批复金额*/
	private BigDecimal replyMoney;
	/**累计支付金额*/
	private BigDecimal payMoney;
	/**经办人(审批人)*/
	private String handleUser;
	/**流程ID*/
	private String piId;
	/**审核状态*/
	private String auditStatus;
	/**备注*/
	private String remark;

	/**待办人查询*/
	private String curUserId;
	private WorkflowBean workflowBean = new WorkflowBean();

	/**附件信息*/
	private String attachFile;
	private String deleteAttachFile;

	private String contractCode;
	private String contractName;
	private BigDecimal contractMoney;
	private BigDecimal totalPayment;
	private String projectId;
	private String projectNumber;
	private String projectName;
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
	public String getPiId(){
		return piId;
	}
	public void setPiId(String piId){
		this.piId = piId;
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

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public BigDecimal getApplyMoney() {
		return applyMoney;
	}

	public void setApplyMoney(BigDecimal applyMoney) {
		this.applyMoney = applyMoney;
	}

	public BigDecimal getReplyMoney() {
		return replyMoney;
	}

	public void setReplyMoney(BigDecimal replyMoney) {
		this.replyMoney = replyMoney;
	}

	public BigDecimal getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}

	public String getHandleUser() {
		return handleUser;
	}

	public void setHandleUser(String handleUser) {
		this.handleUser = handleUser;
	}

	public String getHandleUserValue() {
		String value = "";
		if (StringUtil.isEmpty(this.getHandleUser())) {
			return value;
		}
		User user = UserUtils.getUser(this.getHandleUser());
		if (user != null) {
			value = user.getDisplayName();
		}
		return value;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getContractMoney() {
		return contractMoney;
	}

	public void setContractMoney(BigDecimal contractMoney) {
		this.contractMoney = contractMoney;
	}

	public BigDecimal getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(BigDecimal totalPayment) {
		this.totalPayment = totalPayment;
	}

	public String getDeptCodeCondition() {
		return deptCodeCondition;
	}

	public void setDeptCodeCondition(String deptCodeCondition) {
		this.deptCodeCondition = deptCodeCondition;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}