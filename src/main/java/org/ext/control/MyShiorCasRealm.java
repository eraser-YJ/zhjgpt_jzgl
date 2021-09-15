package org.ext.control;

import com.jc.system.security.domain.User;
import org.apache.shiro.cas.CasRealm;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public class MyShiorCasRealm extends CasRealm {
    @Override
    public void addSessionOper(User user){
        System.out.println(1111);
       /* //principalCollection.add("我看看好用不","tttid");
        SecurityUtils.getSubject().getSession().setAttribute("tttid","我看看好用不");
        System.out.println("-----------------add session----------------"+user.getDisplayName());

        //InitClazzUtils.setClazzMap("MenuController-manage","org.ext.control.service.impl.MySystemServiceImpl");
        InitClazzUtils.setClazzMap("UserController-update","org.ext.control.service.impl.MyUserServiceImpl");
        //登录同步用户数据*/

    }
}
