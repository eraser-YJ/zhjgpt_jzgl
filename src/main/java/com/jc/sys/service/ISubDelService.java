package com.jc.sys.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.sys.domain.SubDepartment;
import com.jc.sys.domain.SubUser;

import java.util.Map;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public interface ISubDelService {

	public Map<String, Object> copyDeptTree() ;

	
}