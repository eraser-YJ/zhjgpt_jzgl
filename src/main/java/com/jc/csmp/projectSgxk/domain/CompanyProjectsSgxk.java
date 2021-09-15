package com.jc.csmp.projectSgxk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.csmp.common.enums.DicKeyEnum;
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
public class CompanyProjectsSgxk extends BaseBean {

	private static final long serialVersionUID = 1L;
	public CompanyProjectsSgxk() {
	}
	private String pcpHtlb;
	private String pcpBdmc;
	private String pcpHtbh;
	private BigDecimal pcpHtje;
	private Date pcpQdrq;
	private Date pcpQdrqBegin;
	private Date pcpQdrqEnd;
	private String pcpXq;
	private String pcpProjectNum;
	private String dlhDataId;
	private Date dlhDataModifyDate;
	private Date dlhDataModifyDateBegin;
	private Date dlhDataModifyDateEnd;
	private String dlhDataSrc;
	private String dlhDataUser;
	private String pcpBzry;
	private String pcpDh;

	private String projectname;
	private String projectaddress;
	private String constructionOrganization;
	private String projectmanager;
	private String builddept;
	private BigDecimal projectmoney;
	private String projectArea;

	public void setPcpHtlb(String pcpHtlb) {
		this.pcpHtlb = pcpHtlb;
	}
	public String getPcpHtlb() {
		return pcpHtlb;
	}
	public void setPcpBdmc(String pcpBdmc) {
		this.pcpBdmc = pcpBdmc;
	}
	public String getPcpBdmc() {
		return pcpBdmc;
	}
	public void setPcpHtbh(String pcpHtbh) {
		this.pcpHtbh = pcpHtbh;
	}
	public String getPcpHtbh() {
		return pcpHtbh;
	}
	public void setPcpHtje(BigDecimal pcpHtje) {
		this.pcpHtje = pcpHtje;
	}
	public BigDecimal getPcpHtje() {
		return pcpHtje;
	}
	public void setPcpQdrqBegin(Date pcpQdrqBegin) {
		this.pcpQdrqBegin = pcpQdrqBegin;
	}
	public Date getPcpQdrqBegin() {
		return pcpQdrqBegin;
	}
	public void setPcpQdrqEnd(Date pcpQdrqEnd) {
		if(pcpQdrqEnd == null){
			return;
		}
		this.pcpQdrqEnd = DateUtils.fillTime(pcpQdrqEnd);
	}
	public Date getPcpQdrqEnd() {
		return pcpQdrqEnd;
	}
	public void setPcpQdrq(Date pcpQdrq) {
		this.pcpQdrq = pcpQdrq;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getPcpQdrq() {
		return pcpQdrq;
	}
	public void setPcpXq(String pcpXq) {
		this.pcpXq = pcpXq;
	}
	public String getPcpXq() {
		return pcpXq;
	}
	public void setPcpProjectNum(String pcpProjectNum) {
		this.pcpProjectNum = pcpProjectNum;
	}
	public String getPcpProjectNum() {
		return pcpProjectNum;
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
	public void setPcpBzry(String pcpBzry) {
		this.pcpBzry = pcpBzry;
	}
	public String getPcpBzry() {
		return pcpBzry;
	}
	public void setPcpDh(String pcpDh) {
		this.pcpDh = pcpDh;
	}
	public String getPcpDh() {
		return pcpDh;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getProjectaddress() {
		return projectaddress;
	}

	public void setProjectaddress(String projectaddress) {
		this.projectaddress = projectaddress;
	}

	public String getConstructionOrganization() {
		return constructionOrganization;
	}

	public void setConstructionOrganization(String constructionOrganization) {
		this.constructionOrganization = constructionOrganization;
	}

	public String getProjectmanager() {
		return projectmanager;
	}

	public void setProjectmanager(String projectmanager) {
		this.projectmanager = projectmanager;
	}

	public String getBuilddept() {
		return builddept;
	}

	public void setBuilddept(String builddept) {
		this.builddept = builddept;
	}

	public BigDecimal getProjectmoney() {
		return projectmoney;
	}

	public void setProjectmoney(BigDecimal projectmoney) {
		this.projectmoney = projectmoney;
	}

	public String getProjectArea() {
		return projectArea;
	}
	public String getProjectAreaValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.region, this.getProjectArea());
	}
	public void setProjectArea(String projectArea) {
		this.projectArea = projectArea;
	}
}