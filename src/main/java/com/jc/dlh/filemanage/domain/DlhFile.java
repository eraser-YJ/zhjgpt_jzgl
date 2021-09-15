package com.jc.dlh.filemanage.domain;

import com.jc.foundation.domain.BaseBean;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.math.BigDecimal;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;

/**
 * @author liubq
 * @version 2020-07-10
 */
public class DlhFile extends BaseBean {

	private static final long serialVersionUID = 1L;
	public DlhFile() {
	}
	private String filename;
	private String objurl;
	private String yewuid;
	private String yewucolname;
	private String remark;
	private String tableName;
	private String userId;

	/**附件信息*/
	private String attachFile;
	private String deleteAttachFile;

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

	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilename() {
		return filename;
	}
	public void setObjurl(String objurl) {
		this.objurl = objurl;
	}
	public String getObjurl() {
		return objurl;
	}
	public void setYewuid(String yewuid) {
		this.yewuid = yewuid;
	}
	public String getYewuid() {
		return yewuid;
	}
	public void setYewucolname(String yewucolname) {
		this.yewucolname = yewucolname;
	}
	public String getYewucolname() {
		return yewucolname;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserId() {
		return userId;
	}
}