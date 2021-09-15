package com.jc.system.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@SuppressWarnings("ALL")
public class UserAddEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public UserAddEvent(Object source) {
		super(source);
	}

}
