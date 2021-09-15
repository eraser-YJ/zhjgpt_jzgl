package com.jc.system.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class DepartmentUpdateEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public DepartmentUpdateEvent(Object source) {
		super(source);
	}

}
