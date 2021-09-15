package com.jc.system.security.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.GlobalUtil;
import com.jc.system.group.domain.GroupUser;
import com.jc.system.security.beans.UserBean;

/***
 * 组织实体
 * @author Administrator
 * @date 2020-06-29
 */
public class Department extends BaseBean {
	private static final long serialVersionUID = 1L;

	/**部门编号*/
	private String code;
	/**部门名称*/
	private String name;
	/**部门描述*/
	private String deptDesc;
	/**部门主管领导ID*/
	private String leaderId;
	/**部门分管领导ID*/
	private Integer chargeLeaderId;
	/**上级节点Id*/
	private String parentId;
	/**上级主管部门id*/
	private Integer managerDept;
	/**标记是部门or机构节点  ’0‘--部门  ’1‘--机构*/
	private Integer deptType;
	/**所属机构ID*/
	private String organizationId;
	/**部门排序*/
	private Integer queue;
	/**组织全称*/
	private String fullName;
	/**租户简称*/
	private String shortName;
	/**租户用户名*/
	private String userName;
	/**密码*/
	private String password;
	/**0-试用 1-正式*/
	private String type;
	/**组织状态  ’0‘--启用 ’1‘--禁用 ’2’--锁定 ‘3’--删除*/
	private String status;
	/**启用时间*/
	private Date openDay;
	/**到期时间*/
	private Date endDay;
	/**文件空间(m)*/
	private BigDecimal fileSpace;
	/**已用空间*/
	private BigDecimal usedSpace;
	/**账户余额*/
	private BigDecimal balance;
	/**短信费用*/
	private BigDecimal smsBalance;
	/**租户LOGO*/
	private String logo;
	/**租户联系人*/
	private String cont;
	/**联系人电话*/
	private String telp;
	/**用户邮箱*/
	private String email;
	/**租户备注*/
	private String memo;
	/**负责人	自定义属性*/
	private String displayName;
	/**负责人是否被删除标识*/
	private int userDelFlag;
	/**上级部门	自定义属性*/
	private String parentName;
	/**上级部门 权重系数*/
	private Integer parentWeight;
	/**部门下的人员	自定义属性*/
	private List<UserBean> userBeanList = new ArrayList<>();
	/**部门下的人员	自定义属性*/
	private List<User> users = new ArrayList<>();
	/**部门下的角色	自定义属性*/
	private List<Role> roles = new ArrayList<>();
	/**组别下的人员	自定义属性*/
	private List<GroupUser> groupUsers = new ArrayList<>();
	/**机构ID	自定义属性*/
	private String orgId;
	/**机构名称	自定义属性*/
	private String orgName;
	/**是否有权限操作	自定义属性 1:有权操作,0:无权操作*/
	private String isChecked = "1";
	/**1超管 2机构管理员 3普通用户 4无意义*/
	private int userType = 1;
	/**根据部门ID集合行查询*/
	private String deptIds;
	/**权重系数*/
	private Integer weight;
	/**资源库id*/
	private String resourceId;
	/**组织客户端token*/
	private String deptToken;
	/**多用户id字符串*/
	private String[] ids;
	/**code模糊查询*/
	private String likeCode;
	/**传递部门id，转换成code进行模糊查询*/
	private String likeIdToCode;
	private String notLikeIdToCode;
	private String likeName;
	/**不能选择的code*/
	private String[] noChangeCodes;
	private int userCount;
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeptDesc() {
		return deptDesc;
	}
	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}
	public String getLeaderId() {
		return leaderId;
	}
	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}
	public Integer getChargeLeaderId() {
		return chargeLeaderId;
	}
	public void setChargeLeaderId(Integer chargeLeaderId) {
		this.chargeLeaderId = chargeLeaderId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public Integer getManagerDept() {
		return managerDept;
	}
	public void setManagerDept(Integer managerDept) {
		this.managerDept = managerDept;
	}
	public Integer getDeptType() {
		return deptType;
	}
	public void setDeptType(Integer deptType) {
		this.deptType = deptType;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public Integer getQueue() {
		return queue;
	}
	public void setQueue(Integer queue) {
		this.queue = queue;
	}
	public List<User> getUsers() {
		return users;
	}
	public void addUser(User user) {
		users.add(user);
	}
	public void addUserBean(UserBean uBean) {
		userBeanList.add(uBean);
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void addRole(Role role) {
		roles.add(role);
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getOpenDay() {
		return openDay;
	}
	public void setOpenDay(Date openDay) {
		this.openDay = openDay;
	}
	public Date getEndDay() {
		return endDay;
	}
	public void setEndDay(Date endDay) {
		this.endDay = endDay;
	}
	public BigDecimal getFileSpace() {
		return fileSpace;
	}
	public void setFileSpace(BigDecimal fileSpace) {
		this.fileSpace = fileSpace;
	}
	public BigDecimal getUsedSpace() {
		return usedSpace;
	}
	public void setUsedSpace(BigDecimal usedSpace) {
		this.usedSpace = usedSpace;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getSmsBalance() {
		return smsBalance;
	}
	public void setSmsBalance(BigDecimal smsBalance) {
		this.smsBalance = smsBalance;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getCont() {
		return cont;
	}
	public void setCont(String cont) {
		this.cont = cont;
	}
	public String getTelp() {
		return telp;
	}
	public void setTelp(String telp) {
		this.telp = telp;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public String getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}
	public String getDeptIds() {
		return deptIds;
	}
	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public List<GroupUser> getGroupUsers() {
		return groupUsers;
	}
	public void setGroupUsers(List<GroupUser> groupUsers) {
		this.groupUsers = groupUsers;
	}
	public String getDeptToken() {
		return deptToken;
	}
	public void setDeptToken(String deptToken) {
		this.deptToken = deptToken;
	}
	public List<UserBean> getUserBeanList() {
		return userBeanList;
	}
	public void setUserBeanList(List<UserBean> userBeanList) {
		this.userBeanList = userBeanList;
	}
	public int getUserDelFlag() {
		return userDelFlag;
	}
	public void setUserDelFlag(int userDelFlag) {
		this.userDelFlag = userDelFlag;
	}
	@Override
	public Integer getWeight() {
		return weight;
	}
	@Override
    public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Integer getParentWeight() {
		return parentWeight;
	}
	public void setParentWeight(Integer parentWeight) {
		this.parentWeight = parentWeight;
	}
	public String getLikeCode() {
		return likeCode;
	}
	public void setLikeCode(String likeCode) {
		this.likeCode = likeCode;
	}
	public String getLikeIdToCode() {
		return likeIdToCode;
	}
	public void setLikeIdToCode(String likeIdToCode) {
		this.likeIdToCode = likeIdToCode;
	}

	public String getNotLikeIdToCode() {
		return notLikeIdToCode;
	}

	public void setNotLikeIdToCode(String notLikeIdToCode) {
		this.notLikeIdToCode = notLikeIdToCode;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public boolean isNeedChagne() {
		return GlobalUtil.needChange(this.getCode());
	}

	public static Department createTreeData(String id, String parentId, String name) {
		Department dept = new Department();
		dept.setId(id);
		dept.setParentId(parentId);
		dept.setName(name);
		return dept;
	}

	public static Department createTreeData(String id, String parentId, String name, Integer deptType) {
		Department dept = new Department();
		dept.setId(id);
		dept.setParentId(parentId);
		dept.setName(name);
		dept.setDeptType(deptType);
		return dept;
	}

	public String getLikeName() {
		return likeName;
	}

	public void setLikeName(String likeName) {
		this.likeName = likeName;
	}

	public String[] getNoChangeCodes() {
		return noChangeCodes;
	}

	public void setNoChangeCodes(String[] noChangeCodes) {
		this.noChangeCodes = noChangeCodes;
	}

	public String getDeptTypeTreeIcon() {
		if (this.getDeptType() != null) {
			if (this.getDeptType().intValue() == 1) {
				return "fa-flag";
			} else if (this.getDeptType().intValue() == 2) {
				//项目
				return "fa-book5";
			} else if (this.getDeptType().intValue() == 3) {
				//周报
				return "fa-files";
			} else {
				return "fa-office";
			}
		}
		return "fa-office";
	}
}