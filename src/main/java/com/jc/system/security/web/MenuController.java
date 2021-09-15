package com.jc.system.security.web;

import com.jc.csmp.common.enums.SubSystemSignEnum;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.sys.domain.SubUser;
import com.jc.sys.service.ISubMenuService;
import com.jc.sys.service.ISubUserService;
import com.jc.sys.util.SubUserUtil;
import com.jc.system.applog.ActionLog;
import com.jc.system.common.util.Encodes;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.domain.Principal;
import com.jc.system.security.domain.Shortcut;
import com.jc.system.security.domain.User;
import com.jc.system.security.domain.validator.MenuValidator;
import com.jc.system.security.service.IMenuService;
import com.jc.system.security.service.IUserShortcutService;
import com.jc.system.security.util.UserUtils;
import com.jc.system.tab.domain.TabTree;
import com.jc.system.tab.service.ITabTreeService;
import com.jc.system.util.menuUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单控制器
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value="/sys/menu")
public class MenuController extends BaseController {

	@Autowired
	private IMenuService menuService;
	@Autowired
	private IUserShortcutService userShortcutService;
	@Autowired
	private ITabTreeService tabTreeService;
	@Autowired
	private ISubUserService subUserService;
	@Autowired
	private ISubMenuService subMenuService;

	@org.springframework.web.bind.annotation.InitBinder("menu")
    public void initBinder(WebDataBinder binder) {
        	binder.setValidator(new MenuValidator());
    }

	public MenuController() {
	}

	private MenuValidator mv = new MenuValidator();

	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(Menu menu,PageManager page){
		PageManager rePage = menuService.query(menu, page);
		return rePage;
	}

	/**
	* @description 跳转方法（导航栏）
	* @return String 跳转的路径
	* @throws Exception
	*/
	@RequestMapping(value="managenAvigation.action",method=RequestMethod.GET)
	public String managenAvigation(Model model,String id,String actionName) throws Exception{
		Map<String,Object> menuMap = new HashMap<String,Object>();
		if(id != null && !"0".equals(id)) {
			menuMap = menuService.getNavigationMenuById(id);
		} else if(actionName != null && !"".equals(actionName)){
			menuMap = menuService.getNavigationMenuByUrl(actionName);
		}
		model.mergeAttributes(menuMap);
		return "sys/menu/navigationMenu";
	}

	/**
	* @description 锚点应用
	* @return String 跳转的路径
	* @throws Exception
	*/
	@RequestMapping(value="anchorAvigation.action",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> anchorAvigation(String id,String actionName) throws Exception{
		Map<String, Object> map = menuService.getNavigationMenuByUrl(actionName);
		map.put("success", "true");
		return map;
	}

	/**
	* @description 跳转方法（资源维护）
	* @return String 跳转的路径
	* @throws Exception
	*/
	@RequestMapping(value="manageResource.action",method=RequestMethod.GET)
	public String manageResource(HttpServletRequest request) throws Exception{
		User userInfo = SystemSecurityUtils.getUser();
		if(userInfo != null){
			if(userInfo.getIsAdmin() == 1 || userInfo.getIsSystemAdmin()){
				menuUtil.saveMenuID("/sys/menu/manageResource.action",request);
				return "sys/menu/resurceTree";
			} else {
				return "error/unauthorized";
			}
		}
		return "error/unauthorized";
	}

	/**
	* @description 加载数据信息（资源维护）
	* @return String 跳转的路径
	* @throws Exception
	*/
	@RequestMapping(value="manageLoadResource.action",method=RequestMethod.GET)
	public String manageLoadResource(Model model,String id,HttpServletRequest request) throws Exception{
		Menu menuVo = new Menu();
		menuVo.setId(id);
		List<Menu> menuList  = menuService.getResourceInfo(menuVo,"all");
		model.addAttribute("menuList", menuList);
		return "sys/menu/loadResource";
	}

	/**
	* @description 加载数据信息（资源树）
	* @return String 跳转的路径
	* @throws Exception
	*/
	@RequestMapping(value="treeResource.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> treeResource(Model model,String id,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		List<Menu> menuList  = menuService.queryMenuTreeForMove(id);
		map.put("menuList", menuList);
		return map;
	}

	/**
	* @description 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	*/
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	public String manage(Model model,HttpServletRequest requests) throws Exception{
		String subSystemMenuId = requests.getParameter("subSystemMenuId");
		Map<String,Object> menuMap = null;
		Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
		User user = SystemSecurityUtils.getUser();
		//判断是否为一岗多职
		SubUser subUser = new SubUser();
		subUser.setCode(user.getCode());
		subUser.setLoginName(user.getLoginName());
		subUser.setOrderBy("t.ORDER_NO desc");
		List<SubUser> subUserList = subUserService.queryAll(subUser);
		if (subUserList != null && subUserList.size() > 0){
			SubUser reSubUser = new SubUser();
			for (SubUser subUser1 : subUserList){
				if (subUser1.getTheme() != null && "sel".equals(subUser1.getTheme())){
					reSubUser.setId(subUser1.getId());
					reSubUser.setDutyId(subUser1.getDutyId());
					reSubUser.setDeptId(subUser1.getDeptId());
					break;
				}
			}
			if (reSubUser.getId() != null){
				menuMap = subMenuService.desktopMenuForSub(reSubUser.getId(),reSubUser.getDeptId());
				SubUserUtil.setSubUserDept(reSubUser.getId(),reSubUser.getDeptId());
			} else {
				SubUser subUser2 = subUserList.get(0);
				subUserList.get(0).setTheme("sel");
				menuMap = subMenuService.desktopMenuForSub(subUser2.getId(),subUser2.getDeptId());
				SubUserUtil.setSubUserDept(subUser2.getId(),subUser2.getDeptId());
			}
			menuMap.put("subUserList",subUserList);
			model.mergeAttributes(menuMap);
			model.addAttribute("ischildnode", 0);
		} else {
			Menu menuVo = new Menu();
			if(user.getIsSystemAdmin()) {
				menuVo.setUserId("-99");
			} else {
				menuVo.setUserId(user.getId());
			}
			menuVo.setSubSystemMenuId(StringUtil.isEmpty(subSystemMenuId) ? null : subSystemMenuId);
			menuMap = menuService.desktopMenuForQuery(menuVo);
			List<Shortcut> shortcutList = userShortcutService.getShortcutListByUserId(user.getId());
			List<Shortcut> shortcuts = new ArrayList<>();
			RequestAttributes requestattributes = RequestContextHolder.currentRequestAttributes();
			if (requestattributes != null) {
				HttpServletRequest request = ((ServletRequestAttributes)requestattributes).getRequest();
				for (Shortcut shortcut : shortcutList) {
					if (!StringUtils.isEmpty(request.getParameter("shortcut")) && request.getParameter("shortcut").equals(shortcut.getPermission())) {
						menuMap.put("subsystemUrl", shortcut.getSubsystemUrl());
						if (shortcut.getSubsystemid() != null && shortcut.getSubsystemid().equals(shortcut.getMenuid())) {
							menuMap.put("subsystemId", shortcut.getSubsystemid());
							menuMap.put("menuUrl", shortcut.getMenuUrl());
						} else {
							menuMap.put("subsystemId", "");
							menuMap.put("menuUrl", shortcut.getMenuUrl());
						}
					}
					if (shortcut.getMenuUrl() != null && shortcut.getSubsystemid() != null && shortcut.getIsShow() == 1 && !shortcut.getSubsystemid().equals(shortcut.getMenuid())) {
						shortcuts.add(shortcut);
					}
				}
				String openUrl = (String) request.getSession().getAttribute("openUrl");
				String pageid = (String) request.getSession().getAttribute("pageid");
				if (!StringUtils.isEmpty(openUrl) || !StringUtils.isEmpty(pageid)) {
					try {
						Menu openMenu = new Menu();
						if(!StringUtils.isEmpty(pageid) && !StringUtils.isEmpty(openUrl)){
							openMenu.setId(pageid);
							openMenu.setActionName( new String(Encodes.decodeBase64(openUrl)));
						}else{
							if(!StringUtils.isEmpty(pageid)){
								openMenu.setId(pageid);
							}else{
								openMenu.setActionName( new String(Encodes.decodeBase64(openUrl)));
							}
							openMenu = menuService.get(openMenu);
						}
						menuMap.put("pageid", openMenu.getId());
						menuMap.put("openUrl", openMenu.getActionName());
						request.getSession().removeAttribute("openUrl");
						request.getSession().removeAttribute("pageid");
					} catch (Exception e){
						menuMap.put("pageid", "");
						menuMap.put("openUrl", "");
					}
				}else{
					//设置默认首页
					menuMap.put("pageid", "");
					menuMap.put("openUrl", "");
				}
			}
			menuMap.put("voiceStatus", UserUtils.getNewUser(user.getId()).getExtStr5());
			menuMap.put("shortcutList", shortcuts);
			TabTree tabTree = new TabTree();
			tabTree.setSysPermission(GlobalContext.getProperty("subsystem.id"));
			List<TabTree> tabTreeList = tabTreeService.queryAll(tabTree);
			if (tabTreeList != null && tabTreeList.size() > 0){
				menuMap.put("tabTreeList", JsonUtil.java2Json(tabTreeList));
			} else {
				menuMap.put("tabTreeList", "");
			}
			model.mergeAttributes(menuMap);
			model.addAttribute("ischildnode", 0);
		}
		requests.getSession().setAttribute("theme","classics");
		requests.getSession().setAttribute("color","blue");
		requests.getSession().setAttribute("font",principal.getFont());
		String toUrl = requests.getParameter("to");
		requests.setAttribute("toUrl", toUrl == null ? "" : toUrl);
		return "sys/menu/desktop";
	}

	/**
	* @description 加载桌面左侧菜单
	* @return String 跳转的路径
	* @throws Exception
	*/
	@RequestMapping(value="manageleft.action",method=RequestMethod.GET)
	public String manageleft(Model model,String id,String ischildnode) throws Exception{
		//获取用户登录信息
		User user = SystemSecurityUtils.getUser();
		Menu menuVo = new Menu();
		menuVo.setId(id);
		menuVo.setIsShow(0);
		if(user.getIsSystemAdmin()) {
			menuVo.setUserId("-99");
		} else {
			menuVo.setUserId(user.getId());
		}
		List<Menu> leftmenuList = menuService.getResourceInfo(menuVo,"power");
		model.addAttribute("leftmenuList", leftmenuList);
		model.addAttribute("ischildnode", ischildnode);
		return "sys/menu/leftmenu";
	}

	/**
	 * @description 删除方法(物理删除)
	 * @param  menu 实体类
	 * @param  ids 删除的多个主键
	 * @return Integer 成功删除
	 * @throws Exception
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	public Integer deleteByIds(Menu menu,String ids) throws Exception{
		menu.setPrimaryKeys(ids.split(","));
		return menuService.delete(menu);
	}

	/**
	 * @description 删除方法(逻辑删除)
	 * @param  menu 实体类
	 * @param  id 删除的多个主键
	 * @return Integer 成功删除
	 * @throws Exception
	 */
	@RequestMapping(value="delete.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="资源设置",operateFuncNm="delete",operateDescribe="对资源设置进行菜单删除")
	public Integer delete(Menu menu,String id,HttpServletRequest request) throws Exception{
		String ids = menuService.groupMenuId(id);
		menu.setPrimaryKeys(ids.split(","));
		menu.setModifyDate(DateUtils.getSysDate());
		return menuService.delete(menu);
	}

	/**
	 * @description 保存方法
	 * @param  menu 实体类
	 * @param  result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="资源设置",operateFuncNm="save",operateDescribe="对资源设置进行菜单添加")
	public Map<String,Object> save(@Valid Menu menu,BindingResult result,HttpServletRequest request) throws Exception{

		mv.validate(menu, result);

		Map<String,Object> resultMap = validateBean(result);
		// 验证bean
		if (resultMap.size() > 0) {
			return resultMap;
		}
		// 验证token
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		if(!"false".equals(resultMap.get("success"))){
			menu.setDeleteFlag(0);
			menu.setCreateDate(DateUtils.getSysDate());
			menu.setModifyDate(DateUtils.getSysDate());
			menu.setWeight(30);
			menuService.save(menu);
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	/**
	* @description 修改方法
	* @param  menu 实体类
	* @return Integer 更新的数目
	*/
	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="资源设置",operateFuncNm="update",operateDescribe="对资源设置进行菜单修改")
	public Map<String, Object> update(Menu menu,BindingResult result,HttpServletRequest request) throws Exception{

		mv.validate(menu, result);
		// 验证bean
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		menu.setModifyDate(DateUtils.getSysDate());
		Integer flag = menuService.update(menu);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
		}
		return resultMap;
	}

	/**
	 * @description 获取单条记录方法
	 * @param  menu 实体类
	 * @return Menu 查询结果
	 * @throws Exception
	 */
	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	public Menu get(Menu menu) throws Exception{
		return menuService.get(menu);
	}

	/**
	 * @description 加载数据信息（资源添加）
	 * @param  id 父菜单id
	 * @return Menu 查询结果
	 * @throws Exception
	 */
	@RequestMapping(value="getaddpage.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getaddpage(String id, HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put("menuvos", menuService.getAddInfo(id));
		map.put(GlobalContext.SESSION_TOKEN, token);
		return map;
	}

	/**
	 * @description 加载数据信息（资源修改）
	 * @param  id 菜单id
	 * @return Menu 查询结果
	 * @throws Exception
	 */
	@RequestMapping(value="geteditpage.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> geteditpage(String id, HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put("menuvos", menuService.getEditInfo(id));
		map.put(GlobalContext.SESSION_TOKEN, token);
		return map;
	}

	/**
	 * @description 验证菜单数据是否存在子菜单（资源删除）
	 * @param  id 菜单id
	 * @return Map 查询结果 true不存在	false存在
	 * @throws Exception
	 */
	@RequestMapping(value="valClickMenu.action",method=RequestMethod.GET)
	@ResponseBody
	public  Map<String, Object> valClickMenu(String id)throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String ids = menuService.groupMenuId(id);
		if(ids.equals(String.valueOf(id))){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
		}
		return resultMap;
	}

	/**
	 * @description 验证菜单数据是否已被选中（资源删除）
	 * @param  id 菜单id
	 * @return Map 查询结果 true不存在	false存在
	 * @throws Exception
	 */
	@RequestMapping(value="valMenuSel.action",method=RequestMethod.GET)
	@ResponseBody
	public  Map<String, Object> valMenuSel(String id)throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(menuService.getRolesByMenu(id) == 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
		}
		return resultMap;
	}

	/**
	  * @description 获得菜单树信息
	  * @param menu 菜单实体bean
	  * @return List<Menu> 菜单树
	  * @throws Exception
	*/
	@RequestMapping(value = "menuTree.action",method=RequestMethod.GET)
	@ResponseBody
	public List<Menu> menuTree(Menu menu) throws Exception{
		menu.setDeleteFlag(0);
		menu.setUserId(SystemSecurityUtils.getUser().getId());
		List<Menu> menuList;
		User user = SystemSecurityUtils.getUser();
		if(user.getIsSystemAdmin() || user.getIsSystemSecurity()){
			menu.setUserId("-99");
			menuList = menuService.queryWithRole(menu);
		}else{
			menuList = menuService.queryWithRole(menu);
		}
		return menuList;
	}
	
	@RequestMapping(value = "menuTree.action",method=RequestMethod.POST)
	@ResponseBody
	public List<Menu> menuTreePost(Menu menu) throws Exception{
		return menuTree(menu);
	}

	/**
	 * @description 验证当前用户是否已分配此菜单
	 * @param  id 菜单id
	 * @return Map 查询结果 true不存在	false存在
	 * @throws Exception
	 */
	@RequestMapping(value="valUserMenu.action",method=RequestMethod.GET)
	@ResponseBody
	public  Map<String, Object> valUserMenu(String id)throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//获取用户登录信息
		User user = SystemSecurityUtils.getUser();

		Menu menuVo = menuService.queryUserMenu(id,user.getId());

		if(menuVo == null){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
		}
		resultMap.put("menuVo", menuVo);
		return resultMap;
	}
	
	@RequestMapping(value="valUserMenu.action",method=RequestMethod.POST)
	@ResponseBody
	public  Map<String, Object> valUserMenuPost(String id)throws Exception{
		return valUserMenu(id);
	}

	/**
	 * @description 获得菜单树信息
	 * @param menu 菜单实体bean
	 * @return List<Menu> 菜单树
	 * @throws Exception
	 */
	@RequestMapping(value = "menuTreeForOne.action",method=RequestMethod.GET)
	@ResponseBody
	public List<Menu> menuTreeForOne(Menu menu) throws Exception{
		//获取用户登录信息
		User user = SystemSecurityUtils.getUser();

		menu.setDeleteFlag(0);
		menu.setMenuType(1);
		menu.setIsShow(0);
		menu.setParentId("-1");
		menu.setUserId(user.getId());
		List<Menu> menuList;
		if(user.getIsSystemAdmin()){
			menu.setUserId("-99");
			menuList = menuService.queryMTreeForOne(menu);
		}else{
			menuList = menuService.queryMTreeForOne(menu);
		}
		return menuList;
	}

	/**
	 * @description 获得菜单树信息
	 * @param menu 菜单实体bean
	 * @return List<Menu> 菜单树
	 * @throws Exception
	 */
	@RequestMapping(value = "menuTreeForOther.action",method=RequestMethod.GET)
	@ResponseBody
	public List<Menu> menuTreeForOther(Menu menu,String parentid) throws Exception{
		List<Menu> menuList = new ArrayList<Menu>();
		if(parentid != null){
			menuList = menuService.queryMenuTree(parentid);
		}
		return menuList;
	}
}