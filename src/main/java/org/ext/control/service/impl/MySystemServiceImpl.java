package org.ext.control.service.impl;

import com.jc.system.security.service.IClientServiceExt;
import com.jc.system.security.service.IMenuService;
import com.jc.system.security.service.impl.AbstractClientServiceExtImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Service
public class MySystemServiceImpl extends AbstractClientServiceExtImpl implements IClientServiceExt {
    @Override
    public void afterMethod(Object[] o, Method method) {
        /*System.out.println("------------------------"+method.toGenericString()+"------------------------------");
        HttpServletRequest request = null;
        Map<String,Object> menuMap = new HashMap<String,Object>();
        for(Object ars :o){
            if(ars instanceof HttpServletRequest){
                request = (HttpServletRequest) ars;
            }
            if(ars instanceof Map){
                menuMap = (Map) ars;
            }
        }
       *//* Menu menuVo = new Menu();
        menuVo.setUserId(Long.valueOf(o.toString()));
        menuMap = menuService.desktopMenuForQuery(menuVo);*//*
        System.out.println("------------------------"+menuMap.get("menuList")+"-------");*/
    }
}
