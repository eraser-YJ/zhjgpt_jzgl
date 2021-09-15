package com.jc.csmp.ptProject.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.csmp.dic.domain.CmCustomDic;
import com.jc.csmp.dic.util.CmCustomDicCacheUtil;
import com.jc.foundation.domain.BaseBean;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.math.BigDecimal;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;

/**
 * @author liubq
 * @version 2020-07-10
 */
public class ProjectInfo extends BaseBean {

	private static final long serialVersionUID = 1L;
	public ProjectInfo() {
	}
	private String projectnumber;
	private String projectname;
	private String projectaddress;
	private String builddeptid;
	private String builddept;
	private String projecttype;
	private BigDecimal buildarea;
	private BigDecimal projectmoney;
	private Date realstartdate;
	private Date realstartdateBegin;
	private Date realstartdateEnd;
	private Date realfinishdate;
	private Date realfinishdateBegin;
	private Date realfinishdateEnd;
	private String dlhDataId;
	private Date dlhDataModifyDate;
	private Date dlhDataModifyDateBegin;
	private Date dlhDataModifyDateEnd;
	private String dlhDataSrc;
	private String dlhDataUser;
	private String constructionOrganization;
	private String projectmanager;
	private String projectArea;
	private String approvalNumber;
	private Date approvalDate;
	private String projectCate;//项目分类

	private String isFinish;//是否竣工
	private String longitude;//经度
	private String latitude;//纬度
    private String highBuild;//高层建筑
	private String companyId;//企业ID
	private String companyName;//企业名称
	private String companyType;//企业类型
	private String htlb;//合同类型-非空条件
	private String contractType;//合同类型
	private String stage;//阶段
	private String tableName;//按阶段查询表名称
	private String columnName;//按阶段查询列名称
	private Integer stageCount1;//项目立项-阶段数量
	private Integer stageCount2;//勘察设计-阶段数量
	private Integer stageCount3;//招投标-阶段数量
	private Integer stageCount4;//合同备案-阶段数量
	private Integer stageCount5;//竣工备案-阶段数量
	private Integer stageCount6;//安全监管-阶段数量
	private Integer stageCount7;//质量监督-阶段数量
	private Integer stageCount8;//施工许可证-阶段数量
	private String buildingTypes;//建筑类型
	private String structureType;//结构类型


	public String getApprovalNumber() {
		return approvalNumber;
	}

	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getProjectCate() {
		return projectCate;
	}

	public void setProjectCate(String projectCate) {
		this.projectCate = projectCate;
	}

	public BigDecimal getProductionTotal() {
		return productionTotal;
	}

	public void setProductionTotal(BigDecimal productionTotal) {
		this.productionTotal = productionTotal;
	}

	public String getPcpXq() {
		return pcpXq;
	}

	public void setPcpXq(String pcpXq) {
		this.pcpXq = pcpXq;
	}

	public String getPcpZbfs() {
		return pcpZbfs;
	}

	public void setPcpZbfs(String pcpZbfs) {
		this.pcpZbfs = pcpZbfs;
	}

	public BigDecimal getPcpZbje() {
		return pcpZbje;
	}

	public void setPcpZbje(BigDecimal pcpZbje) {
		this.pcpZbje = pcpZbje;
	}

	public String getBuildType() {
		return buildType;
	}

	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}

	private BigDecimal productionTotal;
	private String pcpXq;
	private String pcpZbfs;
	private BigDecimal pcpZbje;
	private String buildType;

	public void setProjectnumber(String projectnumber) {
		this.projectnumber = projectnumber;
	}
	public String getProjectnumber() {
		return projectnumber;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectaddress(String projectaddress) {
		this.projectaddress = projectaddress;
	}
	public String getProjectaddress() {
		return projectaddress;
	}
	public void setBuilddeptid(String builddeptid) {
		this.builddeptid = builddeptid;
	}
	public String getBuilddeptid() {
		return builddeptid;
	}
	public void setBuilddept(String builddept) {
		this.builddept = builddept;
	}
	public String getBuilddept() {
		return builddept;
	}
	public void setProjecttype(String projecttype) {
		this.projecttype = projecttype;
	}
	public String getProjecttype() {
		return projecttype;
	}
	public void setBuildarea(BigDecimal buildarea) {
		this.buildarea = buildarea;
	}
	public BigDecimal getBuildarea() {
		return buildarea;
	}
	public void setProjectmoney(BigDecimal projectmoney) {
		this.projectmoney = projectmoney;
	}
	public BigDecimal getProjectmoney() {
		return projectmoney;
	}
	public void setRealstartdateBegin(Date realstartdateBegin) {
		this.realstartdateBegin = realstartdateBegin;
	}
	public Date getRealstartdateBegin() {
		return realstartdateBegin;
	}
	public void setRealstartdateEnd(Date realstartdateEnd) {
		if(realstartdateEnd == null){
			return;
		}
		this.realstartdateEnd = DateUtils.fillTime(realstartdateEnd);
	}
	public Date getRealstartdateEnd() {
		return realstartdateEnd;
	}
	public void setRealstartdate(Date realstartdate) {
		this.realstartdate = realstartdate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getRealstartdate() {
		return realstartdate;
	}
	public void setRealfinishdateBegin(Date realfinishdateBegin) {
		this.realfinishdateBegin = realfinishdateBegin;
	}
	public Date getRealfinishdateBegin() {
		return realfinishdateBegin;
	}
	public void setRealfinishdateEnd(Date realfinishdateEnd) {
		if(realfinishdateEnd == null){
			return;
		}
		this.realfinishdateEnd = DateUtils.fillTime(realfinishdateEnd);
	}
	public Date getRealfinishdateEnd() {
		return realfinishdateEnd;
	}
	public void setRealfinishdate(Date realfinishdate) {
		this.realfinishdate = realfinishdate;
	}
	public Date getRealfinishdate() {
		return realfinishdate;
	}
	public void setDlhDataId(String dlhDataId) {
		this.dlhDataId = dlhDataId;
	}
	public String getDlhDataId() {
		return dlhDataId;
	}
	public void setDlhDataModifyDateBegin(Date dlhDataModifyDateBegin) {
		this.dlhDataModifyDateBegin = dlhDataModifyDateBegin;
	}
	public Date getDlhDataModifyDateBegin() {
		return dlhDataModifyDateBegin;
	}
	public void setDlhDataModifyDateEnd(Date dlhDataModifyDateEnd) {
		if(dlhDataModifyDateEnd == null){
			return;
		}
		this.dlhDataModifyDateEnd = DateUtils.fillTime(dlhDataModifyDateEnd);
	}
	public Date getDlhDataModifyDateEnd() {
		return dlhDataModifyDateEnd;
	}
	public void setDlhDataModifyDate(Date dlhDataModifyDate) {
		this.dlhDataModifyDate = dlhDataModifyDate;
	}
	public Date getDlhDataModifyDate() {
		return dlhDataModifyDate;
	}
	public void setDlhDataSrc(String dlhDataSrc) {
		this.dlhDataSrc = dlhDataSrc;
	}
	public String getDlhDataSrc() {
		return dlhDataSrc;
	}
	public void setDlhDataUser(String dlhDataUser) {
		this.dlhDataUser = dlhDataUser;
	}
	public String getDlhDataUser() {
		return dlhDataUser;
	}
	public void setConstructionOrganization(String constructionOrganization) {
		this.constructionOrganization = constructionOrganization;
	}
	public String getConstructionOrganization() {
		return constructionOrganization;
	}
	public void setProjectmanager(String projectmanager) {
		this.projectmanager = projectmanager;
	}
	public String getProjectmanager() {
		return projectmanager;
	}

	public String getProjectArea() {
		return projectArea;
	}

	public void setProjectArea(String projectArea) {
		this.projectArea = projectArea;
	}

	public String getIsFinish() {
		return isFinish;
	}

	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getHighBuild() {
        return highBuild;
    }

    public void setHighBuild(String highBuild) {
        this.highBuild = highBuild;
    }

    public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getHtlb() {
		return htlb;
	}

	public void setHtlb(String htlb) {
		this.htlb = htlb;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Integer getStageCount1() {
		return stageCount1;
	}

	public void setStageCount1(Integer stageCount1) {
		this.stageCount1 = stageCount1;
	}

	public Integer getStageCount2() {
		return stageCount2;
	}

	public void setStageCount2(Integer stageCount2) {
		this.stageCount2 = stageCount2;
	}

	public Integer getStageCount3() {
		return stageCount3;
	}

	public void setStageCount3(Integer stageCount3) {
		this.stageCount3 = stageCount3;
	}

	public Integer getStageCount4() {
		return stageCount4;
	}

	public void setStageCount4(Integer stageCount4) {
		this.stageCount4 = stageCount4;
	}

	public Integer getStageCount5() {
		return stageCount5;
	}

	public void setStageCount5(Integer stageCount5) {
		this.stageCount5 = stageCount5;
	}

	public Integer getStageCount6() {
		return stageCount6;
	}

	public void setStageCount6(Integer stageCount6) {
		this.stageCount6 = stageCount6;
	}

	public Integer getStageCount7() {
		return stageCount7;
	}

	public void setStageCount7(Integer stageCount7) {
		this.stageCount7 = stageCount7;
	}

	public Integer getStageCount8() {
		return stageCount8;
	}

	public void setStageCount8(Integer stageCount8) {
		this.stageCount8 = stageCount8;
	}

	public String getBuildingTypes() {
		return buildingTypes;
	}

	public void setBuildingTypes(String buildingTypes) {
		this.buildingTypes = buildingTypes;
	}
	public String getBuildingTypesValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.buildingTypes, this.getBuildingTypes());
	}
	public String getStructureType() {
		return structureType;
	}
	public String getStructureTypeValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.structureType, this.getStructureType());
	}
	public void setStructureType(String structureType) {
		this.structureType = structureType;
	}
}