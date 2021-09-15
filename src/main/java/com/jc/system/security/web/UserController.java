package com.jc.system.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.crypto.utils.SM2Utils;
import com.jc.excel.ExcelUtils;
import com.jc.excel.exceptions.Excel4jReadException;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.common.service.ICommonService;
import com.jc.system.common.util.BeanUtil;
import com.jc.system.event.*;
import com.jc.system.gateway.domain.SysUserToken;
import com.jc.system.gateway.service.ISysUserTokenService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.*;
import com.jc.system.security.domain.validator.UserValidator;
import com.jc.system.security.service.IUniqueService;
import com.jc.system.security.service.IUserExtService;
import com.jc.system.security.service.IUserService;
import com.jc.system.security.util.UserUtils;
import com.jc.system.sys.service.IPinUserService;
import com.jc.system.util.SettingUtils;
import com.jc.system.util.menuUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 * @author Administrator
 * @date 2020-06-30
 */
@Controller
@RequestMapping(value = "/sys/user")
public class UserController extends BaseController {
	protected transient final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private IUserService userService;
	@Autowired
	private IUserExtService userExtService;
	@Autowired
	private IUniqueService uniqueService;
	@Autowired
	private IPinUserService pinUserService;
	
	@Autowired
    private ISysUserTokenService sysUserTokenService;

	@Autowired
	ICommonService commonService;

	@InitBinder("user")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new UserValidator());
	}

	public UserController() {
	}

	private UserValidator uv = new UserValidator();

	/**
	 * 分页查询方法
	 * @param user
	 * @param page
	 * @param request
	 * @return
	 */
	@RequiresPermissions("user:select")
	@RequestMapping(value = "manageList.action")
	@ResponseBody
	@ActionLog(operateModelNm="用户设置",operateFuncNm="manageList",operateDescribe="对用户设置进行查询")
	public PageManager manageList(User user,final PageManager page, HttpServletRequest request) {
		if(StringUtils.isEmpty(user.getOrderBy())) {
			user.addOrderByField("t.ORDER_NO");
		}
		if(user.getStatus() != null && user.getStatus().equals(GlobalContext.USER_STATUS_3)){
			user.setDeleteFlag(1);
		} else {
			user.setDeleteFlag(null);
		}
		return userService.query(user, page);
	}

	@RequestMapping(value = "userAllList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager userAllList(User user, final PageManager page, HttpServletRequest request) {
		user.addOrderByField("t.ORDER_NO");
		user.setDeleteFlag(0);
		return userService.queryAllUsersForOrder(user, page);
	}

	/**
	 * 跳转方法
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "manageAll.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="用户设置",operateFuncNm="manageAll",operateDescribe="对用户设置进行访问")
	public String manageAll(Model model, HttpServletRequest request) throws Exception {
		User userInfo = SystemSecurityUtils.getUser();
		if(userInfo != null){
			if(userInfo.getIsAdmin() == 1){
				return "sys/user/userAll";
			} else {
				return "error/unauthorized";
			}
		}
		return "error/unauthorized";
	}

	/**
	 * 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author
	 * @version 2014-03-18
	 */
	@RequiresPermissions("user:select")
	@RequestMapping(value = "manage.action",method=RequestMethod.GET)
	public String manage(Model model, HttpServletRequest request) throws Exception {
		User userInfo = SystemSecurityUtils.getUser();
		if(userInfo != null){
			if(userInfo.getIsAdmin() == 1){
				menuUtil.saveMenuID("/sys/user/manage.action",request);
				return "sys/user/user";
			} else {
				return "error/unauthorized";
			}
		}
		return "error/unauthorized";
	}

	@RequestMapping(value = "userEdit.action",method=RequestMethod.GET)
	public String userEdit(Model model, HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>(3);
		String token = getToken(request);
		List<Role> roleList = new ArrayList<>();
		map.put(GlobalContext.SESSION_TOKEN, token);
		map.put("roleList", roleList);
		map.put("isSysAdmin", SystemSecurityUtils.getUser().getIsSystemAdmin());
		map.put("isSysManager", SystemSecurityUtils.getUser().getIsSystemManager());
		model.addAttribute("data", map);
		return "sys/user/userEdit";
	}

	/**
	 * 删除方法
	 * @param user
	 * @param ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:delete")
	@RequestMapping(value = "deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="用户设置",operateFuncNm="deleteByIds",operateDescribe="对用户设置进行删除")
	public Map<String, Object> deleteByIds(User user, String ids, HttpServletRequest request) throws Exception {
		Map<String, Object> map = SystemSecurityUtils.loginState(ids);
		String onLine = (String) map.get("onLine");
		String deleteIds = (String) map.get("noOnLine");

		Map<String, Object> resultMap = new HashMap<>(2);
		if(deleteIds.length() > 0){
			String[] delIds = deleteIds.split(",");
			user.setPrimaryKeys(delIds);
			SpringContextHolder.getApplicationContext().publishEvent(new UserDeleteEvent(user));
			if(userService.delete(user) == 1){
				if(onLine.length() > 0){
					resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
					resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_078") + onLine + MessageUtils.getMessage("JC_SYS_079"));
				} else {
					resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
					resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
					SysUserToken arg0 = new SysUserToken();
					arg0.setPrimaryKeys(delIds);
					sysUserTokenService.delete(arg0 );
				}
				return resultMap;
			}
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_078") +onLine+ MessageUtils.getMessage("JC_SYS_080"));
			return resultMap;
		}
		return null;
	}

	/**
	 * 保存方法
	 * @param userMap
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:add")
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="用户设置",operateFuncNm="save",operateDescribe="对用户设置进行添加")
	public Map<String, Object> save(@RequestBody Map<String, Object> userMap, BindingResult result, HttpServletRequest request) throws Exception {
		User user = BeanUtil.map2Object(userMap, User.class);
		uv.validate(user, result);
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request, (String)getMapValue(userMap, GlobalContext.SESSION_TOKEN));
		if (resultMap.size() > 0) {
			return resultMap;
		}
		Unique unique = uniqueService.getOne(new Unique());
		if (unique == null) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "无可用的用户编号");
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
			return resultMap;
		} else {
			user.setCode(unique.getUuid());
		}
		if (userService.save(user) == 1) {
			unique.setState("1");
			uniqueService.update(unique);
			pinUserService.infoLoading();
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
			SpringContextHolder.getApplicationContext().publishEvent(new UserAddEvent(user));
		}
		return resultMap;
	}

	/**
	 * 初始化密码
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:initPwd")
	@RequestMapping(value = "initPassword.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="用户设置",operateFuncNm="initPassword",operateDescribe="对用户设置进行密码初始化")
	public Map<String, Object> initPassword(User user, HttpServletRequest request) throws Exception {
		if(userService.initPassword(user) == 1){
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_081"));
			user = userService.getUser(user.getId());
			SpringContextHolder.getApplicationContext().publishEvent(new UserSetPasswordEvent(user));
			return resultMap;
		}
		return null;
	}

	/**
	 * 验证用户名是否存在
	 * @param user
	 * @param loginNameOld
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "checkLoginName.action",method=RequestMethod.GET)
	@ResponseBody
	public String checkLoginName(User user, String loginNameOld) throws Exception {
		if(StringUtils.isEmpty(loginNameOld)){
			user.setDeleteFlag(null);
			if(userService.get(user) == null){
				return "true";
			} else {
				return "false";
			}
		} else {
			return "true";
		}
	}

	/**
	 * 验证手机号是否存在
	 * @param user
	 * @param mobileOld
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "checkMobile.action",method=RequestMethod.GET)
	@ResponseBody
	public String checkMobile(User user, String mobileOld) throws Exception {
		if("0".equals(user.getId())){
			user.setId(null);
		}
		if(userService.checkMobile(user) == null){
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * 修改方法
	 * @param userMap
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:update")
	@RequestMapping(value = "update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="用户设置",operateFuncNm="update",operateDescribe="对用户设置进行修改")
	public Map<String, Object> update(@RequestBody Map<String, Object> userMap, BindingResult result, HttpServletRequest request) throws Exception {
		User user = BeanUtil.map2Object(userMap, User.class);
		uv.validate(user, result);
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		String statusOld = (String) getMapValue(userMap, "statusOld");
		if(!statusOld.equals(user.getStatus())){
			user.setWrongCount(0);
		}
		User oldUser = userService.getUser(user.getId());
		Integer flag = userService.updateUser(user);
		if (flag == 1) {
			pinUserService.infoLoading();
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
			SpringContextHolder.getApplicationContext().publishEvent(new UserUpdateEvent(user));
			if(!oldUser.getDeptId().equals(user.getDeptId())){
				SpringContextHolder.getApplicationContext().publishEvent(new UserDeptUnBindEvent(oldUser));
				SpringContextHolder.getApplicationContext().publishEvent(new UserDeptBindEvent(user));
			}
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	/**
	 * 获取单条记录方法
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:update")
	@RequestMapping(value = "get.action",method=RequestMethod.GET)
	@ResponseBody
	public User get(User user, HttpServletRequest request) throws Exception {
		user.setDeleteFlag(null);
		return userService.getUser(user);
	}

	@RequestMapping(value = "userInfo.action",method=RequestMethod.GET)
	public String userInfo(Model model, HttpServletRequest request) throws Exception {
		return "sys/user/userInfo";
	}

	/**
	 * 个人信息获取信息方法
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getUserInfo.action",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getUserInfo(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put(GlobalContext.SESSION_TOKEN, getToken(request));
		User userInfo = SystemSecurityUtils.getUser();
		if(userInfo != null){
			User user = new User();
			user.setId(userInfo.getId());
			user.setDeleteFlag(0);
			user = userService.get(user);
			map.put("user", user);
			map.put("filePath",GlobalContext.getProperty("FILE_PATH"));
		}
		return map;
	}

	/**
	 * 修改个人信息
	 * @param user
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "userInfoUpdate.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="个人信息",operateFuncNm="userInfoUpdate",operateDescribe="对个人信息进行修改")
	public Map<String,Object> userInfoUpdate(User user,BindingResult result,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap;
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		int state = userService.updateUserInfo(user);
		if(state == 1){
			User currentUser = SystemSecurityUtils.getUser();
			if(currentUser.getId().equals(user.getId())){
				Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
				String photo = user.getPhoto();
				if(!GlobalContext.isSysCenter() &&  "true".equals(GlobalContext.getProperty("cas.start")) && !StringUtil.isEmpty(GlobalContext.getProperty("api.dataServer"))){
					if(!StringUtil.isEmpty(photo)){
						photo = GlobalContext.getProperty("api.dataServer") + "/" + photo;
					}
				}
				principal.setPhoto(photo);
				currentUser.setPhoto(principal.getPhoto());
				currentUser.setDisplayName(user.getDisplayName());
			}
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
			User u = new User();
			u.setId(user.getId());
			u = userService.get(u);
			resultMap.put("modifyDate", DateUtils.formatDate(u.getModifyDate(), "yyyy-MM-dd HH:mm:ss"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_032"));
		}
		resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		return resultMap;
	}

	@RequestMapping(value = "userPwd.action",method=RequestMethod.GET)
	public String userPwd(Model model ,HttpServletRequest request) throws Exception {
		model.addAttribute("pwdType",SettingUtils.getSetting("pwdType"));
		return "sys/user/userPwd";
	}

	@RequestMapping(value = "checkUserPwd.action",method=RequestMethod.GET)
	@ResponseBody
	public Boolean checkUserPwd(User user, HttpServletRequest request) throws Exception{
		User userInfo = SystemSecurityUtils.getUser();
		User u = new User();
		u.setId(userInfo.getId());
		u = userService.get(u);
		if(SystemSecurityUtils.validatePassword(user.getPassword(), u.getPassword(),u.getKeyCode())){
			return true;
		} else {
			return false;
		}
	}

	@RequestMapping(value = "userPwdModify.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="用户密码",operateFuncNm="userPwdModify",operateDescribe="对用户密码进行修改")
	public Map<String,Object> userPwdModify(User user,BindingResult result,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		User userInfo = SystemSecurityUtils.getUser();
		if(userInfo != null){
			User u = new User();
			u.setId(userInfo.getId());
			u = userService.get(u);
			if(SystemSecurityUtils.validatePassword(user.getPassword(), u.getPassword(),u.getKeyCode())){
				SM2Utils.generateKeyPair();
				User u1 = new User();
				u1.setId(userInfo.getId());
				u1.setPassword(SystemSecurityUtils.entryptPassword(user.getNewPassword(),SM2Utils.getPubKey()));
				u1.setModifyPwdFlag(1);
				u1.setExtDate1(DateUtils.getSysDate());
				u1.setKeyCode(SM2Utils.getPriKey());
				if(userService.update2(u1) == 1){
					resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
					resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_042"));
					SystemSecurityUtils.setFirstLoginState();
					u.setPassword(user.getNewPassword());
					SpringContextHolder.getApplicationContext().publishEvent(new UserSetPasswordEvent(u));
				}
			} else {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_082"));
			}
		}
		resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		return resultMap;
	}

	@RequestMapping(value = "forgetPwd.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> forgetPwd(User user,BindingResult result,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		User u = new User();
		u.setLoginName(user.getLoginName());
		u = userService.get(u);
		if(u == null){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_083"));
		} else {
			if(StringUtils.isNotEmpty(u.getQuestion()) && StringUtils.isNotEmpty(u.getAnswer())){
				if(u.getQuestion().trim().equals(user.getQuestion().trim()) && u.getAnswer().trim().equals(user.getAnswer().trim())){
					User newUser = new User();
					SM2Utils.generateKeyPair();
					if(!StringUtil.isEmpty(GlobalContext.getProperty("password.default.value"))){
						newUser.setPassword(SystemSecurityUtils.entryptPassword(GlobalContext.getProperty("password.default.value"),SM2Utils.getPubKey()));
					} else {
						newUser.setPassword(SystemSecurityUtils.entryptPassword(GlobalContext.PASSWORD_DEFAULT_VALUE,SM2Utils.getPubKey()));
					}
					newUser.setKeyCode(SM2Utils.getPriKey());
					newUser.setId(u.getId());
					if(userService.update2(newUser) == 1){
						resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
						resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, "密码初始化成功");
					} else {
						resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
						resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_086"));
					}
				} else {
					resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
					resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_087"));
				}
			} else {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_088"));
			}
		}

		return resultMap;
	}

	@RequestMapping(value = "duressPassword.action",method=RequestMethod.GET)
	public String duressPassword(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute(GlobalContext.SESSION_TOKEN, getToken(request));
		model.addAttribute("pwdType",SettingUtils.getSetting("pwdType"));
		return "sys/user/duressPassword";
	}

	@RequestMapping(value = "isFirstLogin.action",method=RequestMethod.GET)
	@ResponseBody
	public boolean isFirstLogin() throws Exception {
		return SystemSecurityUtils.isFirstLogin();
	}

	@RequestMapping(value = "skin.action",method=RequestMethod.GET)
	@ResponseBody
	public String skin(String theme,String color,String font, HttpServletRequest request) throws Exception{
		if(StringUtils.isNotEmpty(theme)){
			Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
			User userInfo = SystemSecurityUtils.getUser();
			User user = new User();
			user.setId(userInfo.getId());
			user.setTheme(theme);
			if(userService.update2(user) == 1){
				principal.setTheme(theme);
			}
			return "true";
		}
		if (StringUtils.isNotEmpty(color)){
			Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
			User userInfo = SystemSecurityUtils.getUser();
			UserExt userExt = new UserExt();
			userExt.setUserId(userInfo.getId());
			userExt.setCssType("color");
			UserExt tempuserExt = userExtService.get(userExt);
			if (tempuserExt == null){
				userExt.setCssValue(color);
				userExtService.save(userExt);
			} else {
				tempuserExt.setCssValue(color);
				userExtService.update(tempuserExt);
			}
			principal.setColor(color);
			return "true";
		}
		if (StringUtils.isNotEmpty(font)){
			Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
			User userInfo = SystemSecurityUtils.getUser();
			UserExt userExt = new UserExt();
			userExt.setUserId(userInfo.getId());
			userExt.setCssType("font");
			UserExt tempuserExt = userExtService.get(userExt);
			if (tempuserExt == null){
				userExt.setCssValue(font);
				userExtService.save(userExt);
			} else {
				tempuserExt.setCssValue(font);
				userExtService.update(tempuserExt);
			}
			principal.setFont(font);
			return "true";
		}
		return "false";
	}

	/**
	 * 修改方法
	 * @param
	 *            user 实体类
	 * @return Integer 更新的数目
	 * @author
	 * @version 2014-03-18
	 */
	@RequestMapping(value = "updateUserOrder.action",method=RequestMethod.POST)
	@ResponseBody
	public Integer updateUserOrder(User user, HttpServletRequest request) throws Exception {
		String userId = request.getParameter("userId");
		String orderNo = request.getParameter("orderNo");
		String oUserId = request.getParameter("oUserId");
		String oOrderNo = request.getParameter("oOrderNo");
		if(StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(orderNo) && StringUtils.isNotEmpty(oUserId) && StringUtils.isNotEmpty(oOrderNo)){
			return userService.updateUserOrder(userId, orderNo, oUserId, oOrderNo);
		} else if(StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(orderNo)){
			user.setId(userId);
			user.setOrderNo(Integer.parseInt(orderNo));
			return userService.update2(user);
		}
		return null;
	}

	/**
	 * 恢复删除用户方法
	 * @param user
	 * @param ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:update")
	@RequestMapping(value = "deleteBackByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="用户设置",operateFuncNm="deleteBackByIds",operateDescribe="对用户设置进行删除数据恢复")
	public Map<String, Object> deleteBackByIds(User user, String ids, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		if(ids.length() > 0){
			user.setPrimaryKeys(ids.split(","));
			user.setStatus("status_0");
			if(userService.deleteBack(user) >= 1){
				resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, "用户恢复成功");
				return resultMap;
			}
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "用户恢复失败");
			return resultMap;
		}
		return null;
	}

	/**
	 * 修改个人信息
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "userVoiceManager.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> userVoiceManager(String status) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		User userInfo = SystemSecurityUtils.getUser();
		if(userInfo != null){
			User u = new User();
			u.setId(userInfo.getId());
			u = userService.get(u);
			u.setExtStr5(status);
			if(userService.update2(u) == 1){
				if("1".equals(status)){
					resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
				} else {
					resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
				}
			} else {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			}
		}
		return resultMap;
	}

	/**
	 * 根据用户id获取直属领导用户数据
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getLeader.action",method=RequestMethod.GET)
	@ResponseBody
	public User getLeader(User user) throws Exception{
		return userService.getLeader(user.getId());
	}

	/**
	 * 导出Excel
	 * @param user
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws NoSuchFieldException
	 */
	@RequestMapping(value = "exportExcel.action",method=RequestMethod.POST)
	public Map<String, Object> exportExcel(User user, final PageManager page, HttpServletRequest request, HttpServletResponse response) throws IOException, NoSuchFieldException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<User> userList = null;
		String isTemplet = request.getParameter("isTemplet");
		try {
			if("true".equals(isTemplet)){
				userList = new ArrayList<User>();
			}else{
				if(StringUtils.isEmpty(user.getOrderBy())) {
					user.addOrderByField("t.ORDER_NO");
				}
				if(user.getStatus() != null && user.getStatus().equals(GlobalContext.USER_STATUS_3)){
					user.setDeleteFlag(1);
				} else {
					user.setDeleteFlag(null);
				}
				userList = userService.queryAll(user);
			}
			ExcelUtils.getInstance().expBean2Excel("用户信息表", userList, User.class, response);
		} catch (CustomException e) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "用户导出失败");
			return resultMap;
		}
		resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
		resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, "用户导出成功");
		return resultMap;

	}

	/**
	 * 导入导出Excel
	 * @param user
	 * @param excel
	 * @param model
	 * @param result
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws ParseException
	 * @throws InstantiationException
	 */
	@RequestMapping(value = "importExcel.action",method=RequestMethod.POST)
	public void importExcel(User user, @RequestParam MultipartFile excel, Model model, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException, InstantiationException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		if (excel.isEmpty()) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "请选择文件后再上传!!");
			response.getWriter().write(mapper.writeValueAsString(resultMap));
			return;
		} else {
			excel.getOriginalFilename();
			List<User> lstBean = null;
			try {
				lstBean = ExcelUtils.getInstance().readExcel2Objects(excel.getInputStream(), User.class);
				int index = 1;
				for(User impUser : lstBean){
					try {
						uv.validate(impUser, result);
						if (result.hasErrors()) {
							resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
							resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "第["+index+"]行导入异常</br>"+result.getAllErrors().get(0).getDefaultMessage());
							response.getWriter().write(mapper.writeValueAsString(resultMap));
							return;
						}
						index++;
					} catch (Exception e) {
						resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
						resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "第["+index+"]行导入异常</br>"+e.getMessage());
						response.getWriter().write(mapper.writeValueAsString(resultMap));
						return;
					}
				}
				index = 1;
				for(User impUser : lstBean){
					try {
						if(impUser.getStatus() == null) {
                            impUser.setStatus("status_0");
                        }
						if(impUser.getIsAdmin() == null) {
                            impUser.setIsAdmin(0);
                        }
						if(impUser.getWeight() == null) {
                            impUser.setWeight(0);
                        }
						if(impUser.getIsDriver() == null) {
                            impUser.setIsDriver(0);
                        }
						if(impUser.getIsLeader() == null) {
                            impUser.setIsLeader(0);
                        }
						if(impUser.getIsCheck() == null) {
                            impUser.setIsCheck("0");
                        }
						userService.save(impUser);
						index++;
					} catch (CustomException e) {
						logger.error(e);
						resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
						resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "第["+index+"]行导入异常</br>"+e.getMessage());
						response.getWriter().write(mapper.writeValueAsString(resultMap));
						return;
					} catch (Exception e) {
						logger.error(e);
						resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
						resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "第["+index+"]行导入异常");
						response.getWriter().write(mapper.writeValueAsString(resultMap));
						return;
					}
				}
			} catch (Excel4jReadException e) {
				logger.error(e);
				resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, e.getMessage());
				response.getWriter().write(mapper.writeValueAsString(resultMap));
				return;
			} catch (Exception e) {
				logger.error(e);
				resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "用户导入失败");
				response.getWriter().write(mapper.writeValueAsString(resultMap));
				return;
			}
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, "用户导入成功");
			response.getWriter().write(mapper.writeValueAsString(resultMap));
		}
	}
}