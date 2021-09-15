package com.jc.system.gateway.web;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jc.system.security.utils.RSASetting;
import com.jc.system.util.SettingUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.RsaUtil;
import com.jc.foundation.web.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "API - LoginApiController", description = "登录相关api接口")
@RestController
public class LoginApiController extends BaseController {

	protected Logger logger = Logger.getLogger(this.getClass());

	/**
	 *
	 * @Title: generateRSAKey
	 * @Description: 生成公钥和私钥
	 * @param
	 * @return
	 * @date 2018年2月5日 下午4:25:05
	 * @author
	 */
	@ApiOperation(value = "获取rsa密钥",httpMethod="POST") 
	@PostMapping("/api/system/rsaKey")
	public Map<String, Object> generateRSAKey(HttpServletRequest request, @ApiParam(required = true, name = "username", value = "用户名称") String username, 
			@ApiParam(required = false, name = "captcha", value = "验证码-(与配置有关，可不传)") String captcha) {
		// 将公钥传到前端
		Map<String, Object> map = new HashMap<>();
		String verifyCodeExpected = (String) request.getSession()
				.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		String valCaptcha = GlobalContext.getProperty("sys.valCaptcha");
		if ("yes".equals(valCaptcha)) {
			if (captcha == null) {
				map.put("valResule", "false");
				map.put("msg","验证码错误");
				return map;
			}
			if (captcha.toLowerCase().equals(verifyCodeExpected.toLowerCase())) {
				map.put("valResule", "true");
			} else {
				map.put("valResule", "false");
				map.put("msg","验证码错误");
				return map;
			}
		} else {
			map.put("valResule", "true");
		}
		try {

			Map rasMap= RSASetting.getRSAModulusAndExponent();
			map.putAll(rasMap);

			return map;
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
		}
		return map;
	}

}
