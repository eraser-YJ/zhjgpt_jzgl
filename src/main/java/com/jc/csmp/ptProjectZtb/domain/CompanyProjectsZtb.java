package com.jc.csmp.ptProjectZtb.domain;

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
public class CompanyProjectsZtb extends BaseBean {

	private static final long serialVersionUID = 1L;
	public CompanyProjectsZtb() {
	}
	private String pcpZblx;
	private String pcpZbfs;
	private String pcpZbdwmc;
	private Date pcpZbrq;
	private Date pcpZbrqBegin;
	private Date pcpZbrqEnd;
	private BigDecimal pcpZbje;
	private String pcpZbtzs;
	private String pcpXq;
	private String pcpProjectNum;
	private String dlhDataId;
	private Date dlhDataModifyDate;
	private Date dlhDataModifyDateBegin;
	private Date dlhDataModifyDateEnd;
	private Date jdTimeBegin;
	private Date jdTimeEnd;
	private String dlhDataSrc;
	private String dlhDataUser;
	private String pcpArea;
	private String projectName;
	private String projectType;
	private String jdType;

	private String biddingTsss;
	private String biddingYysl;
	private String biddingRemark;
	private String biddingType;
	private String biddingDljgmc;
	private String biddingDljgbm;



	public String getBiddingTsss() {
		return biddingTsss;
	}

	public void setBiddingTsss(String biddingTsss) {
		this.biddingTsss = biddingTsss;
	}

	public String getBiddingYysl() {
		return biddingYysl;
	}

	public void setBiddingYysl(String biddingYysl) {
		this.biddingYysl = biddingYysl;
	}

	public String getBiddingRemark() {
		return biddingRemark;
	}

	public void setBiddingRemark(String biddingRemark) {
		this.biddingRemark = biddingRemark;
	}

	public String getBiddingType() {
		return biddingType;
	}

	public void setBiddingType(String biddingType) {
		this.biddingType = biddingType;
	}
	public String getBiddingTypeValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.pcpZblx, this.getBiddingType());
	}
	public void setPcpZblx(String pcpZblx) {
		this.pcpZblx = pcpZblx;
	}
	public String getPcpZblx() {
		return pcpZblx;
	}
	public void setPcpZbfs(String pcpZbfs) {
		this.pcpZbfs = pcpZbfs;
	}
	public String getPcpZbfs() {
		return pcpZbfs;
	}
	public void setPcpZbdwmc(String pcpZbdwmc) {
		this.pcpZbdwmc = pcpZbdwmc;
	}
	public String getPcpZbdwmc() {
		return pcpZbdwmc;
	}
	public void setPcpZbrqBegin(Date pcpZbrqBegin) {
		this.pcpZbrqBegin = pcpZbrqBegin;
	}
	public Date getPcpZbrqBegin() {
		return pcpZbrqBegin;
	}
	public void setPcpZbrqEnd(Date pcpZbrqEnd) {
		if(pcpZbrqEnd == null){
			return;
		}
		this.pcpZbrqEnd = DateUtils.fillTime(pcpZbrqEnd);
	}
	public Date getPcpZbrqEnd() {
		return pcpZbrqEnd;
	}
	public void setPcpZbrq(Date pcpZbrq) {
		this.pcpZbrq = pcpZbrq;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getPcpZbrq() {
		return pcpZbrq;
	}
	public void setPcpZbje(BigDecimal pcpZbje) {
		this.pcpZbje = pcpZbje;
	}
	public BigDecimal getPcpZbje() {
		return pcpZbje;
	}
	public void setPcpZbtzs(String pcpZbtzs) {
		this.pcpZbtzs = pcpZbtzs;
	}
	public String getPcpZbtzs() {
		return pcpZbtzs;
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
	public void setPcpArea(String pcpArea) {
		this.pcpArea = pcpArea;
	}
	public String getPcpArea() {
		return pcpArea;
	}
	public String getPcpAreaValue() {
		return GlobalUtil.getDicValue(DicKeyEnum.region, this.getPcpArea());
	}
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBiddingDljgmc() {
		return biddingDljgmc;
	}

	public void setBiddingDljgmc(String biddingDljgmc) {
		this.biddingDljgmc = biddingDljgmc;
	}

	public String getBiddingDljgbm() {
		return biddingDljgbm;
	}

	public void setBiddingDljgbm(String biddingDljgbm) {
		this.biddingDljgbm = biddingDljgbm;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getJdType() {
		return jdType;
	}

	public void setJdType(String jdType) {
		this.jdType = jdType;
	}

	public Date getJdTimeBegin() {
		return jdTimeBegin;
	}

	public void setJdTimeBegin(Date jdTimeBegin) {
		this.jdTimeBegin = jdTimeBegin;
	}

	public Date getJdTimeEnd() {
		return jdTimeEnd;
	}

	public void setJdTimeEnd(Date jdTimeEnd) {
		this.jdTimeEnd = jdTimeEnd;
	}
}