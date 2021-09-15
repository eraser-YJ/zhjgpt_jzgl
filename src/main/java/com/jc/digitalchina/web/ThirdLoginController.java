package com.jc.digitalchina.web;

import com.jc.common.kit.StringUtil;
import com.jc.digitalchina.service.ICmUserRelationService;
import com.jc.digitalchina.util.DigitalContext;
import com.jc.digitalchina.util.ThirdResult;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.Result;
import com.jc.foundation.web.BaseController;
import com.jc.resource.util.HttpClientUtil;
import com.jc.system.security.UserToken;
import com.jc.system.security.domain.User;
import com.jc.workflow.util.JsonUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 神州登录
 * @Author 常鹏
 * @Date 2020/9/10 12:51
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/third")
public class ThirdLoginController extends BaseController {

    @Autowired
    private ICmUserRelationService cmUserRelationService;

    @RequestMapping(value = "login.action", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = request.getParameter("token");
        String to = request.getParameter("to");
        System.out.println("**********************************");
        System.out.println("token: " + token);
        System.out.println("**********************************");
        String backUrl = DigitalContext.myUrl + "/csmp/third/login.action";
        if (!StringUtil.isEmpty(to)) {
            Cookie cookie = new Cookie("jumpSign", StringUtil.replaceStr(to, ",", "+"));
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
        }
        if (StringUtil.isEmpty(token)) {
            System.out.println("token is null");
            response.sendRedirect(DigitalContext.systemUrl + "/auth/login?clientId=" +DigitalContext.clientId + "&backUrl=" + backUrl);
            return null;
        }
        Result result = HttpClientUtil.get(DigitalContext.systemUrl + "/auth/valid?token=" + token);
        ThirdResult thirdResult = (ThirdResult) JsonUtil.json2Java((String) result.getData(), ThirdResult.class);
        if (thirdResult.getCode().intValue() != 200) {
            System.out.println("********************");
            System.out.println("token: " + token);
            System.out.println("thirdResult code is " + thirdResult.getCode());
            System.out.println("********************");
            response.sendRedirect(DigitalContext.systemUrl + "/auth/login?clientId=" +DigitalContext.clientId + "&backUrl=" + backUrl);
            return null;
        }
        /**使用上面接口返回的data进行查询，查询出用户信息*/
        User user = this.cmUserRelationService.getByUuid((String) thirdResult.getData());
        if (user == null) {
            System.out.println("********************");
            System.out.println("token: " + token);
            System.out.println("user is null, code = " + thirdResult.getData());
            System.out.println("********************");
            response.sendRedirect(DigitalContext.systemUrl + "/auth/login?clientId=" +DigitalContext.clientId + "&backUrl=" + backUrl);
            return null;
        }
        String checkUserName = ",security,audit,manager,admin,";
        if (checkUserName.indexOf("," + user.getLoginName() + ",") > -1) {
            //避免与内置用户冲突
            user.setLoginName(user.getLoginName() + "_admin");
        }
        UserToken userToken = new UserToken(user.getLoginName(), user.getPassword(), true, null);
        userToken.setThirdly(true);
        userToken.setLoginUser(user);
        Subject subject = SecurityUtils.getSubject();
        subject.login(userToken);
        //读cookie，判断是否需要跳转到指定页面
        Cookie[] cookies = request.getCookies();
        String toSign = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jumpSign")) {
                    toSign = cookie.getValue();
                }
            }
        }
        System.out.println("********************");
        System.out.println("toSign: " + toSign);
        System.out.println("********************");
        if (!StringUtil.isEmpty(toSign)) {
            //删除cookie
            Cookie cookie = new Cookie("jumpSign", null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            response.sendRedirect(GlobalContext.getProperty("cas.localUrl") + "/sys/menu/manage.action?to=" + StringUtil.replaceStr(toSign, "+", ","));
            return null;
        }
        response.sendRedirect(GlobalContext.getProperty("cas.localUrl"));
        return null;
    }
}
