package com.jc.system.group.domain;

import java.util.ArrayList;
import java.util.List;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class Group extends BaseBean{
	private static final long serialVersionUID = 1L;
	/**组名称*/
	private String name;
	/**组别分类   1.个人组别    2.公共组别*/
	private String groupType;
	/**用户id*/
	private String userId;
	/**组成员*/
	private String groupMember;
	/**组成员Id*/
	private String membersId;
	/**组成员json*/
	private String userJson;
	/**用户组成员集合*/
	private List<GroupUser> lstGroupUser = new ArrayList<>();
	public List<GroupUser> getLstGroupUser() {
		return lstGroupUser;
	}
	public void setLstGroupUser(List<GroupUser> lstGroupUser) {
		this.lstGroupUser = lstGroupUser;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserJson() {
		return userJson;
	}
	public void setUserJson(String userJson) {
		this.userJson = userJson;
	}
	public String getMembersId() {
		return membersId;
	}
	public void setMembersId(String membersId) {
		this.membersId = membersId;
	}
	public String getGroupMember() {
		return groupMember;
	}
	public void setGroupMember(String groupMember) {
		this.groupMember = groupMember;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getGroupType(){
		return groupType;
	}
	public void setGroupType(String groupType){
		this.groupType = groupType;
	}
}