package com.jc.system.security.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.domain.SysUserRole;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IRoleMenusService;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class RolesFilter extends RolesAuthorizationFilter{
	
	/**
	 * 白名单路径
	 */
	private static final String[] WHITE_LIST = {"/system","/sys/user/getUserInfo"};
	
	private static final String[] FILTER_NAMES = {"manage","get","update","delete","save"};
	
	@Override
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
		if(request instanceof HttpServletRequest) {
			HttpServletRequest hrequest = (HttpServletRequest)request;
	        User userInfo = SystemSecurityUtils.getUser();
	        if(userInfo!=null) {
	        	String requestURI = WebUtils.getPathWithinApplication(hrequest);
	        	for(String actionname:FILTER_NAMES) {
	        		if(requestURI.indexOf(actionname)!=-1) {
	        			boolean canpass = canpass(requestURI,userInfo,hrequest);
	        			if(!canpass) {
	        				return false;
	        			}
	        		}
	        	}
	        	return true;
	        }else {
	        	return true;
	        }
		}
		return false;
    }
	
	private boolean canpass(String requestURI,User userInfo,HttpServletRequest hrequest) throws IOException {
		requestURI = requestURI.substring(0, requestURI.lastIndexOf("/"));
		if("".equals(requestURI)||"/".equals(requestURI)) {
    		return true;
    	}
    	for(String menu:WHITE_LIST) {
    		if(menu.indexOf(requestURI)!=-1) {
    			return true;
    		}
    	}
    	List<SysUserRole> sysUserRole = userInfo.getSysUserRole();
    	StringBuilder sb = new StringBuilder("''");
    	for(SysUserRole role:sysUserRole) {
    		sb.append(",'"+role.getRoleId()+"'");
    	}
    	WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(hrequest.getSession().getServletContext());
    	IRoleMenusService menuService = applicationContext.getBean(IRoleMenusService.class);
    	List<Menu> menulist = menuService.queryMenuForRoles(sb.toString());
    	
    	for(Menu menu:menulist) {
    		if(menu.getActionName().indexOf(requestURI)!=-1) {
    			return true;
    		}
    	}
    	return false;
	}

}
