package com.jc.sys.service;

import java.util.Map;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public interface ISubMenuService {

    public Map<String,Object> desktopMenuForSub(String userId,String deptId);
}
