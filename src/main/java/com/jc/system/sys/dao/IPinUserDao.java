package com.jc.system.sys.dao;

import com.jc.system.sys.domain.PinReUser;
import com.jc.system.sys.domain.PinUser;

import com.jc.foundation.dao.IBaseDao;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IPinUserDao extends IBaseDao<PinUser>{

    /**
     * 提取用户初始化数据列表
     * @param pinUser
     * @return
     */
    List<PinReUser> queryPinUser(PinUser pinUser);

    /**
     * 还原用户信息
     * @param pinUser
     */
    void deleteBack(PinUser pinUser);
}
