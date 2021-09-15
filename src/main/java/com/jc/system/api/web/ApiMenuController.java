package com.jc.system.api.web;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonResult;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.system.api.domain.ApiMeta;
import com.jc.system.api.service.IApiMetaService;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.service.IMenuService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping("/api/menu")
public class ApiMenuController {
    protected Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    IMenuService menuService;

    /**
     * @description 增加菜单
     */
    @RequestMapping(value = "add.action",method=RequestMethod.GET)
    @ResponseBody
    public JsonResult addMenu(String jsonparams) {
        logger.debug("request parameter - jsonparams:" + jsonparams);
        JsonResult result = null;
        Map<String, String> params = (Map<String, String>) JsonUtil.json2Java(jsonparams, Map.class);
        if (params == null) {
            return new JsonResult(-1, "jsonparams is empty");
        }
        List<String> requiredParamsKeys = new ArrayList<>();
    	requiredParamsKeys.add("name");
    	requiredParamsKeys.add("action");
    	requiredParamsKeys.add("queue");
    	requiredParamsKeys.add("parentnames");
        for (String pkey : requiredParamsKeys) {
            if (!params.containsKey(pkey)) {
                return new JsonResult(-1, pkey + " not exist");
            }
        }
        String parentId = "-1";
        if (!StringUtil.trimIsEmpty(params.get("parentnames"))) {
            Menu parentMenu = menuService.getMenuByHierarchy(params.get("parentnames"));
            if (parentMenu == null || parentMenu.getId() == null) {
                return new JsonResult(-1, "Cannot find parent menu");
            } else {
                parentId = parentMenu.getId();
            }
        }
        try {
            Menu menu = new Menu();
            menu.setName(params.get("name"));
            menu.setActionName(params.get("action"));
            menu.setParentId(parentId);
            if(menuService.get(menu) != null) {
                String msg = params.get("name") + "此菜单已经注册";
                logger.warn(msg);
                return new JsonResult(0, msg);
            }
            menu.setQueue(Integer.valueOf(params.get("queue")));
            menu.setMenuType(0);
            menu.setIsShow(0);
            Integer ret = menuService.save(menu);
            if (ret > 0) {
                result = new JsonResult(0, "Success");
            }
        } catch (CustomException e) {
            logger.error(e.getMessage());
            result = new JsonResult(-1, "Error occurred when save menu");
        }
        return result;
    }
}
