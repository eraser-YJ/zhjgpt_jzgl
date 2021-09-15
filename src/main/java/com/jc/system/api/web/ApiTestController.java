package com.jc.system.api.web;

import com.jc.foundation.util.JsonResult;
import com.jc.system.security.service.IMenuService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URISyntaxException;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping("/api/test")
public class ApiTestController {
    protected Logger logger = Logger.getLogger(this.getClass());

    private static final long serialVersionUID = 1L;
    @Autowired
    IMenuService menuService;

    /**
     * @description 增加菜单
     */
    @RequestMapping(value = "test.action",method=RequestMethod.GET)
    @ResponseBody
    public JsonResult test(String jsonparams) {
        JsonResult result = null;
        try {
            String name = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            result  = new JsonResult(0, name);
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return result;
    }
}
