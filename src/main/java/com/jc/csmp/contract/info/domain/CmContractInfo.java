package com.jc.csmp.contract.info.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalUtil;
import com.jc.system.security.domain.Department;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.workflow.external.WorkflowBean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 建设管理-合同管理
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public class CmContractInfo extends BaseBean {
	private static final long serialVersionUID = 1L;
	public CmContractInfo() {
	}
	/**项目ID*/
	private String projectId;
	/**合同名称*/
	private String contractName;
	/**合同类型(字典)*/
	private String contractType;
	/**合同编号*/
	private String contractCode;
	/**合同金额*/
	private BigDecimal contractMoney;
	/**施工单位（乙方）*/
	private String partybUnit;
	/**建设单位（甲方）*/
	private String partyaUnit;
	/**工期*/
	private String constructionPeriod;
	/**文明用地(字典)*/
	private String civilizedLand;
	/**创优目标(字典)*/
	private String goalOfExcellence;
	/**付款方式(字典)*/
	private String paymentMethod;
	/**结算是否要审计*/
	private String needAudit;
	/**合同内容*/
	private String contractContent;
	/**合同开始时间*/
	private Date startDate;
	/**合同结束时间*/
	private Date endDate;
	/**合同签订时间*/
	private Date signDate;
	/**经办人*/
	private String handleUser;
	/**支付总额*/
	private BigDecimal totalPayment;
	/**支付比例*/
	private BigDecimal paymentRatio;
	/**备注*/
	private String remark;
	/**流程ID*/
	private String piId;
	/**审核状态*/
	private String auditStatus;

	/**项目名称*/
	private String projectName;
	/**项目编号*/
	private String projectNumber;
	private Date signDateBegin;
	private Date signDateEnd;

	/**待办人查询*/
	private String curUserId;
	private WorkflowBean workflowBean = new WorkflowBean();

	/**附件信息*/
	private String attachFile;
	private String deleteAttachFile;
	/**部门数据权限条件*/
	private String deptCodeCondition;
	/**合同乙方负责人*/
	private String partyaUnitLeaderId;
	private String partyaUnitValue;
	private String partybUnitLeaderId;
	private String partybUnitValue;

	/**监理单位，查询列表后，循环查询的,如果当作参数传递过来的话，说明需要查询监理单位*/
	private String supervisorUnit;

	/**序号*/
	private Integer no;

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
	public String getContractType() {
		return contractType;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractMoney(BigDecimal contractMoney) {
		this.contractMoney = contractMoney;
	}
	public BigDecimal getContractMoney() {
		return contractMoney;
	}
	public void setPartybUnit(String partybUnit) {
		this.partybUnit = partybUnit;
		Department entity = null;
		try {
			entity = DeptCacheUtil.getDeptById(partybUnit);
			if (entity != null) {
				this.setPartybUnitValue(entity.getName());
				this.setPartybUnitLeaderId(entity.getLeaderId());
			}
		} catch (CustomException e) {
			e.printStackTrace();
		}
	}
	public String getPartybUnit() {
		return partybUnit;
	}
	public void setPartyaUnit(String partyaUnit) {
		this.partyaUnit = partyaUnit;
		Department entity = null;
		try {
			entity = DeptCacheUtil.getDeptById(partyaUnit);
			if (entity != null) {
				this.setPartyaUnitValue(entity.getName());
				this.setPartyaUnitLeaderId(entity.getLeaderId());
			}
		} catch (CustomException e) {
			e.printStackTrace();
		}
	}
	public String getPartyaUnit() {
		return partyaUnit;
	}
	public void setConstructionPeriod(String constructionPeriod) {
		this.constructionPeriod = constructionPeriod;
	}
	public String getConstructionPeriod() {
		return constructionPeriod;
	}
	public void setCivilizedLand(String civilizedLand) {
		this.civilizedLand = civilizedLand;
	}
	public String getCivilizedLand() {
		return civilizedLand;
	}
	public void setGoalOfExcellence(String goalOfExcellence) {
		this.goalOfExcellence = goalOfExcellence;
	}
	public String getGoalOfExcellence() {
		return goalOfExcellence;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setNeedAudit(String needAudit) {
		this.needAudit = needAudit;
	}
	public String getNeedAudit() {
		return needAudit;
	}
	public void setContractContent(String contractContent) {
		this.contractContent = contractContent;
	}
	public String getContractContent() {
		return contractContent;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getStartDate() {
		return startDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getEndDate() {
		return endDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getSignDate() {
		return signDate;
	}
	public void setHandleUser(String handleUser) {
		this.handleUser = handleUser;
	}
	public String getHandleUser() {
		return handleUser;
	}
	public void setTotalPayment(BigDecimal totalPayment) {
		this.totalPayment = totalPayment;
	}
	public BigDecimal getTotalPayment() {
		return totalPayment;
	}
	public void setPaymentRatio(BigDecimal paymentRatio) {
		this.paymentRatio = paymentRatio;
	}
	public BigDecimal getPaymentRatio() {
		return this.paymentRatio;
	}
	public BigDecimal getPaymentRatioValue() {
		if (this.getTotalPayment() == null || this.getTotalPayment().longValue() == 0) {
			return new BigDecimal(0);
		}
		BigDecimal num = this.getTotalPayment();
		if (num == null || num.longValue() == 0) {
			num = new BigDecimal(0);
		}
		return GlobalUtil.multiply(GlobalUtil.divide(num, this.getContractMoney(), 4), new BigDecimal(100));
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
	public String getContractTypeValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.contractType, this.getContractType());
	}
	public String getCivilizedLandValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.civilizedLand, this.getCivilizedLand());
	}
	public String getGoalOfExcellenceValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.goalOfExcellence, this.getGoalOfExcellence());
	}
	public String getPaymentMethodValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.paymentMethod, this.getPaymentMethod());
	}
	public String getNeedAuditValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.yesOrNo, this.getNeedAudit());
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	public Date getSignDateBegin() {
		return signDateBegin;
	}
	public void setSignDateBegin(Date signDateBegin) {
		this.signDateBegin = signDateBegin;
	}
	public Date getSignDateEnd() {
		return signDateEnd;
	}
	public void setSignDateEnd(Date signDateEnd) {
		this.signDateEnd = DateUtils.fillTime(signDateEnd);
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
	public String getPiId(){
		return piId;
	}
	public void setPiId(String piId){
		this.piId = piId;
	}
	public String getPartybUnitValue() {
		return DeptCacheUtil.getNameById(this.getPartybUnit());
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

	public String getDeptCodeCondition() {
		return deptCodeCondition;
	}

	public void setDeptCodeCondition(String deptCodeCondition) {
		this.deptCodeCondition = deptCodeCondition;
	}

	public String getSupervisorUnit() {
		return supervisorUnit;
	}

	public void setSupervisorUnit(String supervisorUnit) {
		this.supervisorUnit = supervisorUnit;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public String getPartyaUnitLeaderId() {
		return partyaUnitLeaderId;
	}

	public void setPartyaUnitLeaderId(String partyaUnitLeaderId) {
		this.partyaUnitLeaderId = partyaUnitLeaderId;
	}

	public String getPartyaUnitValue() {
		return partyaUnitValue;
	}

	public void setPartyaUnitValue(String partyaUnitValue) {
		this.partyaUnitValue = partyaUnitValue;
	}

	public String getPartybUnitLeaderId() {
		return partybUnitLeaderId;
	}

	public void setPartybUnitLeaderId(String partybUnitLeaderId) {
		this.partybUnitLeaderId = partybUnitLeaderId;
	}

	public void setPartybUnitValue(String partybUnitValue) {
		this.partybUnitValue = partybUnitValue;
	}
}