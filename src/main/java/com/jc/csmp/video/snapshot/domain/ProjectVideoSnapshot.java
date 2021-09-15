package com.jc.csmp.video.snapshot.domain;

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
public class ProjectVideoSnapshot extends BaseBean {

	private static final long serialVersionUID = 1L;
	public ProjectVideoSnapshot() {
	}
	private String equiCode;
	private String imgPath;
	private String remark;

	public void setEquiCode(String equiCode) {
		this.equiCode = equiCode;
	}
	public String getEquiCode() {
		return equiCode;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
}