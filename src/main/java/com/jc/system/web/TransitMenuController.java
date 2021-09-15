package com.jc.system.web;

import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.web.BaseController;
import com.jc.sys.domain.SubUser;
import com.jc.sys.service.ISubMenuService;
import com.jc.sys.service.ISubUserService;
import com.jc.sys.util.SubUserUtil;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.domain.Principal;
import com.jc.system.security.domain.Shortcut;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IMenuService;
import com.jc.system.security.service.IUserShortcutService;
import com.jc.system.security.util.UserUtils;
import com.jc.system.tab.domain.TabTree;
import com.jc.system.tab.service.ITabTreeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Controller
@RequestMapping(value = "/transitMenu")
public class TransitMenuController extends BaseController {

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

    @RequestMapping(value="manage.action",method=RequestMethod.GET)
    public String manage(Model model, HttpServletRequest requests) throws Exception{
        //-----从数据中心获取菜单信息-----------------------------无需修改---------start-------
        Map<String,Object> menuMap;
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        //返回页面链接
        String standardUrl = "sys/menu/desktopStandard";
        String classicsUrl = "sys/menu/desktopClassics";
        String conciseUrl = "sys/menu/desktopConcise";
        //获取用户登录信息
        User user = SystemSecurityUtils.getUser();

        //判断是否为一岗多职
        SubUser subUser = new SubUser();
        subUser.setId(user.getId());
        //subUser.setOrderBy("MODIFY_DATE desc");
        List<SubUser> subUserList = subUserService.queryAll(subUser);
        if (subUserList != null && subUserList.size() > 0){
            List<SubUser> subUsers = new ArrayList<>();
            for (SubUser subUser1:subUserList){
                SubUser reSubUser = new SubUser();
                reSubUser.setId(subUser1.getId());
                reSubUser.setDutyId(subUser1.getDutyId());
                reSubUser.setDeptId(subUser1.getDeptId());
                subUsers.add(reSubUser);
            }

            SubUser subUser2 = subUsers.get(0);
            menuMap = subMenuService.desktopMenuForSub(subUser2.getId(),subUser2.getDeptId());
            SubUserUtil.setSubUserDept(subUser2.getId(),subUser2.getDeptId());//注入当前用户岗位部门

            classicsUrl = "sys/subMenu/desktopClassics";
            standardUrl = "sys/subMenu/desktopClassics";
            menuMap.put("subUserList",subUserList);
            model.mergeAttributes(menuMap);
            model.addAttribute("ischildnode", 0);
        } else {
            //2014.7.17 chz 增加查询所有一级菜单列表 start----
            Menu menuVo = new Menu();
            //menuVo.setParentId(-1L);//一级菜单标识
            if(user.getIsSystemAdmin()) {
                menuVo.setUserId("-99");
            }else {
                menuVo.setUserId(user.getId());//临时 测试 需要提取登录用户信息------------------------------替换
            }
            menuMap = menuService.desktopMenuForQuery(menuVo);
            List<Shortcut> shortcutList = userShortcutService.getShortcutListByUserId(user.getId());
            List<Shortcut> shortcuts = new ArrayList<>();
            RequestAttributes requestattributes = RequestContextHolder.currentRequestAttributes();
            if(requestattributes != null)
            {
                HttpServletRequest request = ((ServletRequestAttributes)requestattributes).getRequest();
                for(Shortcut shortcut : shortcutList){
                    if(!StringUtils.isEmpty(request.getParameter("shortcut")) && request.getParameter("shortcut").equals(shortcut.getPermission())){
                        menuMap.put("subsystemUrl", shortcut.getSubsystemUrl());
                        if(shortcut.getSubsystemid()!=null && shortcut.getSubsystemid().equals(shortcut.getMenuid())){
                            menuMap.put("subsystemId", shortcut.getSubsystemid());
                            menuMap.put("menuUrl", shortcut.getMenuUrl());
                        }else{
                            menuMap.put("subsystemId", "");
                            menuMap.put("menuUrl", shortcut.getMenuUrl());
                        }
                    }
                    if(shortcut.getMenuUrl()!=null && shortcut.getSubsystemid()!=null && shortcut.getIsShow()==1 && !shortcut.getSubsystemid().equals(shortcut.getMenuid())){
                        shortcuts.add(shortcut);
                    }
                }
            }
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
        //-----从数据中心获取菜单信息-----------------------------无需修改---------end-------

        menuMap.put("voiceStatus", UserUtils.getNewUser(user.getId()).getExtStr5());
        //model.addAttribute("menuList", menuList);
        //2014.7.17 chz 增加查询所有一级菜单列表 end -----
        if (StringUtils.isNotEmpty(principal.getTheme())){
            requests.getSession().setAttribute("theme",principal.getTheme());
        } else {
            requests.getSession().setAttribute("theme","classics");
        }

        if (StringUtils.isNotEmpty(principal.getColor())){
            requests.getSession().setAttribute("color",principal.getColor());
        } else {
            requests.getSession().setAttribute("color","blue");
        }

        if (StringUtils.isNotEmpty(principal.getFont())){
            requests.getSession().setAttribute("font",principal.getFont());
        } else {
            requests.getSession().setAttribute("font","standard");
        }
        if("standard".equals(principal.getTheme())){//标准
            return standardUrl;
        }
        if("classics".equals(principal.getTheme())){//经典
            return classicsUrl;
        }
        if("concise".equals(principal.getTheme())){//简约
            return conciseUrl;
        }
        return classicsUrl;//默认的
        //return "sys/menu/desktopWhite";
    }
}
