package com.jc.system.api.service;

import com.jc.system.security.domain.Department;
import java.io.Serializable;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IDeptSync extends Serializable{
	
	/**
	 * 接收组织机构基本信息的JSON串
	 * @return
	 */
	boolean sync(Department dept);
	
}
