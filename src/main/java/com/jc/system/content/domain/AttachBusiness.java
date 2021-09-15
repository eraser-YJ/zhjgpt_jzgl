package com.jc.system.content.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * 附件信息
 * @author Administrator
 * @date 2020-06-30
 */
public class AttachBusiness extends BaseBean {
	private static final long serialVersionUID = 1L;
	/**附件ID*/
	private String attachId;
	/**业务ID*/
	private String businessId;
	/**业务表名*/
	private String businessTable;
	/**业务来源  0-OA  1-CRM*/
	private String businessSource;
	/**附件状态标识  1只读 0可编辑*/
	private int isEdit;
	private String attachIds;
	public String getAttachId(){
		return attachId;
	}
	public void setAttachId(String attachId){
		this.attachId = attachId;
	}
	public String getBusinessId(){
		return businessId;
	}
	public void setBusinessId(String businessId){
		this.businessId = businessId;
	}
	public String getBusinessTable(){
		return businessTable;
	}
	public void setBusinessTable(String businessTable){
		this.businessTable = businessTable;
	}
	public String getBusinessSource(){
		return businessSource;
	}
	public void setBusinessSource(String businessSource){
		this.businessSource = businessSource;
	}
	public int getIsEdit() {
		return isEdit;
	}
	public void setIsEdit(int isEdit) {
		this.isEdit = isEdit;
	}
	public String getAttachIds() {
		return attachIds;
	}
	public void setAttachIds(String attachIds) {
		this.attachIds = attachIds;
	}
}