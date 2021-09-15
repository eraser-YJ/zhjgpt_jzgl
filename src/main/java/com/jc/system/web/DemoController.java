package com.jc.system.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Controller
@RequestMapping(value = "/demo")
public class DemoController {

    @RequestMapping(value="treeDemo.action",method=RequestMethod.GET)
    public String manage(Model model, HttpServletRequest requests) throws Exception{
        System.out.println();
        return "sys/demo/treeDemo";//默认的
    }
}
