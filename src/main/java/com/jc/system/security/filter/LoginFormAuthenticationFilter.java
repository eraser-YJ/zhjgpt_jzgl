package com.jc.system.security.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jc.system.security.ILoginVerification;
import com.jc.system.security.UserToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class LoginFormAuthenticationFilter extends FormAuthenticationFilter{

    @Autowired(required=false)
    private ILoginVerification loginVerification;

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String url = this.getSuccessUrl();
        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + url);
        return false;
    }

    @Override
    protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
        if (loginVerification != null) {
            return (request instanceof HttpServletRequest);
        } else {
            return (request instanceof HttpServletRequest) && WebUtils.toHttp(request).getMethod().equalsIgnoreCase(POST_METHOD);
        }
    }

    @Override
    protected AuthenticationToken createToken(String username, String password, boolean rememberMe, String host) {
        UserToken token = new UserToken(username, password, rememberMe, host);
        if(loginVerification != null){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            token.setLoginUser(loginVerification.getUser(request));
            token.setThirdly(true);
        }
        return token;
    }
}
