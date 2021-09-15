package com.jc.system.security.service;

import com.jc.foundation.exception.CustomException;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.domain.User;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ISystemUserService {
    User getUser(String id) throws CustomException;

    Integer update2(User user);

    User getUser(User user) throws CustomException;

    User get(User user)  throws CustomException;

    List<Menu> queryMenu(String userId);
}
