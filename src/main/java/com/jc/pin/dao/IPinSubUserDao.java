package com.jc.pin.dao;

import com.jc.pin.domain.PinReSubUser;
import com.jc.pin.domain.PinSubUser;

import com.jc.foundation.dao.IBaseDao;

import java.util.List;

/**
 * Plugin初始化方类
 * @author Administrator
 * @date 2020-06-30
 */
public interface IPinSubUserDao extends IBaseDao<PinSubUser>{

    /**
     * 提取用户初始化数据列表
     * @param pinUser
     * @return
     */
    List<PinReSubUser> queryPinUser(PinSubUser pinUser);
}
