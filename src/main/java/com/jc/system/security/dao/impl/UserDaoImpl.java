package com.jc.system.security.dao.impl;

import java.util.List;

import com.jc.foundation.exception.CustomException;
import org.springframework.stereotype.Repository;

//import com.jc.android.oa.system.domain.User4M;
import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.security.dao.IUserDao;
import com.jc.system.security.domain.User;

/**
 * 用户dao实现
 * @author Administrator
 * @date 2020-06-30
 */
@Repository
public class UserDaoImpl extends BaseServerDaoImpl<User> implements IUserDao {


	@Override
	public Integer initPassword(User user) {
		return getTemplate().update(getNameSpace(user)+".initPassword", user);
	}

	@Override
	public List<User> queryUserByDeptId(User user) {
		return getTemplate().selectList(getNameSpace(user) + ".queryUserByDeptId", user);
	}

	@Override
	public List<User> queryDeptUserByOrgId(User user) {
		return getTemplate().selectList(getNameSpace(user) + ".queryDeptUserByOrgId", user);
	}

	@Override
	public List<User> queryUserByLeader(User user) {
		return getTemplate().selectList(getNameSpace(user) + ".queryUserByLeader", user);
	}

	@Override
	public List<User> queryUserByIds(User user) {
		return getTemplate().selectList(getNameSpace(user) + ".queryAllUsers", user);
	}

	@Override
	public Integer update2(User user) {
		return getTemplate().update(getNameSpace(user) + ".update2", user);
	}

	@Override
	public User checkMobile(User user) {
		return getTemplate().selectOne(getNameSpace(user) + ".checkMobile", user);
	}

	@Override
	public Integer updateUserInfo(User user) {
		return getTemplate().update(getNameSpace(user) + ".updateUserInfo", user);
	}

	@Override
	public Integer deleteBack(User user) {
		return getTemplate().update(getNameSpace(user) + ".deleteBackByIds", user);
	}

	@Override
    public List<User> queryUserAndOtherDept(User user){
		return getTemplate().selectList(getNameSpace(user) + ".queryUserAndOtherDept", user);
	}

	@Override
	public User get(User o) {
		return super.get(o);
	}

	@Override
    public User getWithLogic(User user){
		return getTemplate().selectOne( getNameSpace(user) + ".getWithLogic", user);
	}

	@Override
	public List<User> getUsersByRoleids(User user) {
		return getTemplate().selectList(getNameSpace(user) + ".getUsersByRoleids", user);
	}

	@Override
	public List<User> queryUserIdAndName(User user) {
		return getTemplate().selectList(getNameSpace(user) + ".queryUserIdAndName", user);
	}

	@Override
    public List<User> getUsersByDeptId(String id) {
		return getTemplate().selectList(getNameSpace(new User()) + ".getUsersByDeptId", id);
	}
}