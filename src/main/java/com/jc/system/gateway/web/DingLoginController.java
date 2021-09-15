package com.jc.system.gateway.web;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.common.service.ICommonService;
import com.jc.system.gateway.domain.SysUserBinding;
import com.jc.system.gateway.model.SysLoginForm;
import com.jc.system.gateway.service.ISysUserBindingService;
import com.jc.system.gateway.service.ISysUserTokenService;
import com.jc.system.gateway.utils.ApiResponse;
import com.jc.system.security.check.CheckFactory;
import com.jc.system.security.domain.Unique;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.ISystemUserService;
import com.jc.system.security.service.IUniqueService;
import com.jc.system.security.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 钉钉生态登录相关
 * @author Administrator
 * @date 2020-07-01
 */
@Api(value = "API - DingLoginController", description = "钉钉生态登录相关")
@RestController
public class DingLoginController extends BaseController {
    private static final Logger dingLogger = LoggerFactory.getLogger(DingLoginController.class);

    private static final String DING_H5_APP = "DING_H5_APP";
    private static final String DING_MINI_APP = "DING_MINI_APP";
    private static final String DING_CODE_SUCCESS = "0";
    private static final String DING_CODE_TOKEN_ERROR = "40014";
    private static final String DING_ERROR_CODE = "errcode";
    private static final String DING_ERROR_MSG = "errmsg";
    private static final String DING_DATA_USERID = "userid";
    private static final String USER_ID = "userId";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String EXPIRES_IN = "expires_in";
    /**
     * 开发者后台->企业自建应用->选择您创建的E应用->查看->AppKey
     */
    private String DING_H5_APP_KEY;
    /**
     * 开发者后台->企业自建应用->选择您创建的E应用->查看->AppSecret
     */
    private String DING_H5_APP_SECRET;
    /**
     * 开发者后台->企业自建应用->选择您创建的E应用->查看->AppKey
     */
    private String DING_APP_KEY;
    /**
     * 开发者后台->企业自建应用->选择您创建的E应用->查看->AppSecret
     */
    private String DING_APP_SECRET;
    private String AUTO_USER_CREATE;
    private String AUTO_USER_DEPT;
    /**
     * 获取用户姓名的接口url
     */
    private final String URL_USER_GET = "https://oapi.dingtalk.com/user/get";
    private final String DING_H5_ACCESS_TOKEN = "h5_access_token";
    private final String DING_APP_ACCESS_TOKEN = "app_access_token";


    private ConcurrentHashMap<String, String> accessTokenMap = new ConcurrentHashMap<>();


    private RestTemplate restTemplate;


    @Autowired
    private ISysUserTokenService sysUserTokenService;
    @Autowired
    ISysUserBindingService sysUserBindingService;
    @Autowired
    ICommonService commonService;
    @Autowired
    IUniqueService uniqueService;
    @Autowired
    IUserService userService;

    public DingLoginController() {
        this.restTemplate = new RestTemplate();
        //初始化单点登录地址
        Properties p = new Properties();
        InputStream isp = this.getClass().getClassLoader().getResourceAsStream("jcap.properties");
        try {
            p.load(isp);
            DING_H5_APP_KEY = p.getProperty("dingding.h5AppKey");
            DING_H5_APP_SECRET = p.getProperty("dingding.h5AppSecret");
            DING_APP_KEY = p.getProperty("dingding.appKey");
            DING_APP_SECRET = p.getProperty("dingding.appSecret");
            AUTO_USER_CREATE = p.getProperty("user.auto.create");
            AUTO_USER_DEPT = p.getProperty("user.auto.dept");
        } catch (IOException e) {
            dingLogger.error(e.getMessage());
        }
    }

    @ApiOperation(value = "钉钉小程序用户绑定系统帐号并登录", httpMethod = "POST")
    @PostMapping("/gateway/dingMiniAuthLogin")
    public ApiResponse dingMiniAppAuth(@RequestBody SysLoginForm form) {
        dingLogger.info("start dingMiniAuthLogin, param -> form:{}", form.toString());
        if (StringUtil.isEmpty(form.getExtId())) {
            return ApiResponse.error("参数不正确");
        }
        return dingAuth(form, DING_MINI_APP);
    }

    @ApiOperation(value = "钉钉H5应用用户绑定系统帐号并登录", httpMethod = "POST")
    @PostMapping("/gateway/dingH5Auth")
    public ApiResponse dingH5Auth(@RequestBody SysLoginForm form) {
        dingLogger.info("start dingH5Auth, param -> form:{}", form.toString());
        if (StringUtil.isEmpty(form.getExtId())) {
            return ApiResponse.error("参数不正确");
        }
        return dingAuth(form, DING_H5_APP);
    }

    @ApiOperation(value = "钉钉小程序Code登录", httpMethod = "GET")
    @GetMapping("/gateway/dingMiniCodeLogin")
    public ApiResponse dingMiniCodeLogin(@ApiParam(required = true, name = "code", value = "code") @NotNull @RequestParam String code) {
        dingLogger.info("start dingMiniCodeLogin param -> code {}", code);

        return codeLogin(code, DING_MINI_APP);
    }

    @ApiOperation(value = "钉钉H5应用Code登录", httpMethod = "GET")
    @GetMapping("/gateway/dingH5CodeLogin")
    public ApiResponse dingH5CodeLogin(@ApiParam(required = true, name = "code", value = "code") @NotNull @RequestParam String code) {
        dingLogger.info("start dingH5CodeLogin param -> code {}", code);
        return codeLogin(code, DING_H5_APP);
    }

    private ApiResponse codeLogin(String code, String type) {
        dingLogger.info("start CodeLogin param -> code {}", code);
        String token;
        try {
            token = getDingAccessToken(DING_H5_APP.equals(type) ? DING_H5_ACCESS_TOKEN : DING_APP_ACCESS_TOKEN);
        } catch (CustomException e) {
            return ApiResponse.error(e.getMessage());
        }
        /**
         * 获取用户在企业内userId的接口URL GET https://oapi.dingtalk.com/user/getuserinfo?access_token=access_token&code=code
         */
        String URL_GET_USER_INFO = "https://oapi.dingtalk.com/user/getuserinfo?access_token=%s&code=%s";
        String url = String.format(URL_GET_USER_INFO, token, code);
        dingLogger.info("getDingUserInfo remote url -> {}", url);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class);
        } catch (Exception e) {
            e.printStackTrace();
            dingLogger.error("getDingUserInfo error :  error -> errMsg {}", e.getMessage());
            return ApiResponse.error("钉钉用户服务异常:" + e.getMessage());
        }
        if (response.getStatusCode() != HttpStatus.OK) {
            dingLogger.error("getDingUserInfo error :  error -> errMsg {}", response);
            return ApiResponse.error("钉钉用户服务异常:" + response.getStatusCode());
        }
        dingLogger.info("getDingUserInfo response -> {}", response);
        String body = null;
        try {
            body = new String(response.getBody().getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ApiResponse.error("钉钉用户服务异常:" + e.getMessage());
        }
        Map<String, Object> wxResult = (Map) JsonUtil.json2Java(body, Map.class);
        String errCode = wxResult.get(DING_ERROR_CODE) != null ? wxResult.get(DING_ERROR_CODE).toString() : "-1";
        String errMsg = wxResult.get(DING_ERROR_MSG) != null ? wxResult.get(DING_ERROR_MSG).toString() : "未知";

        if (DING_CODE_SUCCESS.equals(errCode)) {
            String userId = wxResult.get(DING_DATA_USERID) != null ? wxResult.get(DING_DATA_USERID).toString() : "";
            if (StringUtils.isNotEmpty(userId)) {
                return dingLogin(userId, type).put(USER_ID, userId);
            }
        } else if (DING_CODE_TOKEN_ERROR.equals(errCode)) {
            //access token has expired , need refresh
            accessTokenMap.remove(DING_H5_APP.equals(type) ? DING_H5_ACCESS_TOKEN : DING_APP_ACCESS_TOKEN);
            return codeLogin(code, type);
        } else {
            dingLogger.info("codeLogin finished, code -> {},msg -> {}", errCode, errMsg);
            return ApiResponse.error(errMsg);
        }
        dingLogger.info("codeLogin finished, {}", "无法获取钉钉用户");
        return ApiResponse.error("无法获取钉钉用户");
    }

    private ApiResponse dingLogin(@NotNull String dingUserId,
                                  String type) {
        dingLogger.info("start dingLogin, param -> dingUserId:{},type:{}", dingUserId, type);
        if (StringUtil.isEmpty(dingUserId)) {
            return ApiResponse.error("登录参数不正确");
        }

        SysUserBinding binding = new SysUserBinding();
        binding.setDingUserId(dingUserId);

        try {
            binding = sysUserBindingService.get(binding);
            if (binding != null && !StringUtil.isEmpty(binding.getUserId())) {
                //生成token，并保存到数据库
                ApiResponse r = sysUserTokenService.createToken(binding.getUserId(), type);
                dingLogger.info("dingLogin success! response -> {}", r.get("token"));
                r.put("dingUserId", dingUserId);
                return r;
            } else {
                dingLogger.error("dingLogin failure! -> 用户未绑定钉钉账号");
                return ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "账号未绑定").put("dingUserId", dingUserId);
            }
        } catch (Exception e) {
            dingLogger.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    private ApiResponse dingAuth(SysLoginForm form, String type) {
        User user = null;
        try {
            user = CheckFactory.checkForRsaPassword(form.getUsername(), form.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            dingLogger.error(e.getMessage());
            return ApiResponse.error("用户名或密码错误!");
        }

        return bindingAndLogin(user.getId(), form.getExtId(), type);
    }

    private ApiResponse bindingAndLogin(String userId, String dingId, String type) {
        dingLogger.info("start bindingAndLogin param -> type {},userId {},dingId", type, userId, dingId);
        try {
            SysUserBinding binding = new SysUserBinding();
            binding.setUserId(userId);
            binding = sysUserBindingService.get(binding);
            if (binding != null && StringUtils.isNotEmpty(binding.getDingUserId()) && !dingId.equals(binding.getDingUserId())) {
                //已经绑定过其它帐号
                return ApiResponse.error(HttpStatus.CONFLICT.value(), "此钉钉用户已经绑定其他帐号");
            }

            //生成绑定数据
            if (binding == null) {
                binding = new SysUserBinding();
                binding.setUserId(userId);
                binding.setDingUserId(dingId);
                sysUserBindingService.save(binding);
            } else {
                binding.setDingUserId(dingId);
                sysUserBindingService.update(binding);
            }

            //生成token，并保存到数据库
            return sysUserTokenService.createToken(userId, type);
        } catch (CustomException e) {
            dingLogger.error(e.getMessage());
            return ApiResponse.error(e.getMessageStr());
        } catch (Exception e) {
            dingLogger.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    private String getDingAccessToken(String tokenType) throws CustomException {
        dingLogger.info("start getDingAccessToken param -> tokenType {}", tokenType);
        if (accessTokenMap.get(tokenType) != null) {
            return accessTokenMap.get(tokenType);
        }

        /**
         * 钉钉网关gettoken地址 GET https://oapi.dingtalk.com/gettoken?appkey=key&appsecret=secret
         */
        String URL_GET_TOKKEN = "https://oapi.dingtalk.com/gettoken?appkey=%s&appsecret=%s";
        String url = DING_H5_ACCESS_TOKEN.equals(tokenType) ?
                String.format(URL_GET_TOKKEN, DING_H5_APP_KEY, DING_H5_APP_SECRET) :
                String.format(URL_GET_TOKKEN, DING_APP_KEY, DING_APP_SECRET);
        dingLogger.info("DingDing AccessToken request -> {}", url);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);
        } catch (Exception e) {
            e.printStackTrace();
            dingLogger.error("Catch AccessToken error -> errMsg {}", responseEntity);
            throw new CustomException(e);
        }
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            dingLogger.error("Get AccessToken error -> errMsg {}", responseEntity);
            throw new CustomException("钉钉服务异常:" + responseEntity.getStatusCode());
        }

        String body = null;
        try {
            body = new String(responseEntity.getBody().getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new CustomException("钉钉用户服务异常:" + e.getMessage());
        }
        dingLogger.info("DingDing AccessToken response -> {}", body);

        Map wxResult = (Map) JsonUtil.json2Java(body, Map.class);
        String errCode = wxResult.get(DING_ERROR_CODE).toString();
        String errMsg = wxResult.get(DING_ERROR_MSG).toString();
        String accessToken = wxResult.get(ACCESS_TOKEN).toString();
        String expiresIn = wxResult.get(EXPIRES_IN).toString();
        dingLogger.info("getDingAccessToken result -> code:{},errmsg:{},access_token:{},expires_in:{}", errCode, errMsg, accessToken, expiresIn);

        if ("0".equals(errCode)) {
            accessTokenMap.put(tokenType, accessToken);
            return accessToken;
        } else {
            dingLogger.info(" getDingAccessToken error, code {},errMsg {}", errCode, errMsg);
            throw new CustomException(errMsg);
        }
    }

    /**
     * 注意，等保三级要求办公系统必须由三员分配用户，禁止用户自行注册。这个方法要慎用！！！
     *
     * @param dingId
     * @param type
     * @return
     * @throws CustomException
     */
    private User autoCreateUser(String dingId, String type) throws CustomException {
        dingLogger.info("start autoCreateUser");
        String token;
        token = getDingAccessToken(DING_H5_APP.equals(type) ? DING_H5_ACCESS_TOKEN : DING_APP_ACCESS_TOKEN);

        //调Welink拿到user详细信息
        String loginName = "";
        String displayName = "";

//        请求方式：GET（HTTPS）
//        请求地址：https://oapi.dingtalk.com/user/get?access_token=ACCESS_TOKEN&userid=zhangsan
        String URL_GET_USER_INFO = "https://oapi.dingtalk.com/user/get?access_token=" + token + "&userid=" + dingId;
        dingLogger.info("autoCreateUser -> get DingDing user Detail: url -> {}", URL_GET_USER_INFO);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(URL_GET_USER_INFO, HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class);
        } catch (Exception e) {
            e.printStackTrace();
            dingLogger.error("autoCreateUser -> get WeLink user info:  error -> errMsg {}", e.getMessage());
            throw new CustomException("钉钉用户服务异常:" + e.getMessage());
        }
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            dingLogger.error("autoCreateUser -> get WeLink user info:  error -> errMsg {}", responseEntity);
            throw new CustomException("钉钉用户服务异常:" + responseEntity.getStatusCode());
        }

        String body = null;
        try {
            body = new String(responseEntity.getBody().getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new CustomException("钉钉用户服务异常:" + e.getMessage());
        }
        Map<String, Object> dingdingMap = (Map<String, Object>) JsonUtil.json2Java(body, Map.class);
        if (!"0".equals(dingdingMap.get("errcode").toString())) {
            throw new CustomException("WeLink用户服务异常:" + dingdingMap.get("errcode"));
        }

        dingLogger.info("autoCreateUser get WeLink User info response -> {}", dingdingMap.get("name"));
        //需要开通接口权限：通讯录只读，编辑，手机号权限
        if (dingdingMap != null) {
            String mobile = (String) dingdingMap.get("mobile");

            loginName = mobile != null ? mobile.substring(mobile.indexOf('-') + 1) : (String) dingdingMap.get("userid");
            displayName = dingdingMap.get("name") != null ? (String) dingdingMap.get("name") : loginName;
        }
        if (StringUtils.isEmpty(loginName)) {
            loginName = UUID.randomUUID().toString();
            displayName = loginName;
        }

        Unique unique = uniqueService.getOne(new Unique());
        if (unique == null) {
            unique = new Unique();
            unique.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
            unique.setState("0");
            uniqueService.save(unique);
        }

        User user = new User();
        user.setDeptId(AUTO_USER_DEPT);
        user.setExtStr1("secret_type_0");
        user.setWeight(70);
        user.setOrderNo(1);
        user.setLoginName(loginName);
        user.setDisplayName(displayName);
        user.setCode(unique.getUuid());
        user.setStatus("status_0");
        user.setEthnic("ethnic_01");
        user.setWrongCount(0);
        user.setIsCheck("1");
        user.setIsLeader(0);
        user.setIsDriver(0);
        user.setModifyPwdFlag(1);
        user.setSex("");
        user.setKind("");
        user.setIsAdmin(0);
        userService.save(user);

        unique.setState("1");
        uniqueService.update(unique);

        return user;
    }
}
