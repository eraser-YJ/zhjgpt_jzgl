package com.jc.system.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jc.foundation.util.GlobalContext;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class HostAttackFilter implements Filter{

	/** 参数：白名单访问地址 */
	private static final String PARAM_WHITE_LIST = "request.host.whiteList";

	/** 切分符 */
	public static final String SPLIT_FLAG = ","; 

	/** 白名单 */
	private String[] whiteList = null;

	/**
	 * <B>方法名称：</B>初始化<BR>
	 * <B>概要说明：</B>初始化过滤器。<BR>
	 * 
	 * @param fConfig 过滤器配置
	 * @throws ServletException Servlet异常
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {

		String whiteListstr = GlobalContext.getProperty(PARAM_WHITE_LIST);
		if (whiteListstr != null) {
			this.whiteList = whiteListstr.split(SPLIT_FLAG);
		}

	}

	/**
	 * <B>方法名称：</B>过滤处理<BR>
	 * <B>概要说明：</B>验证请求的HOST名称是否在白名单中。<BR>
	 * 
	 * @param request  请求
	 * @param response 响应
	 * @param chain    过滤器链
	 * @throws IOException      IO异常
	 * @throws ServletException Servlet异常
	 * @see Filter#doFilter(ServletRequest,
	 *      ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (true) {
			chain.doFilter(request, response);
			return;
		}
		try {
			// 设定请求默认字符集
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			// 取得host值
			String hostName = req.getHeader("host");
			if (isEmpty(hostName)) {
				hostName = req.getHeader("Host");
				if (isEmpty(hostName)) {
					hostName = req.getHeader("HOST");
				}
			}
			// 判断是否在白名单中,不通过返回403
			if (inWhiteList(hostName)) {
				// 当前serverName名称
				String serverName = req.getServerName();
				// 判断是否在白名单中,不通过返回403
                if (inWhiteList(serverName)) {
                    chain.doFilter(request, response);
                    //System.out.println("HostFilter================host[" + hostName + "]验证通过,url="+req.getRequestURI());
                } else {
                    res.setStatus(403);
                    //System.out.println("HostAttack================serverName[" + serverName + "],url="+req.getRequestURI());
                    return;
                }

            } else {
                res.setStatus(403);
                //System.out.println("HostAttack================hostName[" + hostName + "],url="+req.getRequestURI());
                return;
            }
        } catch (Exception e) {
            //System.out.println("HostAttack================异常:"+e.getMessage());
            e.printStackTrace();
        }
	}

	/**
	 * <B>方法名称：</B>释放资源<BR>
	 * <B>概要说明：</B><BR>
	 * 
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
		// 释放过滤器资源
	}

	/**
	 * <B>方法名称：</B>判断是否为忽略地址<BR>
	 * <B>概要说明：</B>判断是否为忽略地址。<BR>
	 * 
	 * @param hostName 待判断HOST名称
	 * @return boolean true:在可信白名单，可通过
	 */
	protected boolean inWhiteList(String hostName) {
		if (isEmpty(hostName)) {
			return true;
		}
		if (this.whiteList == null || this.whiteList.length == 0) {
			return true;
		}
		for (String secureHost : this.whiteList) {
			if (hostName.length() < 2 * secureHost.length() && hostName.indexOf(secureHost) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判空
	 * 
	 * @param value
	 * @return
	 */
	private boolean isEmpty(String value) {
		if (value == null || value.trim().length() == 0) {
			return true;
		}
		return false;
	}
}
