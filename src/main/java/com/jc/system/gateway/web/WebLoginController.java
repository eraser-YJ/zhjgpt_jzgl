package com.jc.system.gateway.web;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.Result;
import com.jc.foundation.util.ResultCode;
import com.jc.foundation.web.BaseController;
import com.jc.system.common.service.ICommonService;
import com.jc.system.gateway.model.SysLoginForm;
import com.jc.system.gateway.service.ISysUserBindingService;
import com.jc.system.gateway.service.ISysUserTokenService;
import com.jc.system.gateway.utils.ApiResponse;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.check.CheckFactory;
import com.jc.system.security.domain.User;
import com.jc.system.security.exception.UserLockedException;
import com.jc.system.security.exception.UserPasswordException;
import com.jc.system.security.service.ISystemUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Web前端、移动端登录相关
 * @author Administrator
 */
@Api(value = "API - WebLoginController", description = "h5登录api接口")
@RestController
public class WebLoginController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WebLoginController.class);
    public static final String WEB_H5 = "WEB_H5";

    @Autowired
    private ISystemUserService sysUserService;
    @Autowired
    private ISysUserTokenService sysUserTokenService;

    @Autowired
    ISysUserBindingService sysUserBindingService;

    @Autowired
    ICommonService commonService;

    public WebLoginController() {
    }

    /**
     * 登录
     */
    @ApiOperation(value = "登录方法",httpMethod="POST") 
    @PostMapping("/gateway/login")
    public Result login(@ApiParam(required = true, name = "form", value = "登录表单")@RequestBody SysLoginForm form)
            throws IOException {
        try {
            User user = CheckFactory.checkForRsaPassword(form.getUsername(), form.getPassword());
            //生成token，并保存到数据库
            ApiResponse r = sysUserTokenService.createToken(user.getId(), WEB_H5);

            CheckFactory.checkChioce(user, null, "checkForUserDisabled,checkForUnknownAccount");//账号锁定 判断
            Map<String,Object> data=new HashMap<>();
            data.put("token",r.get("token"));
            data.put("expire",r.get("expire"));
            data.put("userId",user.getId());
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof UserPasswordException) {
                return Result.failure(ResultCode.USER_LOGIN_ERROR);
            }else if(e instanceof UserLockedException) {
                return Result.failure(ResultCode.USER_ACCOUNT_FORBIDDEN);
            }else {
                return Result.failure(ResultCode.USER_NOT_LOGGED_IN);
            }
        }
    }

    /**
     * 退出
     */
    @ApiOperation(value = "退出",httpMethod="POST") 
    @PostMapping("/gateway/logout")
    public ApiResponse logout() {
        sysUserTokenService.logout(SystemSecurityUtils.getUser().getId());
        return ApiResponse.ok();
    }


    @RequiresPermissions("user:list")
    @GetMapping("/api/v1/test")
    public ApiResponse test() {
        int count = commonService.getOnlineUserCount();
        return ApiResponse.ok("Test success!Online user count is " + count);
    }

}
