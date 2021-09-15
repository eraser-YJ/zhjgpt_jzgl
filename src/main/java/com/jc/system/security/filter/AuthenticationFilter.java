package com.jc.system.security.filter;

import com.jc.foundation.util.GlobalContext;
import com.jc.system.security.domain.Principal;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.jasig.cas.client.authentication.DefaultGatewayResolverImpl;
import org.jasig.cas.client.authentication.GatewayResolver;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class AuthenticationFilter extends AbstractCasFilter {
	protected transient final Logger logger = Logger.getLogger(this.getClass());
	private String casServerUrlPrefix;
	private boolean renew = false;

	private boolean gateway = false;

	@Override
	protected void initInternal(FilterConfig filterConfig) throws ServletException {
		if (!isIgnoreInitConfiguration()) {
			super.initInternal(filterConfig);
			setCasServerUrlPrefix(getPropertyFromInitParams(filterConfig, "casServerUrlPrefix", null));
			this.log.trace("Loaded casServerUrlPrefix parameter: " + this.casServerUrlPrefix);
			setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));
			this.log.trace("Loaded renew parameter: " + this.renew);
			setGateway(parseBoolean(getPropertyFromInitParams(filterConfig, "gateway", "false")));
			this.log.trace("Loaded gateway parameter: " + this.gateway);
		}
	}

	@Override
	public void init() {
		super.init();
		CommonUtils.assertNotNull(this.casServerUrlPrefix, "casServerUrlPrefix cannot be null.");
	}

	@Override
	public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		if ("/".equals(this.casServerUrlPrefix) || !"true".equals(GlobalContext.getProperty("cas.start"))) {
			filterChain.doFilter(request, response);
			return;
		}
		HttpSession session = request.getSession(false);
		Assertion assertion = session != null ? (Assertion) session.getAttribute("_const_cas_assertion_") : null;

		if (assertion != null) {
			filterChain.doFilter(request, response);
			return;
		}
		Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
		if (principal == null) {
			filterChain.doFilter(request, response);
			return;
		}
		String reuqestUrl = request.getRequestURI();
		Iterator iter = getKeyIterator();
		boolean flag = true;
		while (iter.hasNext()) {
			String invaild = (String) iter.next();
			if (reuqestUrl.indexOf(invaild) != -1) {
				if (!"true".equals(request.getParameter("csFlag"))) {
					flag = false;
				}
			}
		}
		if (flag == false) {
			filterChain.doFilter(request, response);
			return;
		}
		if (reuqestUrl.endsWith(".action") || "/".equals(reuqestUrl)) {
			new Thread(new UpdateTime(
					this.casServerUrlPrefix + "/updateTime?serviceTicket=" + principal.getServiceTicket())).start();
		}
		filterChain.doFilter(request, response);
		return;
	}

	private static int SocketTimeout = 10000;
	private static int ConnectTimeout = 10000;
	private static Boolean SetTimeOut = true;

	private static CloseableHttpClient getHttpClient() {
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
		ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
		registryBuilder.register("http", plainSF);
		Registry<ConnectionSocketFactory> registry = registryBuilder.build();
		// 设置连接管理器
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
		return HttpClientBuilder.create().setConnectionManager(connManager).build();
	}

	class UpdateTime implements Runnable {

		private String url;

		public UpdateTime(String url) {
			this.url = url;
		}

		@Override
		public void run() {
			HttpPost httpGet = new HttpPost(url);
			CloseableHttpResponse res = null;
			try {
				if (SetTimeOut) {
					RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SocketTimeout)
							.setConnectTimeout(ConnectTimeout).build();// 设置请求和传输超时时间
					httpGet.setConfig(requestConfig);
				}
				HttpClientContext context = new HttpClientContext();
				res = AuthenticationFilter.getHttpClient().execute(httpGet, context);
				if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = res.getEntity();
				}
			} catch (ClientProtocolException e1) {
				logger.error(e1);
			} catch (IOException e1) {
				logger.error(e1);
			} catch (Exception e1) {
				logger.error(e1);
			} finally {
				// 关闭连接 ,释放资源
				// client.getConnectionManager().shutdown();
				if (res != null) {
					try {
						res.close();
					} catch (IOException e) {
						logger.error(e);
					}
				}
			}
		}
	}

	public final void setRenew(boolean renew) {
		this.renew = renew;
	}

	public final void setGateway(boolean gateway) {
		this.gateway = gateway;
	}

	public final void setCasServerUrlPrefix(String casServerUrlPrefix) {
		this.casServerUrlPrefix = casServerUrlPrefix;
	}
    /**
     * sessionId,上一次有效操作时间
     */
	static final ConcurrentHashMap<String,Double> SESSIONEFFECTIVEMAP = new ConcurrentHashMap<String,Double>();

	static Properties p = null;
	static InputStream in;

	public AuthenticationFilter() {
		super();
		if (p == null) {
			in = this.getClass().getClassLoader().getResourceAsStream("InvalidAction.properties");
			p = new Properties();
			try {
				p.load(in);
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e);
			}
		}
	}

	public static String getValue(String itemName) {
		return p.getProperty(itemName);
	}

	@SuppressWarnings("rawtypes")
	public static Iterator getKeyIterator() {
		return p.keySet().iterator();
	}
	
	public void putSeesionMap(String sessionId,Double lastTime){
        SESSIONEFFECTIVEMAP.put(sessionId, lastTime);
    }
    public void removeSessionMap(String sessionId){
        SESSIONEFFECTIVEMAP.remove(sessionId);
    }
    public Double getEffectiveWithSessionId(String sessionId){
    	return SESSIONEFFECTIVEMAP.get(sessionId);
    }
}