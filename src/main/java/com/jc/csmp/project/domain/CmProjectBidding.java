package com.jc.csmp.project.domain;

import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.foundation.domain.BaseBean;
import java.util.Date;
import java.math.BigDecimal;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalUtil;
import com.jc.system.security.util.DeptCacheUtil;

/**
 * 建设管理-招投标管理
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmProjectBidding extends BaseBean {

	private static final long serialVersionUID = 1L;
	public CmProjectBidding() {
	}
	/**项目ID*/
	private String projectId;
	/**招标方式(字典)*/
	private String biddingMethod;
	/**最高限价*/
	private BigDecimal maxPrice;
	/**采购需求*/
	private String purchasingDemand;
	/**质量要求*/
	private String qualityRequirement;
	/**项目规模及内容*/
	private String projectContent;
	/**合同期限*/
	private String contractPeriod;
	/**建设用地规划许可证号*/
	private String buildLandLicence;
	/**建设工程规划许可证号*/
	private String buildProjectLicence;
	/**立项批文*/
	private String projectApproval;

	/**项目编号*/
	private String projectNumber;
	/**项目名称*/
	private String projectName;

	/**项目建设单位*/
	private String projectFirstPartyDeptId;

	/**数据权限，code查询*/
	private String deptCodeCondition;
	private Integer no;

	/**附件信息*/
	private String attachFile1;
	private String deleteAttachFile1;
	private String attachFile2;
	private String deleteAttachFile2;
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setBiddingMethod(String biddingMethod) {
		this.biddingMethod = biddingMethod;
	}
	public String getBiddingMethod() {
		return biddingMethod;
	}
	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}
	public BigDecimal getMaxPrice() {
		return maxPrice;
	}
	public void setPurchasingDemand(String purchasingDemand) {
		this.purchasingDemand = purchasingDemand;
	}
	public String getPurchasingDemand() {
		return purchasingDemand;
	}
	public void setQualityRequirement(String qualityRequirement) {
		this.qualityRequirement = qualityRequirement;
	}
	public String getQualityRequirement() {
		return qualityRequirement;
	}
	public void setProjectContent(String projectContent) {
		this.projectContent = projectContent;
	}
	public String getProjectContent() {
		return projectContent;
	}
	public void setContractPeriod(String contractPeriod) {
		this.contractPeriod = contractPeriod;
	}
	public String getContractPeriod() {
		return contractPeriod;
	}
	public String getBiddingMethodValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.biddingMethod, this.getBiddingMethod());
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

	public String getDeptCodeCondition() {
		return deptCodeCondition;
	}

	public void setDeptCodeCondition(String deptCodeCondition) {
		this.deptCodeCondition = deptCodeCondition;
	}

	public String getProjectFirstPartyDeptId() {
		return projectFirstPartyDeptId;
	}

	public void setProjectFirstPartyDeptId(String projectFirstPartyDeptId) {
		this.projectFirstPartyDeptId = projectFirstPartyDeptId;
	}

	public String getProjectFirstPartyDeptIdValue() {
		return DeptCacheUtil.getNameById(this.getProjectFirstPartyDeptId());
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public String getBuildLandLicence() {
		return buildLandLicence;
	}

	public void setBuildLandLicence(String buildLandLicence) {
		this.buildLandLicence = buildLandLicence;
	}

	public String getBuildProjectLicence() {
		return buildProjectLicence;
	}

	public void setBuildProjectLicence(String buildProjectLicence) {
		this.buildProjectLicence = buildProjectLicence;
	}

	public String getProjectApproval() {
		return projectApproval;
	}

	public void setProjectApproval(String projectApproval) {
		this.projectApproval = projectApproval;
	}

	public String getAttachFile1() {
		return attachFile1;
	}

	public void setAttachFile1(String attachFile1) {
		this.attachFile1 = attachFile1;
	}

	public String getDeleteAttachFile1() {
		return deleteAttachFile1;
	}

	public void setDeleteAttachFile1(String deleteAttachFile1) {
		this.deleteAttachFile1 = deleteAttachFile1;
	}

	public String getAttachFile2() {
		return attachFile2;
	}

	public void setAttachFile2(String attachFile2) {
		this.attachFile2 = attachFile2;
	}

	public String getDeleteAttachFile2() {
		return deleteAttachFile2;
	}

	public void setDeleteAttachFile2(String deleteAttachFile2) {
		this.deleteAttachFile2 = deleteAttachFile2;
	}
}