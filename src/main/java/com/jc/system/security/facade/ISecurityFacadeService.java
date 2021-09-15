package com.jc.system.security.facade;

import java.util.List;

import com.jc.system.security.domain.Role;
import com.jc.system.security.domain.User;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface ISecurityFacadeService {

	/**
	 * 根据用户查询该用户所在部门节点下人员
	 * @param user
	 * @return
	 */
	List<User> getDeptUsers(User user);

	/**
	 * 获取用户对应角色
	 * @param user
	 * @return
	 */
	List<Role> getUserRoles(User user);
}
