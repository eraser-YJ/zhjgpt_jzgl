package com.jc.csmp.equipment.info.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @author liubq
 * @version 2020-07-10
 */
public class EquipmentExinfo extends BaseBean {

	private static final long serialVersionUID = 1L;
	public EquipmentExinfo() {
	}
	private String content;

	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
}