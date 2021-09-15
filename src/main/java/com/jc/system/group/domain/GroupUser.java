package com.jc.system.group.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class GroupUser extends BaseBean{
	private static final long serialVersionUID = 1L;
	/**组ID*/
	private String groupId;
	/**用户ID*/
	private String userId;
	/**用户姓名*/
	private String displayName;
	/**排序*/
	private Integer orderNo;
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getGroupId(){
		return groupId;
	}
	public void setGroupId(String groupId){
		this.groupId = groupId;
	}
	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
}