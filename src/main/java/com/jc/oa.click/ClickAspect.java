package com.jc.oa.click;





import java.util.Date;

import com.jc.foundation.util.GlobalContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;


import com.jc.oa.click.domain.Click;
import com.jc.oa.click.service.IClickService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Menu;

import com.jc.system.security.service.IMenuService;

public class ClickAspect implements MethodInterceptor{

	@Autowired
	private IClickService clickService;
	@Autowired
	private IMenuService menuService;
    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object[] ars = mi.getArguments();
        // 判断该方法是否加了@ActionClick 注解
        if (mi.getMethod().isAnnotationPresent(ActionClick.class)) {
            ActionClick logAnnotation = mi.getMethod().getAnnotation(ActionClick.class);
            String userId = SystemSecurityUtils.getUser().getId();
            Menu  menuvo = menuService.queryMenuForUrl(logAnnotation.menuAction(),GlobalContext.getProperty("subsystem.id"));
          //  Menu menu = new Menu();
        //    List<Menu> menuList = menuService.queryAll(menu);
//            for(int i =0;i<menuList.size();i++){
//                if(){}
//
//            }

            Click click = new Click();
            click.setUserId(userId);
            click.setClicks(1L);
            click.setMenuId(menuvo.getId());
            click.setMenuName(menuvo.getName());
            click.setMenuClass(menuvo.getIcon());
            click.setMenuAction(menuvo.getActionName());
            click.setModifyDate(new Date());
            clickService.save(click);
        }
        return mi.proceed();
    }
}
