package com.jc.system.security.dao;

import java.util.List;
import com.jc.foundation.dao.IBaseDao;
import com.jc.system.security.domain.User;

/**
 * 用户dao
 * @author Administrator
 * @date 2020-06-30
 */
public interface IUserDao extends IBaseDao<User> {

	/**
	 * 初始化密码
	 * @param user
	 * @return
	 */
	Integer initPassword(User user);

	/**
	 * 根据部门ID查询用户
	 * @param user
	 * @return
	 */
	List<User> queryUserByDeptId(User user);

	/**
	 * 根据机构ID查询部门用户
	 * @param user
	 * @return
	 */
	List<User> queryDeptUserByOrgId(User user);

	/**
	 * 根据用户ID查询下级用户
	 * @param user
	 * @return
	 */
	List<User> queryUserByLeader(User user);

	/**
	 * 根据IDS查询用户
	 * @param user
	 * @return
	 */
	List<User> queryUserByIds(User user);

	/**
	 * 修改
	 * @param user
	 * @return
	 */
	Integer update2(User user);

	/**
	 * 检查手机号
	 * @param user
	 * @return
	 */
	User checkMobile(User user);

	/**
	 * 修改信息
	 * @param user
	 * @return
	 */
	Integer updateUserInfo(User user);

	/**
	 * 查询用户(包括兼职部门)
	 * @param user
	 * @return
	 */
	List<User> queryUserAndOtherDept(User user);

	/**
	 * 恢复删除用户
	 * @param user
	 * @return
	 */
	Integer deleteBack(User user);

	/**
	 * @param user
	 * @return
	 */
	User getWithLogic(User user);

	/**
	 * 根据角色ids查询用户对象列表
	 * @param user
	 * @return
	 */
	List<User> getUsersByRoleids(User user);

	/**
	 * 查询用户对象列表
	 * @param user
	 * @return
	 */
	List<User> queryUserIdAndName(User user);

	/**
	 * 根据组织id获取组织下的人员集合
	 * @param id
	 * @return
	 */
	List<User> getUsersByDeptId(String id);
}
