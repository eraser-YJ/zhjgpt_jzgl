package com.jc.system.plugin.web;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.plugin.PluginMenuToMenu;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.plugin.domain.Plugin;
import com.jc.system.plugin.domain.PluginDependencies;
import com.jc.system.plugin.domain.PluginMenu;
import com.jc.system.plugin.service.IPluginService;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.service.IMenuService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value = "/sys/plugin")
public class PluginController extends BaseController {

    protected Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private IPluginService pluginService;

    public PluginController() {
    }

    @RequestMapping(value = "manageList.action",method=RequestMethod.GET)
    @ResponseBody
    @ActionLog(operateModelNm = "操作日志", operateFuncNm = "manageList", operateDescribe = "对插件进行查询")
    public PageManager manageList(Plugin plugin, PageManager page, HttpServletRequest request) {
        //默认排序
        if (StringUtils.isEmpty(plugin.getOrderBy())) {
            plugin.addOrderByFieldDesc("t.CREATE_DATE");
        }
        plugin.setUuid(null);
        PageManager rePage = pluginService.query(plugin, page);
        return rePage;
    }

    @RequestMapping(value = "manage.action",method=RequestMethod.GET)
    public String manage() throws Exception {
        return "sys/plugin/plugin";
    }

    @RequestMapping(value = "deleteByIds.action",method=RequestMethod.POST)
    @ResponseBody
    public Integer deleteByIds(Plugin plugin, String ids) throws Exception {
        plugin.setPrimaryKeys(ids.split(","));
        return pluginService.delete(plugin);
    }

    @RequestMapping(value = "save.action",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(@Valid Plugin plugin, BindingResult result) throws Exception {

        Map<String, Object> resultMap = validateBean(result);
        if (!"false".equals(resultMap.get("success"))) {
            pluginService.save(plugin);
            resultMap.put("success", "true");
        }
        return resultMap;
    }

    @RequestMapping(value = "update.action",method=RequestMethod.POST)
    @ResponseBody
    public Integer update(Plugin plugin) throws Exception {
        Integer flag = pluginService.update(plugin);
        return flag;
    }

    @RequestMapping(value = "changeStatus.action",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> changeStatus(Plugin plugin, BindingResult result) throws Exception {
        Plugin temp = new Plugin();
        temp.setUuid(null);
        temp.setId(plugin.getId());
        temp.setState(plugin.getState()==0?1:0);
        temp = pluginService.get(temp);
        Plugin regPlugin = PluginUtil.getPluginMap().get(temp.getUuid());
        plugin.setUuid(null);
        Map<String, Object> resultMap = validateBean(result);
        Integer flag = pluginService.update(plugin);
        if (flag == 1) {
            if (regPlugin!=null && regPlugin.getMenus() != null && regPlugin.getMenus().size() > 0) {
                for (PluginMenu pluginMenu : regPlugin.getMenus()) {
                    changeLocalMenu(pluginMenu,plugin.getState());
                }
            }
            resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
            resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
        }
        return resultMap;
    }

    protected boolean changeLocalMenu(PluginMenu pluginMenu,int deleteFlag) {
        boolean flag = false;
        String parentId = "-1";

        IMenuService menuService = SpringContextHolder.getBean(IMenuService.class);

        try {
            String menuNameList = pluginMenu.getParentName();
            if (!StringUtil.trimIsEmpty(menuNameList)) {
                menuNameList = getSystemMenuNames(menuNameList);
                Menu parentMenu = menuService.getMenuByHierarchy(menuNameList,1);

                if (parentMenu == null) {
                    logger.error("没有找到上层菜单" + pluginMenu + "，请检查插件配置");
                    return false;
                } else {
                    parentId = parentMenu.getId();
                }
            }
            Menu newMenu = PluginMenuToMenu.toMenu(pluginMenu,parentId);
            Menu menu = new Menu();
            menu.setName(newMenu.getName());
            menu.setParentId(parentId);
            menu.setDeleteFlag(deleteFlag);
            
            menu = menuService.get(menu);
            
            if(menu == null){
            	return flag;
            }
            
            List<Menu> menuList1 = new ArrayList<Menu>();;
            menuList1.add(menu);
            List<Menu> menuList = menuService.menuTreeForParentId(menu.getId(),menuList1);
            for (Menu tempMenu : menuList ) {
                int menuDeleteFlag = deleteFlag==0 ? 1 : 0;
                tempMenu.setDeleteFlag(menuDeleteFlag);
                Integer cnt = menuService.update(tempMenu);
                if (cnt < 1) {
                    flag = true;
                }
            }
        } catch (CustomException ex) {
            logger.error(ex.getMessage());
            flag = true;
        }
        return flag;
    }

    private String getSystemMenuNames(String menuNameList) {
        if (StringUtil.isEmpty(menuNameList)) {
            return "";
        }
        if (menuNameList.startsWith("{" + GlobalContext.SYS_SUBSYSTEM_FIELD + "}")) {
            if (GlobalContext.getProperty(GlobalContext.SYS_SUBSYSTEM_FIELD) != null) {
                menuNameList = menuNameList.replace(GlobalContext.SYS_SUBSYSTEM_FIELD,
                        GlobalContext.getProperty(GlobalContext.SYS_SUBSYSTEM_FIELD));
            } else {
                menuNameList = menuNameList.replace("{" + GlobalContext.SYS_SUBSYSTEM_FIELD + "},", "")
                        .replace("{" + GlobalContext.SYS_SUBSYSTEM_FIELD + "}", "");
            }
        }
        return menuNameList;
    }

    @RequestMapping(value = "get.action",method=RequestMethod.GET)
    @ResponseBody
    public Plugin get(Plugin plugin) throws Exception {
        return pluginService.get(plugin);
    }

    @RequestMapping(value="loadForm.action",method=RequestMethod.GET)
    public String loadForm(Model model,HttpServletRequest request) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        String token = getToken(request);
        map.put(GlobalContext.SESSION_TOKEN, token);
        model.addAttribute("data", map);
        return "sys/plugin/pluginService";
    }

    @RequestMapping(value = "savePluginService.action",method=RequestMethod.POST)
    @ResponseBody
    @ActionLog(operateModelNm="",operateFuncNm="save",operateDescribe="对进行新增操作")
    public Map<String,Object> savePluginService(@RequestBody Map<String, Object> pluginMap, BindingResult result,HttpServletRequest request) throws Exception{
        List<Map<String,String>> list = (List<Map<String, String>>) pluginMap.get("pluginService");
        List<PluginDependencies> servicelist = new ArrayList<>();
        for(Map<String,String> map : list){
            PluginDependencies pluginDependencies = new PluginDependencies();
            pluginDependencies.setPluginId(map.get("pluginId"));
            pluginDependencies.setServiceId(map.get("serviceId"));
            servicelist.add(pluginDependencies);
        }

        String pluginId = (String) pluginMap.get("pluginId");
        Map<String, Object> resultMap = validateBean(result);

        pluginService.savePluginService(servicelist,pluginId);

        resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
        String token = getToken(request);
        resultMap.put(GlobalContext.SESSION_TOKEN, token);
        return resultMap;
    }

}