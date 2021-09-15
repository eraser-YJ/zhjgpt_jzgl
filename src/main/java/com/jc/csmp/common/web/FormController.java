package com.jc.csmp.common.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName FormController
 * @Deacription TODO
 * @Author Administrator
 * @Date 2021/4/6 18:41
 * @Version 1.0
 **/
@Controller
@RequestMapping(value = "/form")
public class FormController {

    @RequestMapping(value="jumpForm.action",method= RequestMethod.GET)
    public String jumpForm(Model model, HttpServletRequest request) throws Exception{
        String jumpUrl = request.getParameter("jumpUrl");
        model.addAttribute("jumpUrl", jumpUrl);
        return "csmp/form/jumpForm";
    }
}
