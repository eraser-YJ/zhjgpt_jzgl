package com.jc.system.api.service;

import java.io.Serializable;

import com.jc.system.security.domain.User;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IUserSync extends Serializable{
	
	/**
	 * 接收用户基本信息的JSON串
	 * @return
	 */
	boolean sync(User user);
	
}
