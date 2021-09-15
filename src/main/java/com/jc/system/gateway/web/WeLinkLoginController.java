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
import com.jc.system.security.service.IUniqueService;
import com.jc.system.security.service.IUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;


/**
 * 钉钉生态登录相关
 * @author Administrator
 */
@RestController
public class WeLinkLoginController extends BaseController {
    private static final Logger weLinkLogger = LoggerFactory.getLogger(WeLinkLoginController.class);

    private static final String WE_LINK_H5_APP = "WE_LINK_H5_APP";
    private static final String WE_LINK_WE_APP = "WE_LINK_WE_APP";
    private static final String WE_LINK_CODE_SUCCESS = "0";
    private static final String WE_LINK_CODE_TOKEN_ERROR = "1000";
    private static final String WE_LINK_RESULT_CODE = "code";
    private static final String WE_LINK_RESULT_MESSAGE = "message";
    private static final String USE_ID = "userId";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String EXPIRES_IN = "expires_in";

    private String WE_LINK_APP_ID;
    private String WE_LINK_APP_SECRET;
    private String AUTO_USER_CREATE;
    private String AUTO_USER_DEPT;

    private RestTemplate restTemplate;
    private String accessToken;

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

    public WeLinkLoginController() {
        this.restTemplate = new RestTemplate();
        //初始化单点登录地址
        Properties p = new Properties();
        InputStream isp = this.getClass().getClassLoader().getResourceAsStream("jcap.properties");
        try {
            p.load(isp);
            WE_LINK_APP_ID = p.getProperty("welink.appId");
            WE_LINK_APP_SECRET = p.getProperty("welink.appSecret");
            AUTO_USER_CREATE = p.getProperty("user.auto.create");
            AUTO_USER_DEPT = p.getProperty("user.auto.dept");
        } catch (IOException e) {
            weLinkLogger.error(e.getMessage());
        }
    }

    @ApiOperation(value = "WeLinkH5应用Code登录", httpMethod = "GET")
    @GetMapping("/gateway/weLinkCodeLogin")
    public ApiResponse weLinkCodeLogin(@ApiParam(required = true, name = "code", value = "code") @NotNull @RequestParam String code) {
        weLinkLogger.info("start weLinkCodeLogin, params -> code {}", code);
        return codeLogin(code, WE_LINK_H5_APP);
    }

    @ApiOperation(value = "WeLinkH5应用用户绑定系统帐号并登录", httpMethod = "POST")
    @PostMapping("/gateway/weLinkAuth")
    public ApiResponse weLinkAuth(@RequestBody SysLoginForm form) {
        weLinkLogger.info("start weLinkAuth, param -> code {}", form.toString());
        try {
            User user = CheckFactory.checkForRsaPassword(form.getUsername(), form.getPassword());

            if (user == null) {
                return ApiResponse.error("用户名或密码错误!");
            }
            return bindingAndLogin(user.getId(), form.getExtId());
        } catch (CustomException e) {
            weLinkLogger.info(" weLinkAuth error, message -> {}", e.getMessage());
            weLinkLogger.error(e.getMessage());
            return ApiResponse.error(e.getMessageStr());
        } catch (Exception e) {
            weLinkLogger.info(" weLinkAuth error, message -> {}", e.getMessage());
            weLinkLogger.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    private ApiResponse codeLogin(String code, String type) {
        weLinkLogger.info("start codeLogin, params -> code {},type {}", code, type);
        ResponseEntity<String> response = null;
        try {

            /**
             * https://open-doc.welink.huaweicloud.com/docs/serverapi/contact/getidbycode.html
             *
             * 获取用户userId的接口URL GET
             * 请求地址： https://open.welink.huaweicloud.com/api/auth/v1/userid
             * 设置请求头：x-wlk-Authorization: access_token
             * 返回：
             * {
             *     "code":"0",
             *     "message":"ok",
             *     "userId": "zhangsan@welink"
             * }
             *
             * 请求方式： GET (HTTPS)
             * 请求地址： https://open.welink.huaweicloud.com/api/auth/v2/userid
             * 认证方式： access_token，请参考获取access_token
             * 获取此接口的access_token时，参数不需要传tenantId
             * 请求头部：
             * x-wlk-Authorization: access_token
             * 参数：
             * code	免登授权码
             * 示例：curl  -H "x-wlk-Authorization: access_token" https://open.welink.huaweicloud.com/api/auth/v2/userid?code=xxx
             * 返回
             * {
             *     "code":"0",
             *     "message":"ok",
             *     "userId": "zhangsan@welink",
             *     "tenantId": "AXSCC123ND2DESC..."
             * }
             */
            String URL_GET_USER_INFO = "https://open.welink.huaweicloud.com/api/auth/v2/userid?code=" + code;
            weLinkLogger.info("getWeLinkUserId remote url -> {}", URL_GET_USER_INFO);

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-wlk-Authorization", getWeLinkAccessToken());
            HttpEntity<String> request =
                    new HttpEntity<String>(headers);
            response = restTemplate.exchange(URI.create(URL_GET_USER_INFO), HttpMethod.GET, request, String.class);
        } catch (Exception e) {
            accessToken = null;
            e.printStackTrace();
            weLinkLogger.error(" weLink  error, params -> errMsg {}", e.getMessage());
            return ApiResponse.error("WeLink服务异常");
        }

        if (response.getStatusCode() != HttpStatus.OK) {
            accessToken = null;
            weLinkLogger.error("autoCreateUser -> get WeLink user info:  error -> errMsg {}", response);
            return ApiResponse.error("WeLink服务异常");
        }
        weLinkLogger.info("getWeLinkUserId response -> {}", response);


        Map wxResult = (Map) JsonUtil.json2Java(response.getBody(), Map.class);
        String errCode = wxResult.get(WE_LINK_RESULT_CODE) != null ? wxResult.get(WE_LINK_RESULT_CODE).toString() : "-1";
        String errMsg = wxResult.get(WE_LINK_RESULT_MESSAGE) != null ? wxResult.get(WE_LINK_RESULT_MESSAGE).toString() : "未知";

        if (WE_LINK_CODE_SUCCESS.equals(errCode)) {
            String userId = wxResult.get(USE_ID) != null ? wxResult.get(USE_ID).toString() : "";
            if (StringUtils.isNotEmpty(userId)) {
                return weLinkIdLogin(userId, type).put(USE_ID, userId);
            }
        } else if (WE_LINK_CODE_TOKEN_ERROR.equals(errCode)) {
            //access token has expired , need refresh
            accessToken = null;
            return codeLogin(code, type);
        } else {
            return ApiResponse.error(errMsg);
        }
        weLinkLogger.info(" codeLogin error, params -> errCode {},errMsg {}", errCode, errMsg);
        return ApiResponse.error("无法获取WeLink用户");
    }

    private ApiResponse weLinkIdLogin(@NotNull String weLinkId,
                                      @NotNull String clientType) {
        weLinkLogger.info("start WeLinkLogin, param -> userId:{},clientType:{}", weLinkId, clientType);
        if (StringUtil.isEmpty(weLinkId)) {
            return ApiResponse.error("登录参数不正确");
        }

        SysUserBinding binding = new SysUserBinding();
        binding.setWeLinkUserId(weLinkId);

        try {
            binding = sysUserBindingService.get(binding);
            if (binding != null && !StringUtil.isEmpty(binding.getUserId())) {
                //如果已经绑定过帐号，生成token，并保存到数据库
                ApiResponse r = sysUserTokenService.createToken(binding.getUserId(), clientType);
                weLinkLogger.info("weLinkLogin success! response -> {}", r.get("token"));
                r.put("weLinkUserId",weLinkId);
                return r;
                //TODO 注意，等保三级要求办公系统必须由三员分配用户，禁止用户自行注册。这个方法要慎用！！！
//            } else if ("yes".equals(AUTO_USER_CREATE)) {
//                //如果没绑定过，并且配置为自动生成帐号，就new一个用户，并绑定
//                weLinkLogger.info("weLinkLogin not binding , auto create user! ");
//                User user = autoCreateUser(weLinkId);
//                return bindingAndLogin(user.getId(), weLinkId);
            } else {
                //如果没绑定过，并且配置不是自动生成，通知客户登录绑定
                weLinkLogger.info("weLinkLogin failure! -> 用户未绑定WeLink账号");
                return ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "账号未绑定").put("weLinkUserId",weLinkId);
            }
        } catch (Exception e) {
            weLinkLogger.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    private ApiResponse bindingAndLogin(String userId, String extId) {
        try {
            //check sys_user_binding
            SysUserBinding binding = new SysUserBinding();
            binding.setUserId(userId);
            binding = sysUserBindingService.get(binding);
            if (binding != null
                    && StringUtils.isNotEmpty(binding.getWeLinkUserId())
                    && !extId.equals(binding.getWeLinkUserId())) {
                //已经绑定过其它帐号
                return ApiResponse.error(HttpStatus.CONFLICT.value(), "此用户已经绑定其他帐号");
            }

            //生成绑定数据
            if (binding == null) {
                binding = new SysUserBinding();
                binding.setUserId(userId);
                binding.setWeLinkUserId(extId);
                sysUserBindingService.save(binding);
            } else {
                binding.setWeLinkUserId(extId);
                sysUserBindingService.update(binding);
            }

            //生成token，并保存到数据库
            return sysUserTokenService.createToken(userId, WE_LINK_H5_APP);
        } catch (CustomException e) {
            weLinkLogger.info(" weLinkAuth error, message -> {}", e.getMessage());
            weLinkLogger.error(e.getMessage());
            return ApiResponse.error(e.getMessageStr());
        } catch (Exception e) {
            weLinkLogger.info(" weLinkAuth error, message -> {}", e.getMessage());
            weLinkLogger.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    private String getWeLinkAccessToken() throws CustomException {
        weLinkLogger.info("start getWeLinkAccessToken");
        if (accessToken != null) {
            return accessToken;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        /**
         * 文档地址
         * https://open-doc.welink.huaweicloud.com/docs/serverapi/authorization/token_internal.html?type=internal
         * 请求参数：
         * 这是文档上原来写的参数表，今天发现，它变了 --2020.02.23
         * {
         *   "type": "u",
         *   "client_id": "appId",
         *   "client_secret": "appSecret",
         *   "code": "code",
         *   "state": "1"
         * }
         * 变成了如下参数，线上文档都改了，上哪讲理去，要不是我这里有记录，我还以为我提前阿尔茨海默了呢。
         * {
         *   "client_id": "appId",
         *   "client_secret": "appSecret"
         * }
         *
         * Content-Type: application/json
         * getToken地址 POST https://open.welink.huaweicloud.com/api/auth/v2/tickets
         */
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("client_id", WE_LINK_APP_ID);
        bodyMap.put("client_secret", WE_LINK_APP_SECRET);
        bodyMap.put("type", "u");
        HttpEntity<String> request =
                new HttpEntity<String>(JsonUtil.java2Json(bodyMap), headers);

        String URL_GET_TOKKEN = "https://open.welink.huaweicloud.com/api/auth/v2/tickets";
        weLinkLogger.info("WeLink AccessToken request -> {}", URL_GET_TOKKEN);
//        String response = restTemplate.postForObject(URL_GET_TOKKEN, request, String.class);
        ResponseEntity<String> response = restTemplate.exchange(URL_GET_TOKKEN, HttpMethod.POST, request, String.class);
        weLinkLogger.info("WeLink AccessToken response -> {}", response);


        if (response.getStatusCode() != HttpStatus.OK) {
            accessToken = null;
            weLinkLogger.error("autoCreateUser -> get WeLink user info:  error -> errMsg {}", response.getBody());
            throw new CustomException("WeLink服务异常");
        }

        /**
         * 返回值
         * {
         *   "code": "0",
         *   "message": "ok",
         *   "access_token": "5e2584ad-c6ca-4cf9-8513-8216e4759911",
         *   "expires_in": 7200
         * }
         */
        Map<String, Object> weLinkResult = (Map) JsonUtil.json2Java(response.getBody(), Map.class);
        String errCode = weLinkResult.get(WE_LINK_RESULT_CODE).toString();
        String errMsg = weLinkResult.get(WE_LINK_RESULT_MESSAGE).toString();

        if (WE_LINK_CODE_SUCCESS.equals(errCode)) {
            String accessToken = weLinkResult.get(ACCESS_TOKEN).toString();
            String expiresIn = weLinkResult.get(EXPIRES_IN).toString();
            weLinkLogger.info("getWeLinkAccessToken result -> code:{},message:{},access_token:{},expires_in:{}", errCode, errMsg, accessToken, expiresIn);
            this.accessToken = accessToken;
            return accessToken;
        } else {
            weLinkLogger.info("getWeLinkAccessToken error, errCode {}, errMsg {}", errCode, errMsg);
            throw new CustomException(errMsg);
        }
    }

    private User autoCreateUser(String weLinkId) throws CustomException {
        weLinkLogger.info("start autoCreateUser");
        //调Welink拿到user详细信息
        String loginName = "";
        String displayName = "";

        /**
         * 文档地址：https://open-doc.welink.huaweicloud.com/docs/serverapi/contact/user_detail.html?v=1578129795&type=internal
         *
         * 请求方式： GET (HTTPS)
         * 请求地址： https://open.welink.huaweicloud.com/api/contact/v1/users
         * 认证方式： access_token，请参考获取access_token
         * 请求头部：x-wlk-Authorization: access_token
         *
         * curl  -H "x-wlk-Authorization: access_token" https://open.welink.huaweicloud.com/api/contact/v1/users?userId=zhangshan2@cloudlink
         **/

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-wlk-Authorization", getWeLinkAccessToken());
        HttpEntity<String> request =
                new HttpEntity<String>(headers);

        String URL_GET_USER_INFO = "https://open.welink.huaweicloud.com/api/contact/v1/users?userId=" + weLinkId;
        weLinkLogger.info("autoCreateUser -> get WeLink user info: url -> {}", URL_GET_USER_INFO);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(URI.create(URL_GET_USER_INFO), HttpMethod.GET, request, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            accessToken = null;
            weLinkLogger.error("autoCreateUser -> get WeLink user info:  error -> errMsg {}", e.getMessage());
            throw new CustomException("WeLink用户服务异常");
        }
        if (response.getStatusCode() != HttpStatus.OK) {
            weLinkLogger.error("autoCreateUser -> get WeLink user info:  error -> errMsg {}", response);
            throw new CustomException("WeLink用户服务异常:" + response.getStatusCode());
        }
        Map<String, String> weLinkUser = (Map<String, String>) JsonUtil.json2Java(response.getBody(), Map.class);
        if (!WE_LINK_CODE_SUCCESS.equals(weLinkUser.get(WE_LINK_RESULT_CODE))) {
            throw new CustomException("WeLink用户服务异常:" + response.getStatusCode());
        }

        weLinkLogger.info("autoCreateUser get WeLink User info response -> {}", response);
        if (weLinkUser != null) {
            String mobile = weLinkUser.get("mobileNumber");

            loginName = mobile != null ? mobile.substring(mobile.indexOf('-') + 1) : weLinkUser.get("userId");
            displayName = weLinkUser.get("userNameCn") != null ? weLinkUser.get("userNameCn") : weLinkUser.get("userNameEn");
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
