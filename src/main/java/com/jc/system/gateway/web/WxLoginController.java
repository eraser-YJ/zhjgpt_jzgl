package com.jc.system.gateway.web;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.ResultCode;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.common.service.ICommonService;
import com.jc.system.common.util.Digests;
import com.jc.system.gateway.domain.SysUserBinding;
import com.jc.system.gateway.model.SysLoginForm;
import com.jc.system.gateway.service.ISysUserBindingService;
import com.jc.system.gateway.service.ISysUserTokenService;
import com.jc.system.gateway.utils.ApiResponse;
import com.jc.system.security.check.CheckFactory;
import com.jc.system.security.domain.User;
import com.jc.system.security.exception.UserLockedException;
import com.jc.system.security.exception.UserPasswordException;
import com.jc.system.security.service.ISystemUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 微信生态登录相关
 * @author Administrator
 */
@RestController
public class WxLoginController extends BaseController {
    private static final Logger wxLogger = LoggerFactory.getLogger(WxLoginController.class);

    private static final String WE_WORK_APP = "WE_WORK_APP";
    private static final String WX_MINI_APP = "WX_MINI_APP";
    private static final String WX_PUBLIC = "WX_PUBLIC";

    private static final String WX_APP_CODE_2_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    private static final String WX_H5_CODE_2_SESSION_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    private static final String WE_WORK_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    private static final String WE_WORK_USERID_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=%s&code=%s";
    private static final String WE_WORK_ACCESS_TOKEN = "access_token";
    private static final String WE_WORK_OPEN_ID = "OpenId";
    private static final String WE_WORK_USER_ID = "UserId";
    private static final String WE_CHAT_OPEN_ID = "openid";


    private static final String ERRCODE = "errcode";
    private static final String ERRMSG = "errmsg";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String EXPIRES_IN = "expires_in";
    private static final String CODE_SUCCESS = "0";
    private static final String CODE_TOKEN_EXPIRED = "42001";
    private static final String DEVICE_ID = "DeviceId";
    private static final String UNION_ID = "unionid";
    private static final String SESSION_KEY = "session_key";
    private static final String WX_OPENID = "openid";
    private static final String OPEN_ID = "openId";
    private static final String USER_ID = "userId";


    private ConcurrentHashMap<String, String> miniAppOpenIdMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> weWorkTokenMap = new ConcurrentHashMap<>();


    private RestTemplate restTemplate;
    private String wxToken = "";
    private String wxMiniAppId = "";
    private String wxMiniSecret = "";
    private String wxH5AppId = "";
    private String wxH5Secret = "";

    private String weworkCorpId = "";
    private String weworkAgentId = "";
    private String weworkSecret = "";

    private String AUTO_USER_CREATE;
    private String AUTO_USER_DEPT;

    @Autowired
    private ISystemUserService sysUserService;
    @Autowired
    private ISysUserTokenService sysUserTokenService;
    @Autowired
    ISysUserBindingService sysUserBindingService;
    @Autowired
    ICommonService commonService;
    @Autowired
    ISystemUserService userService;

    public WxLoginController() {
        this.restTemplate = new RestTemplate();
        //初始化单点登录地址
        Properties p = new Properties();
        InputStream isp = this.getClass().getClassLoader().getResourceAsStream("jcap.properties");
        try {
            p.load(isp);
            wxToken = p.getProperty("wechat.token");
            wxMiniAppId = p.getProperty("wechat.app.appId");
            wxMiniSecret = p.getProperty("wechat.app.secret");
            wxH5AppId = p.getProperty("wechat.h5.appId");
            wxH5Secret = p.getProperty("wechat.h5.secret");

            weworkCorpId = p.getProperty("wework.corpid");
            weworkAgentId = p.getProperty("wework.agentid");
            weworkSecret = p.getProperty("wework.secret");
            AUTO_USER_CREATE = p.getProperty("user.auto.create");
            AUTO_USER_DEPT = p.getProperty("user.auto.dept");
        } catch (IOException e) {
            wxLogger.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/gateway/wxCheck", method = RequestMethod.POST, consumes = "application/xml")
    public String wxCheck(String signature, String timestamp, String nonce,
                          String echostr) {
        wxLogger.info("wx valid signature is {},timestamp is {}, nonce is {} .",
                signature, timestamp, nonce);
        if (signature != null && timestamp != null & nonce != null
                && checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        return "signature:" + signature + "; timestamp:" + timestamp
                + "; nonce:" + nonce;
    }

    //==============================微信小程序==============================//
    @ApiOperation(value = "微信小程序用户绑定系统帐号并登录", httpMethod = "POST")
    @PostMapping("/gateway/wxMiniAppAuthLogin")
    public ApiResponse miniAppAuth(@RequestBody SysLoginForm form, HttpServletRequest request) {
        wxLogger.info("start miniAppAuth, param -> form:{}", form);
        return wxAuth(form, WX_MINI_APP, request);
    }

    @ApiOperation(value = "微信小程序用户OpenId登录", httpMethod = "GET")
    @GetMapping("/gateway/wxMiniAppOpenIdLogin")
    public ApiResponse miniAppOpenIdLogin(@ApiParam(required = false, name = "iv", value = "iv") @RequestParam String iv,
                                          @ApiParam(required = false, name = "encryptedData", value = "encryptedData") @RequestParam String encryptedData,
                                          @ApiParam(required = false, name = "openId", value = "openId") @RequestParam String openId,
                                          HttpServletRequest request) {
        wxLogger.info("start miniAppOpenIdLogin, params : iv -> {},openId -> {}", iv, openId);
        Map userInfo = decryptionUserInfo(encryptedData, miniAppOpenIdMap.get(openId), iv);
        if (userInfo == null) {
            return ApiResponse.error("无法解析用户信息");
        }
        String unionId = (String) userInfo.get("unionId");
        wxLogger.info("getUserInfo result -> unionId:{}", unionId);
        return wxIdLogin(unionId, openId, null, WX_MINI_APP, request);
    }

    @ApiOperation(value = "微信小程序用户Code登录", httpMethod = "GET")
    @GetMapping("/gateway/wxMiniAppCodeLogin")
    public ApiResponse miniAppCodeLogin(@ApiParam(required = true, name = "jsCode", value = "jsCode") @RequestParam @NotNull String jsCode,
                                        HttpServletRequest request) {
        wxLogger.info("start wxMiniAppCodeLogin -> jsCode:{}", jsCode);
        String url = String.format(WX_APP_CODE_2_SESSION_URL, wxMiniAppId, wxMiniSecret, jsCode);
        wxLogger.info("wxMiniAppCodeLogin remote url -> {}", url);
        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .build().toUri();
        String response = restTemplate.getForObject(uri, String.class);
        wxLogger.info("wxMiniAppCodeLogin remote response -> {}", response);
        Map wxResult = (Map) JsonUtil.json2Java(response, Map.class);
        String code = wxResult.get(ERRCODE) != null ? wxResult.get(ERRCODE).toString() : "-1";
        String errMsg = wxResult.get(ERRMSG) != null ? wxResult.get(ERRMSG).toString() : "未知错误";
        String unionId = getString(wxResult, UNION_ID);
        String sessionKey = getString(wxResult, SESSION_KEY);
        String openId = getString(wxResult, WX_OPENID);

        wxLogger.info("wxMiniAppCodeLogin result -> code:{},errormsg:{},unionId:{}, session_key:{},openId:{}", code, errMsg, unionId, sessionKey, openId);
        if (StringUtils.isNotEmpty(openId)) {
            return wxIdLogin(unionId, openId, null, WX_MINI_APP, request)
                    .put(OPEN_ID, openId);
        } else {
            wxLogger.error("无法取得微信openId");
            miniAppOpenIdMap.put(openId, sessionKey);

            return ApiResponse.error(HttpStatus.TEMPORARY_REDIRECT.value(), errMsg)
                    .put(OPEN_ID, openId);
        }
    }

    //==============================企业微信==============================//
    @ApiOperation(value = "企业微信用户绑定系统帐号并登录", httpMethod = "POST")
    @PostMapping("/gateway/weWorkAuth")
    public ApiResponse weWorkAuth(@RequestBody SysLoginForm form,
                                  HttpServletRequest request) {
        wxLogger.info("start weWorkAuth, param -> form:{}", form);
        return wxAuth(form, WE_WORK_APP, request);
    }

    @ApiOperation(value = "企业微信用户Code登录", httpMethod = "GET")
    @GetMapping("/gateway/weworkCodeLogin")
    public ApiResponse weworkCodeLogin(@ApiParam(required = true, name = "code", value = "code") @RequestParam @NotNull String code,
                                       HttpServletRequest request) {
        wxLogger.info("start weworkGetUserId -> code:{}", code);
        String url;
        try {
            url = String.format(WE_WORK_USERID_URL, getWeworkAccessToken(), code);
        } catch (CustomException e) {
            return ApiResponse.error(e.getMessage());
        }
        wxLogger.info("weworkCodeLogin GetUserId remote url -> {}", url);
        String response = restTemplate.getForObject(url, String.class);
        wxLogger.info("weworkCodeLogin GetUserId response -> {}", response);
        Map wxResult = (Map) JsonUtil.json2Java(response, Map.class);
        String errCode = wxResult.get(ERRCODE) != null ? wxResult.get(ERRCODE).toString() : "-1";
        String errMsg = wxResult.get(ERRMSG) != null ? wxResult.get(ERRMSG).toString() : "未知";
        String openId = getString(wxResult, WE_WORK_OPEN_ID);
        String userId = getString(wxResult, WE_WORK_USER_ID);
        String deviceId = getString(wxResult, DEVICE_ID);

        wxLogger.info("weworkCodeLogin GetUserId result -> code:{},errmsg:{},openId:{},userId:{},deviceId:{}", errCode, errMsg, openId, userId, deviceId);
        if (CODE_TOKEN_EXPIRED.equals(errCode)) {
            //access token has expired , need refresh
            weWorkTokenMap.remove(WE_WORK_ACCESS_TOKEN);
            return weworkCodeLogin(code, request);
        } else if (!CODE_SUCCESS.equals(errCode)) {
            return ApiResponse.error(errMsg).put(OPEN_ID, openId).put(USER_ID, userId);
        }
        wxLogger.info("finished weChatCodeLogin : unionId -> {},\nopenId -> {},\nuserId -> {}", "", openId, userId);
        return wxIdLogin(null, openId, userId, WE_WORK_APP, request)
                .put(OPEN_ID, openId).put(USER_ID, userId);
    }

    //==============================微信====================================//
    @ApiOperation(value = "微信用户Code登录", httpMethod = "GET")
    @GetMapping("/gateway/weChatCodeLogin")
    public ApiResponse weChatCodeLogin(@ApiParam(required = true, name = "code", value = "code") @RequestParam @NotNull String code,
                                       HttpServletRequest request) {
        wxLogger.info("start weChatCodeLogin -> code:{}", code);
        String url;

        url = String.format(WX_H5_CODE_2_SESSION_URL, wxH5AppId, wxH5Secret, code);

        wxLogger.info("wechat GetOpenId remote url -> {}", url);
        String response = restTemplate.getForObject(url, String.class);
        wxLogger.info("wechat GetOpenId response -> {}", response);
        Map wxResult = (Map) JsonUtil.json2Java(response, Map.class);
        String openId = getString(wxResult, WE_CHAT_OPEN_ID);

        wxLogger.info("wechat GetOpenId result -> openId:{}", openId);
        if (StringUtils.isEmpty(openId)) {
            return ApiResponse.error("无法获取微信信息").put(OPEN_ID, openId);
        }

        return wxIdLogin(null, openId, null, WX_PUBLIC, request)
                .put(OPEN_ID, openId);
    }

    @ApiOperation(value = "微信用户绑定系统帐号并登录", httpMethod = "POST")
    @PostMapping("/gateway/weChatAuth")
    public ApiResponse weChatAuth(@RequestBody SysLoginForm form, HttpServletRequest request) {
        wxLogger.info("start weChatAuth, param -> form:{}", form);
        return wxAuth(form, WX_PUBLIC, request);
    }

    private ApiResponse wxIdLogin(String unionId,
                                  @NotNull String openId,
                                  String wxUserId,
                                  String type,
                                  HttpServletRequest request) {
        wxLogger.info("start wxLogin, param -> type:{}, unionId:{},openId:{},userId:{}", type, unionId, openId, wxUserId);
        if (StringUtil.isEmpty(openId) && StringUtils.isEmpty(wxUserId)) {
            return ApiResponse.error("登录参数不正确");
        }

        SysUserBinding binding = new SysUserBinding();
        switch (type) {
            case WX_MINI_APP:
                binding.setWxAppOpenId(openId);
                break;
            case WX_PUBLIC:
                binding.setWeChatOpenId(openId);
                break;
            case WE_WORK_APP:
                if (StringUtils.isNotEmpty(wxUserId)) {
                    binding.setWeWorkUserId(wxUserId);
                } else {
                    binding.setWeWorkOpenId(openId);
                }
                break;
            default:
                binding.setWxAppOpenId(openId);
        }

        try {
            binding = sysUserBindingService.get(binding);
            if (binding != null && !StringUtil.isEmpty(binding.getUserId())) {
                //生成token，并保存到数据库
                ApiResponse r = sysUserTokenService.createToken(binding.getUserId(), type);
                wxLogger.info("wxlogin success! response -> {}", r.get("token"));
                User user = userService.getUser(binding.getUserId());
                if (user != null) {
                    r.put(USER_ID, user.getId());
                    r.put("openId", openId);
                    r.put("wxUserId", wxUserId);
                }
                return r;
            } else {
                wxLogger.error("wxlogin failure -> 用户未绑定微信账号");
                return ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "账号未绑定")
                        .put("openId", openId)
                        .put("wxUserId", wxUserId);

            }
        } catch (Exception e) {
            e.printStackTrace();
            wxLogger.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }

    }

    private boolean checkSignature(String signature, String timestamp,
                                   String nonce) {
        TreeSet<String> sortSet = new TreeSet<String>();
        sortSet.add(wxToken);
        sortSet.add(timestamp);
        sortSet.add(nonce);

        StringBuffer sb = new StringBuffer();
        sb.append(sortSet.pollFirst());
        sb.append(sortSet.pollFirst());
        sb.append(sortSet.pollFirst());

        byte[] result = Digests.sha1(sb.toString().getBytes());
        StringBuffer tmpStr = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            tmpStr.append(Integer.toString((result[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        if (tmpStr.toString().equals(signature)) {
            return true;
        }

        return false;
    }

    private String getWeworkAccessToken() throws CustomException {
        wxLogger.info("start getWeworkAccessToken ");
        if (weWorkTokenMap.get(WE_WORK_ACCESS_TOKEN) != null) {
            return weWorkTokenMap.get(WE_WORK_ACCESS_TOKEN);
        }

        String url = String.format(WE_WORK_TOKEN_URL, weworkCorpId, weworkSecret);
        wxLogger.info("Get wework Access Token remote url -> {}", url);
        String response = restTemplate.getForObject(url, String.class);
        wxLogger.info("Get wework Access Token response -> {}", response);

        Map wxResult = (Map) JsonUtil.json2Java(response, Map.class);
        String errCode = getString(wxResult, ERRCODE);
        String errMsg = getString(wxResult, ERRMSG);
        String accessToken = getString(wxResult, ACCESS_TOKEN);
        String expiresIn = getString(wxResult, EXPIRES_IN);
        wxLogger.info("Get wework Access Token result -> code:{},errmsg:{},access_token:{},expires_in:{}", errCode, errMsg, accessToken, expiresIn);

        if (CODE_SUCCESS.equals(errCode)) {
            weWorkTokenMap.put(WE_WORK_ACCESS_TOKEN, accessToken);
            return accessToken;
        } else {
            wxLogger.info("getWeworkAccessToken failed ,code ->{}, message -> {}", errCode, errMsg);
            throw new CustomException(errMsg);
        }

    }

    /**
     * 小程序解密用户数据
     *
     * @param encryptedData 密文
     * @param sessionKey    会话密钥
     * @param iv            iv
     * @return 解密的用户信息
     */
    private Map decryptionUserInfo(String encryptedData, String sessionKey, String iv) {
        wxLogger.info("decrypt wx mini app userinfo => encryptedData:{},iv:{}", encryptedData, iv);
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);

        try {
            // 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            wxLogger.info("decrypted => ");
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                wxLogger.info(result);
                return (Map) JsonUtil.json2Java(result, Map.class);
            }
        } catch (Exception e) {
            wxLogger.error(e.getMessage());
        }
        return null;
    }


    private ApiResponse bindingAndLogin(String userId,
                                        String extId,
                                        String extId2,
                                        String type,
                                        HttpServletRequest request) {

        try {
            SysUserBinding binding = new SysUserBinding();
            binding.setUserId(userId);
            binding = sysUserBindingService.get(binding);
            if (binding != null) {
                boolean isBindingOther = false;
                switch (type) {
                    case WX_MINI_APP:
                        isBindingOther = StringUtils.isNotEmpty(binding.getWxAppOpenId())
                                && !extId.equals(binding.getWxAppOpenId());
                        break;
                    case WX_PUBLIC:
                        isBindingOther = StringUtils.isNotEmpty(binding.getWeChatOpenId())
                                && !extId.equals(binding.getWeChatOpenId());
                        break;
                    case WE_WORK_APP:
                        isBindingOther = StringUtils.isNotEmpty(binding.getWeWorkOpenId())
                                && !extId.equals(binding.getWeWorkOpenId());
                        break;
                    default:

                }
                if (isBindingOther) {
                    //已经绑定过其它帐号
                    wxLogger.info(" bindingAndLogin,the user had binding another one, user -> {} ", userId);
                    return ApiResponse.error(HttpStatus.CONFLICT.value(), "此微信用户已经绑定其他帐号");
                }
            }

            //生成绑定数据
            if (binding == null) {
                binding = new SysUserBinding();
                binding.setUserId(userId);
                binding.setWxUnionId(miniAppOpenIdMap.get(extId));
            }

            switch (type) {
                case WX_MINI_APP:
                    binding.setWxAppOpenId(extId);
                    break;
                case WX_PUBLIC:
                    binding.setWeChatOpenId(extId);
                    break;
                case WE_WORK_APP:
                    binding.setWeWorkUserId(extId2);
                    binding.setWeWorkOpenId(extId);
                    break;
                default:
            }

            if (StringUtils.isEmpty(binding.getId())) {
                sysUserBindingService.save(binding);
            } else {
                sysUserBindingService.update(binding);
            }

            //生成token，并保存到数据库
            ApiResponse response = sysUserTokenService.createToken(userId, type);

            User userInfo = userService.getUser(userId);
            response.put(USER_ID, userInfo.getId());
            response.put("wxUserId", extId2);
            response.put("openId", extId);
            return response;
        } catch (CustomException e) {
            wxLogger.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            wxLogger.error(e.getMessage());
            if (e instanceof UserPasswordException) {
                return ApiResponse.error(ResultCode.USER_LOGIN_ERROR.message());
            } else if (e instanceof UserLockedException) {
                return ApiResponse.error(ResultCode.USER_ACCOUNT_FORBIDDEN.message());
            } else {
                return ApiResponse.error(e.getMessage());
            }
        }
    }

    private String getString(Map map, String key) {
        Object v = map.get(key);
        return v == null ? "" : v.toString();
    }


    private ApiResponse wxAuth(SysLoginForm form, String type, HttpServletRequest request) {
        wxLogger.info("start bindingAndLogin, form -> {}, \n type -> {} ", form, type);
        if (StringUtil.isEmpty(form.getExtId()) && StringUtils.isEmpty(form.getExtId2())) {
            return ApiResponse.error("参数不正确");
        }
        try {
            User user = CheckFactory.checkForRsaPassword(form.getUsername(), form.getPassword());
            return bindingAndLogin(user.getId(), form.getExtId(), form.getExtId2(), type, request);
        } catch (Exception e) {
            e.printStackTrace();
            wxLogger.error(e.getMessage());
            return ApiResponse.error("用户名或密码错误!");
        }
    }
}
