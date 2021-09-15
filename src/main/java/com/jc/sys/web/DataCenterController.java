package com.jc.sys.web;

import com.jc.foundation.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="/csmp/dataCenter")
public class DataCenterController extends BaseController {

    @RequestMapping(value = "centerForword.action",method= RequestMethod.GET)
    public String centerForword(Model model, HttpServletRequest request) throws Exception {
        String path = request.getParameter("path");
        request.setAttribute("path", path);
        model.addAttribute("path", path);

        return "csmp/forword";
    }

}
