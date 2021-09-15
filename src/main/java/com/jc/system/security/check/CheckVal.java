package com.jc.system.security.check;

import com.jc.system.security.UserToken;
import com.jc.system.security.domain.User;

/**
 * @author Administrator
 * @date 2020-07-01
 */
abstract public class CheckVal {

	abstract public void check(User user,UserToken token) throws Exception;

}
