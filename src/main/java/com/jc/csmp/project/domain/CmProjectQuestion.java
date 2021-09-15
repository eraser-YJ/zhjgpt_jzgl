package com.jc.csmp.project.domain;

import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.foundation.domain.BaseBean;
import java.util.Date;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.workflow.external.WorkflowBean;
import com.jc.workflow.util.JsonUtil;
import org.infinispan.commons.hash.Hash;

/**
 * 建设管理-工程问题管理
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public class CmProjectQuestion extends BaseBean {

	private static final long serialVersionUID = 1L;
	public CmProjectQuestion() {
	}
	/**流程id*/
	private String piId;
	/**审核状态*/
	private String auditStatus;
	/**编号*/
	private String code;
	/**项目id*/
	private String projectId;
	/**合同id*/
	private String contractId;
	/**问题类型*/
	private String questionType;
	/**标题*/
	private String title;
	/**整改单位*/
	private String rectificationCompany;
	/**责任人*/
	private String personLiable;
	/**整改要求*/
	private String rectificationAsk;
	/**整改结果*/
	private String rectificationResult;
	/**备注*/
	private String remark;
	/**问题处置*/
	private String problemHandling;
	/**问题描述*/
	private String questionMeta;
	/**监理核验结果*/
	private String checkResult;
	/**处置问题负责人*/
	private String problemDept;
	/**安全事故类型*/
	private String safeFailureType;

	/***查询条件***/
	/**项目编号*/
	private String projectNumber;
	/**项目名称*/
	private String projectName;
	/**合同名称*/
	private String contractName;
	/**合同编号*/
	private String contractCode;
	/**机构查询条件*/
	private String deptCodeCondition;
	/**
	 * 附件信息
	 */
	private String attachFile;
	private String deleteAttachFile;
	/**待办人查询*/
	private String curUserId;

	/**流程变量的值，为了静默提交*/
	private String partaUnitLeaderId;
	private String superviseLeaderId;

	private String region;
	private String projectType;
	private String buildType;

	private Integer no;
	
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
	public String getAuditStatusValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.workFlowAuditStatus, this.getAuditStatus());
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
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setRectificationCompany(String rectificationCompany) {
		this.rectificationCompany = rectificationCompany;
	}
	public String getRectificationCompany() {
		return rectificationCompany;
	}
	public void setPersonLiable(String personLiable) {
		this.personLiable = personLiable;
	}
	public String getPersonLiable() {
		return personLiable;
	}
	public void setRectificationAsk(String rectificationAsk) {
		this.rectificationAsk = rectificationAsk;
	}
	public String getRectificationAsk() {
		return rectificationAsk;
	}
	public void setRectificationResult(String rectificationResult) {
		this.rectificationResult = rectificationResult;
	}
	public String getRectificationResult() {
		return rectificationResult;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getDeptCodeCondition() {
		return deptCodeCondition;
	}

	public void setDeptCodeCondition(String deptCodeCondition) {
		this.deptCodeCondition = deptCodeCondition;
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

	public String getRectificationCompanyValue() {
		return DeptCacheUtil.getNameById(this.getRectificationCompany());
	}

	public String getCreateDateFormat() {
		return GlobalUtil.dateUtil2String(this.getCreateDate(), "yyyy-MM-dd");
	}

	public String getProblemHandling() {
		return problemHandling;
	}

	public void setProblemHandling(String problemHandling) {
		this.problemHandling = problemHandling;
	}

	public String getPartaUnitLeaderId() {
		return partaUnitLeaderId;
	}

	public void setPartaUnitLeaderId(String partaUnitLeaderId) {
		this.partaUnitLeaderId = partaUnitLeaderId;
	}

	public String getSuperviseLeaderId() {
		return superviseLeaderId;
	}

	public void setSuperviseLeaderId(String superviseLeaderId) {
		this.superviseLeaderId = superviseLeaderId;
	}

	@Override
	public String getExtStr1() {
		if (!StringUtil.isEmpty(extStr1)) {
			Map<String, String> workflowAuditUser = (Map<String, String>) JsonUtil.json2Java(this.extStr1, Map.class);
			if (workflowAuditUser != null) {
				this.setPartaUnitLeaderId(workflowAuditUser.get("partaUnitLeaderId"));
				this.setSuperviseLeaderId(workflowAuditUser.get("superviseLeaderId"));
			}
		}
		return this.extStr1;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public String getQuestionMeta() {
		return questionMeta;
	}

	public void setQuestionMeta(String questionMeta) {
		this.questionMeta = questionMeta;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getProblemDept() {
		return problemDept;
	}

	public void setProblemDept(String problemDept) {
		this.problemDept = problemDept;
	}

	public String getProblemDeptValue() {
		return DeptCacheUtil.getNameById(problemDept);
	}

	public String getSafeFailureType() {
		return safeFailureType;
	}

	public void setSafeFailureType(String safeFailureType) {
		this.safeFailureType = safeFailureType;
	}

	public String getSafeFailureTypeValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.safeFailureType, this.getSafeFailureType());
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getBuildType() {
		return buildType;
	}

	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}
}