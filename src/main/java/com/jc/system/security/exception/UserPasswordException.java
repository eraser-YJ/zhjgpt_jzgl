package com.jc.system.security.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class UserPasswordException extends AuthenticationException{

	private static final long serialVersionUID = 1L;
	
	public UserPasswordException(){
		super();
	}
	
	public UserPasswordException(String message){
		super(message);
	}

}
