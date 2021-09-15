package com.jc.system.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class Utils {

	private Utils() {
	    throw new IllegalStateException("Utility class");
	}
	
	protected static final Logger log = Logger.getLogger(Utils.class);
	
	private static final String UNKNOWN = "unknown";
	
	/**
	 * 获取IP
	 */
	public static String getIpAddr(HttpServletRequest request) {  
		String ip = request.getHeader("x-forwarded-for");  
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {  
		    ip = request.getHeader("Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {  
		    ip = request.getHeader("WL-Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {  
		    ip = request.getRemoteAddr();  
		}  
		return ip; 
    }
	
	/**
	 * 获取服务器IP
	 */
	public static String getRemoteIp() {  
	    String result = "";
		Enumeration<NetworkInterface> netInterfaces = null;  
	    try {  
	        netInterfaces = NetworkInterface.getNetworkInterfaces();  
	        while (netInterfaces.hasMoreElements()) {  
	            NetworkInterface ni = netInterfaces.nextElement();  
	            if(ni.getDisplayName().toLowerCase().indexOf("vmware")!=-1||
	            		ni.getDisplayName().toLowerCase().indexOf("内核调试")!=-1||
	            			ni.getDisplayName().toLowerCase().indexOf("loopback")!=-1){
	            	continue;
	            }
	            Enumeration<InetAddress> ips = ni.getInetAddresses();  
	            while (ips.hasMoreElements()) { 
	            	String ipStr = ips.nextElement().getHostAddress();
	            	if(ipStr.indexOf('.')!=-1){
	            		result = ipStr;
	            		break;
	            	}
	            }  
	        }  
	    } catch (Exception e) {  
	        log.error(e);
	    }
	    return result;
    }
	
	/**
	 * 判断IP在指定范围内
	 */
	public static boolean ipExistsInRange(String ip, String ipSection) {
		ipSection = ipSection.trim();
		ip = ip.trim();
		int idx = ipSection.indexOf('-');
		String beginIP = ipSection.substring(0, idx);
		String endIP = ipSection.substring(idx + 1);
		return getIp2long(beginIP) <= getIp2long(ip)&& getIp2long(ip) <= getIp2long(endIP);
	}

	private static long getIp2long(String ip) {
		ip = ip.trim();
		String[] ips = ip.split("\\.");
		long ip2long = 0L;
		for (int i = 0; i < 4; ++i) {
			ip2long = ip2long << 8 | Integer.parseInt(ips[i]);
		}
		return ip2long;
	}

	/**
	 * 判断IP在指定范围内end
	 */
}
