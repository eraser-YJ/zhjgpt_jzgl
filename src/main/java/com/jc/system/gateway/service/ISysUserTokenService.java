package com.jc.system.gateway.service;

import com.jc.foundation.service.IBaseService;
import com.jc.system.gateway.domain.SysUserToken;
import com.jc.system.gateway.utils.ApiResponse;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ISysUserTokenService extends IBaseService<SysUserToken> {
    ApiResponse createToken(String userId,String clientType);

    void logout(String userId);

    SysUserToken queryByToken(String token);
}
