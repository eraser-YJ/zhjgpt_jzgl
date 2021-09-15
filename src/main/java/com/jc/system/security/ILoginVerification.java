package com.jc.system.security;

import com.jc.system.security.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ILoginVerification extends Serializable {

    User getUser(HttpServletRequest request);

}
