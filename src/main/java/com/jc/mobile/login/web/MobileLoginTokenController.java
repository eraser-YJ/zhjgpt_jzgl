package com.jc.mobile.login.web;

import com.jc.csmp.common.enums.DepartRootCodeEnum;
import com.jc.foundation.util.ResultCode;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.mobile.basic.web.MobileController;
import com.jc.mobile.login.domain.MobileLoginToken;
import com.jc.mobile.login.domain.validator.MobileLoginTokenValidator;
import com.jc.mobile.login.service.IMobileLoginTokenService;
import com.jc.mobile.util.MobileApiResponse;
import com.jc.system.security.check.CheckFactory;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 移动端登录tokenController
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/mobile/login")
public class MobileLoginTokenController extends MobileController {

	@Autowired
	private IMobileLoginTokenService mobileLoginTokenService;

	@org.springframework.web.bind.annotation.InitBinder("mobileLoginToken")
    public void initBinder(WebDataBinder binder) {
		binder.setValidator(new MobileLoginTokenValidator());
    }

	public MobileLoginTokenController() {
	}

	@RequestMapping(value = "",method=RequestMethod.POST)
	@ResponseBody
	public MobileApiResponse index(@RequestBody Map<String, String> param) {
		String userName = param.get("userName");
		String password = param.get("password");
		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
			return MobileApiResponse.error(ResultCode.PARAM_IS_BLANK);
		}
		try {
			String checkUserName = ",security,audit,manager,admin,";
			if (checkUserName.indexOf("," + userName + ",") > -1) {
				//避免与内置用户冲突
				userName = userName + "_admin";
			}
			User user = CheckFactory.checkForPassword(userName, password);
			MobileLoginToken token = this.mobileLoginTokenService.createToken(user.getId());
			if (token != null) {
				Map<String, Object> resultMap = new HashMap<>(3);
				resultMap.put(ACCESS_TOKEN, token.getAccessToken());
				resultMap.put("expire", token.getExpireTime());
				User sessionUser = UserUtils.getUser(token.getUserId());
				Department dept = DeptCacheUtil.getDeptById(sessionUser.getDeptId());
				if (dept != null) {
					if (dept.getCode().startsWith(DepartRootCodeEnum.COMPANY.getValue())) {
						sessionUser.setPersonCompanyType(DepartRootCodeEnum.COMPANY.toString());
					} else if (dept.getCode().startsWith(DepartRootCodeEnum.GOVERNMENT.getValue())) {
						sessionUser.setPersonCompanyType(DepartRootCodeEnum.GOVERNMENT.toString());
					}
				}
				resultMap.put("sessionUser", sessionUser);
				return MobileApiResponse.ok(resultMap);
			} else {
				return MobileApiResponse.error("未知异常");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return MobileApiResponse.error("用户名或密码错误!");
		}
	}
}
