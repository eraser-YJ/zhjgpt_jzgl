package com.jc.system.security.service;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.exception.SystemException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;

import java.util.List;

/**
 * 用户接口
 * @author Administrator
 * @date 2020-06-30
 */
public interface IUserService extends IBaseService<User>{
	
	@Override
    PageManager query(User user, PageManager page);
	
	PageManager queryAllUsers(User user, PageManager page);
	
	List<User> queryUserByLeader(User user);
	
	List<User> queryUserByLeaderAndDeptId(User user);
	
	List<User> queryUserByIds(String ids);
	
	User getUser(User user) throws CustomException;
	
	Integer initPassword(User user) throws CustomException;
	
	List<User> queryUserByDeptId(User user);

	List<User> queryDeptUserByOrgId(User user);
	
	User getUserById(User user);
	
	void getAllUsers();
	
	User getUser(String id);
	
	Integer updateUser(User user) throws CustomException;
	
	Integer update2(User user);
	
	User checkMobile(User user);
	
	List<Department> queryUserTreeByIds(String ids);
	
	boolean isLeader(User sourceUser,User targetUser);

	boolean isLeader(String sourceUser, String targetUser);
	
	Integer updateUserInfo(User user) throws CustomException;
	
	PageManager queryAllUsersForOrder(User user, PageManager page);
	
	Integer updateUserOrder(String userId, String orderNo, String oUserId, String oOrderNo);

	/**
	 * 恢复删除用户
	 * @param user
	 * @return
	 * @throws SystemException
	 */
	Integer deleteBack(User user) throws SystemException;

	User getWithLogic(User user);

	/**
	 * 根据角色ids查询用户对象列表
	 * @param user
	 * @return 对象
	 */
	List<User> getUsersByRoleids(User user);

	/**
	 * 查询用户对象列表
	 * @param user
	 * @return 对象仅包含用户id及名称
	 */
	List<User> queryUserIdAndName(User user);

	/**
	 * 根据组织id获取本组织人员集合
	 * @param id
	 * @return
	 */
	List<User> getUsersByDeptId(String id);

	/**
	 * 根据用户id获取用户直属领导信息
	 * @param id
	 * @return
	 * @throws CustomException
	 */
	User getLeader(String id) throws CustomException;

	/**
	 * 根据登录用户名查询用户详情
	 * @param loginName
	 * @return
	 * @throws CustomException
	 */
	User getUserByLoginName(String loginName) throws CustomException;
}