package com.jc.system.gateway.realm;

import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.gateway.domain.SysUserToken;
import com.jc.system.gateway.model.AccessToken;
import com.jc.system.gateway.service.ISysUserTokenService;
import com.jc.system.security.domain.Principal;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 认证
 * @author Administrator
 * @date 2020-07-01
 */
@Component
public class ApiAuthRealm extends AuthorizingRealm {
    @Autowired
    ISysUserTokenService sysUserTokenService;
    private IUserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof AccessToken;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Principal user = (Principal) principals.getPrimaryPrincipal();
        Set<String> permsSet = new HashSet<>();
        permsSet.add("user");
        permsSet.add("list");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();
        SysUserToken tokenEntity = sysUserTokenService.queryByToken(accessToken);
        if (tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }
        User user = getUserService().getUser(tokenEntity.getUserId());
        if (user == null) {
            throw new UnknownAccountException("帐号错误");
        }
        Principal principal = new Principal(user);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, accessToken, getName());
        return info;
    }

    /**
     * 获取系统业务对象
     */
    public IUserService getUserService() {
        if (userService == null) {
            userService = SpringContextHolder.getBean(IUserService.class);
        }
        return userService;
    }
}
