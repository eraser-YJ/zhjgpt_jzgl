package com.jc.system.security.web;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.common.util.BeanUtil;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.*;
import com.jc.system.security.domain.validator.RoleValidator;
import com.jc.system.security.service.IRoleService;
import com.jc.system.util.ClazzInvoker;
import com.jc.system.util.menuUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色控制器
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value = "/sys/role")
public class RoleController extends BaseController {
    private static Logger logger = Logger.getLogger(RoleController.class);
    @Autowired
    private IRoleService roleService;


    public RoleController() {

    }

    @org.springframework.web.bind.annotation.InitBinder("role")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new RoleValidator());
    }

    @RequestMapping(value = "manage.action")
    @ActionLog(operateModelNm="角色设置",operateFuncNm="manageList",operateDescribe="对角色设置进行访问")
    public String manageList(Model model, HttpServletRequest request) {
        User userInfo = SystemSecurityUtils.getUser();
        if(userInfo != null){
            if(userInfo.getIsAdmin().equals(2) || userInfo.getIsSystemAdmin()){
                try {
                    menuUtil.saveMenuID("/sys/role/manage.action",request);
                } catch (CustomException e) {
                    logger.error(e);
                }
                return "sys/role/role";
            } else {
                return "error/unauthorized";
            }
        }
        return "error/unauthorized";
    }

    @RequestMapping(value = "manageList.action")
    @ResponseBody
    @ActionLog(operateModelNm="角色设置",operateFuncNm="manageList",operateDescribe="对角色设置进行查询")
    public PageManager manageList(Role role, final PageManager page, HttpServletRequest request) throws CustomException {
        if(StringUtils.isEmpty(role.getOrderBy())) {
            role.addOrderByFieldDesc("t.CREATE_DATE");
        }
        role.setDeleteFlag(0);
        return roleService.query(role, page);
    }

    @RequestMapping(value = "save.action",method=RequestMethod.POST)
    @ResponseBody
    @ActionLog(operateModelNm="角色设置",operateFuncNm="save",operateDescribe="对角色设置进行添加")
    public  Map<String, Object> save(@Valid Role role, BindingResult result, HttpServletRequest request) throws Exception  {
        Map<String, Object> resultMap = validateBean(result);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        resultMap = validateToken(request);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        Role rBean = new Role();
        rBean.setName(role.getName());
        rBean.setDeptIds(role.getDeptId());
        if (roleService.get(rBean) != null) {
            throw new CustomException("角色名称已存在");
        }
        if (roleService.save(role) == 1) {
            resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
        }
        String token = getToken(request);
        resultMap.put(GlobalContext.SESSION_TOKEN, token);
        return resultMap;
    }

    /**
     * 验证角色名称
     */
    @ResponseBody
    @RequestMapping(value = "checkName",method=RequestMethod.GET)
    public String checkName(String oldName, Role role) {
        return "false";
    }

    /**
     * @description 修改方法
     * @param role 实体类
     * @return Integer 更新的数目
     */
    @RequestMapping(value = "update.action",method=RequestMethod.POST)
    @ResponseBody
    @ActionLog(operateModelNm="角色设置",operateFuncNm="update",operateDescribe="对角色设置进行修改")
    public Map<String, Object> update(@Valid Role role, BindingResult result, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = validateBean(result);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        resultMap = validateToken(request);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        role.setModifyDateNew(DateUtils.getSysDate());
        if (roleService.update(role) == 1) {
            CacheClient.removeCache("role_info_" + role.getId());
            resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
        }
        String token = getToken(request);
        resultMap.put(GlobalContext.SESSION_TOKEN, token);
        return resultMap;
    }

    @RequestMapping(value = "deleteByIds.action",method=RequestMethod.POST)
    @ResponseBody
    @ActionLog(operateModelNm="角色设置",operateFuncNm="deleteByIds",operateDescribe="对角色设置进行删除")
    public Integer deleteByIds(Role role, String ids, HttpServletRequest request) throws Exception {
        role.setPrimaryKeys(ids.split(","));
        return roleService.delete(role);
    }

    @RequestMapping(value = "get.action",method=RequestMethod.GET)
    @ResponseBody
    public Role get(Role role) throws Exception {
        role.setDeleteFlag(0);
        return roleService.get(role);
    }

    @RequestMapping(value = "roleEdit.action",method=RequestMethod.GET)
    public String roleEdit(Model model, HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String token = getToken(request);
        map.put(GlobalContext.SESSION_TOKEN, token);
        model.addAttribute("data", map);
        return "sys/role/roleEdit";
    }

    @RequestMapping(value = "roleAuthorize.action",method=RequestMethod.GET)
    public String roleAuthorize(Model model, HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String token = getToken(request);
        map.put(GlobalContext.SESSION_TOKEN, token);
        model.addAttribute("data", map);
        return "sys/role/roleAuthorize";
    }

    /**
     * 保存角色-菜单关联数据
     * @param roleMap
     * @param result
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "saveRoleMenu.action",method=RequestMethod.POST)
    @ResponseBody
    @ActionLog(operateModelNm="角色设置",operateFuncNm="saveRoleMenu",operateDescribe="对角色设置进行授权")
    public  Map<String, Object> saveRoleMenu(@RequestBody Map<String, Object> roleMap, BindingResult result,HttpServletRequest request) throws Exception  {
        List<Object> userlist = (List<Object>)roleMap.get("sysUserRoles");
        boolean sign = false;
        if (userlist.size() == 1){
            Object roleId = "";
            for (Object obj:userlist){
                Map<String, Object> userMap= (Map<String, Object>)obj;
                if ("all".equals(userMap.get("userId"))){
                    sign = true;
                    roleId = userMap.get("roleId");
                    break;
                }
            }
            if (sign){
                List reMap = new ArrayList();
                ClazzInvoker pinUsers = new ClazzInvoker("com.jc.system.sys.service.impl.PinUserServiceImpl", "queryPinUsers", null);
                List<?> list = (List<?>) pinUsers.getInvoker();
                for (Object puser:list){
                    Map<String,Object> tempMap = new HashMap<>();
                    tempMap.put("userId",puser);
                    tempMap.put("roleId",roleId);
                    reMap.add(tempMap);
                }
                roleMap.put("sysUserRoles",reMap);
            }
        }
        Role role = BeanUtil.map2Object(roleMap, Role.class);
        Map<String, Object> resultMap = validateBean(result);
        roleService.saveRoleMenu(role);
        resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
        String token = getToken(request);
        resultMap.put(GlobalContext.SESSION_TOKEN, token);
        return resultMap;
    }

    /**
     * 根据角色获得已选的菜单
     * @param roleMenus
     * @param result
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getMenusByRole.action",method=RequestMethod.GET)
    @ResponseBody
    public  List<RoleMenus> getMenusByRole(RoleMenus roleMenus, BindingResult result, HttpServletRequest request) throws Exception  {
        return roleService.getMenusByRole(roleMenus);
    }

    @RequestMapping(value = "getMenusByRole.action",method=RequestMethod.POST)
    @ResponseBody
    public  List<RoleMenus> getMenusByRolePOST(RoleMenus roleMenus, BindingResult result, HttpServletRequest request) throws Exception  {
        return roleService.getMenusByRole(roleMenus);
    }

    /**
     * 根据角色获得已选的菜单
     * @param roleExts
     * @param result
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getExtsByRole.action",method=RequestMethod.GET)
    @ResponseBody
    public  List<RoleExts> getExtsByRole(RoleExts roleExts, BindingResult result, HttpServletRequest request) throws Exception  {
        return roleService.getExtsByRole(roleExts);
    }

    @RequestMapping(value = "getExtsByRole.action",method=RequestMethod.POST)
    @ResponseBody
    public  List<RoleExts> getExtsByRolePOST(RoleExts roleExts, BindingResult result, HttpServletRequest request) throws Exception  {
        return roleService.getExtsByRole(roleExts);
    }

    /**
     * 根据角色获得已选的部门
     * @param roleBlocks
     * @param result
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getBlcoksByRole.action",method=RequestMethod.GET)
    @ResponseBody
    public  List<RoleBlocks> getBlcoksByRole(RoleBlocks roleBlocks, BindingResult result, HttpServletRequest request) throws Exception  {
        return roleService.getBlocksByRole(roleBlocks);
    }

    /**
     * 获得部门及部门下所有角色集合
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "getAllDeptAndRole.action",method=RequestMethod.GET)
    public void getAllDeptAndRole(HttpServletResponse response) throws Exception {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().print(JsonUtil.java2Json(roleService.getAllDeptAndRole()));
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * 验证同一部门下角色名称是否存在
     * @param role
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "checkRoleName.action",method=RequestMethod.GET)
    @ResponseBody
    public String checkRoleName(Role role) throws Exception {
        if(role.getNameOld().equals(role.getName())){
            return "true";
        }else{
            if(roleService.get(role) == null ){
                return "true";
            }else{
                return "false";
            }
        }
    }

}
