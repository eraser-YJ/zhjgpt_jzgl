package com.jc.sys.domain;

import com.jc.system.security.beans.UserBean;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public class SubUserBean extends UserBean {

    private boolean isSubSystem;

    public boolean isSubSystem() {
        return isSubSystem;
    }

    public void setSubSystem(boolean subSystem) {
        isSubSystem = subSystem;
    }
}
