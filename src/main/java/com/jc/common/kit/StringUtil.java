package com.jc.common.kit;

import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	/**
	 * 注入service
	 * @@param request
	 * @@param bean
	 * @@return
	 */
	public static Object getBean(HttpServletRequest request,String bean) {
		return WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean(bean);
	}
	
	public static Integer toInteger(String s){
		return toInteger(s , null);
	}
	
	public static Integer toInteger(String s , Integer defaultValue){
		if (s == null || "".equals(s.trim()))
			return defaultValue;
		try {
			return Integer.parseInt(s.trim()); 
		}catch(NumberFormatException nfe) {
			return defaultValue; 
		}
	}
	
	/**
	 * 将字符串转为long
	 * @@param s
	 * @@return
	 */
	public static Long toLong(String s){
		return toLong(s , null);
	}
	
	public static Long toLong(String s , Long defaultValue){
		if (s == null || "".equals(s.trim()))
			return defaultValue;
		try {
			return Long.parseLong(s.trim()); 
		}catch(NumberFormatException nfe) {
			return defaultValue; 
		}
	}
	
	public static Float toFloat(String s){
		return toFloat(s , null);
	}
	
	public static Float toFloat(String s , Float defaultValue){
		if (s == null || "".equals(s.trim()))
			return defaultValue;
		try {
			return Float.parseFloat(s.trim()); 
		}catch(NumberFormatException nfe) {
			return defaultValue; 
		}
	}
	
	public static Double toDouble(String s){
		return toDouble(s , null);
	}
	
	public static Double toDouble(String s , Double defaultValue){
		if (s == null || "".equals(s.trim()))
			return defaultValue;
		try {
			return Double.parseDouble(s.trim()); 
		}catch(NumberFormatException nfe) {
			return defaultValue; 
		}
	}
	
	/**
	 * 根据指定字符分割字符串
	 * @@param str
	 * @@param c
	 * @@return
	 */
	public static String[] splitStr(String str , char c){
		if(str == null){
			return null;
		}
		str += c;
		int n = 0;
		for(int i = 0 ; i < str.length() ; i++)	{
			if(str.charAt(i) == c){
				n++;
			}
		}
		String out[] = new String[n];
		for(int i = 0 ; i < n ; i++) {
			int index = str.indexOf(c);
			out[i] = str.substring(0 , index);
			str = str.substring(index + 1 , str.length());
		}
		return out;
	}
	
	/**
	 * 处理模版
	 * @@param value
	 * @@param array
	 * @@return
	 */
	public static String getValue(String value,Object array[]){
		try {
			if(array==null || array.length==0){
				return value;
			}
			String regEx = "\\{\\d+\\}";
			Pattern p = Pattern.compile(regEx);
			Matcher matcher = p.matcher(value);
			StringBuffer sb = new StringBuffer();
			Integer i=0;
			while (matcher.find()) {
			   if(i < array.length ){
				   String arrar_tmp = array[i].toString();
				   if(arrar_tmp == null){
					   arrar_tmp = "";
				   }
				   arrar_tmp = arrar_tmp.replaceAll("$", "");
				   matcher.appendReplacement(sb, arrar_tmp);
				   i++;
			   }				
			}
			matcher.appendTail(sb);
			value = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}    
      	return value;
	}
	
	public static String replaceStr(String source, String oldString, String 
	newString) { if(source == null) return source; StringBuffer output = new 
	StringBuffer();

        int lengthOfSource = source.length(); int lengthOfOld = 
        oldString.length();

        int posStart = 0; int pos;

        String lower_s=source.toLowerCase(); String 
        lower_o=oldString.toLowerCase();

        while ((pos = lower_s.indexOf(lower_o, posStart)) >= 0) { 
        output.append(source.substring(posStart, pos));

            output.append(newString); posStart = pos + lengthOfOld; }

        if (posStart < lengthOfSource) { 
        output.append(source.substring(posStart)); }

        return output.toString(); }
	
	public static String toUTF8(String str){
		try{
			if(str == null)
				str = "";
			else{
				str = str.trim();
				str=new String(str.getBytes("ISO-8859-1"),"utf-8");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
        }
		return str;
    }
	
	public static String getNumShow(Double num){
		return roundString(num , 2);
	}
	
	public static String roundString(double num , int len){
		try{
			String temp="####0";
			temp="####0.";
			for(int i = 0 ; i < len ; i++){
				temp+="0";
			}
			String result = new java.text.DecimalFormat(temp).format(num);
			return result;
		}catch(Exception e){
			return num + "";
		}
	}
	
	public static String toEmpty(Object obj){
		String result = (String)obj;
		if(result == null || result.toLowerCase().equals("null")){
			result = "";
		}
		return result;
	}
	
	public static String UrlEncoder(String url){
    	try {
    		if(url == null) url = "";
			String tmp = URLEncoder.encode(url,"UTF-8");
			return tmp;
		} catch (UnsupportedEncodingException ex) {
			return null;
		}  	
    }
	
	 public static String UrlDecoder(String url){
		 try {
			 if(url != null){
				 return URLDecoder.decode(url,"UTF-8");
			 }else{
	    		return null;
	    	}
	    }catch (UnsupportedEncodingException ex) {
			return null;
		}  	
	}
	 
	 public static Map<String , String> displosePage(String json){
		 json = StringUtil.replaceStr(json, "[" , "");
		 json = StringUtil.replaceStr(json, "]" , "");
		 json = StringUtil.replaceStr(json, "{" , "");
		 json = StringUtil.replaceStr(json, "}" , "");
		 json = StringUtil.replaceStr(json, "\"" , "");
		 String[] array = StringUtil.splitStr(json, ',');
		 Map<String , String> map = new HashMap<String , String>();
		 if(array.length > 0){
			 map.put("property", StringUtil.splitStr(array[0], ':')[1]);
		 }
		 if(array.length > 1){
			 map.put("direction", StringUtil.splitStr(array[1], ':')[1]);
		 }
		 return map;
	 }
	 
	 /**
	  * 2007-7-30 added by changpeng
	  * 将原有的字符串按照需要的长度显示,超出的长度用..代替。
	  * 给定的长度应包含2位..的长度数。
	  *0x3400->13312->'?' 0x9FFF->40959->? 0xF900->63744->?
	  */
	public static String getPointStr(String str,int length){
		if(str == null || "".equals(str)){
			return "";
		}
		if(length <= 0){
			return str;
		}
		if(getStrLength(str) > length){
			str = getOutputPintString(str, length);
		}
		return str;
	}
	
	public static int getStrLength(String str){
		if(str == null || "".equals(str)){
			return 0;
		}
		char[] charArray = str.toCharArray();
		int length = 0;
		int strLength = str.length();
		for(int i = 0; i < charArray.length; i++){
			if(((charArray[i]>=0x3400)&&(charArray[i]<0x9FFF))||(charArray[i]>=0xF900)){
				length += 2;
			}else if(charArray[i]=='&'){
				//&lt;
				if (strLength>(i+3) && charArray[i+1]=='l' && charArray[i+2]=='t' && charArray[i+3]==';'){
					length ++;
					i+=3;
				}
				//&#46;
				if(strLength>(i+4) && charArray[i+1]=='#' && charArray[i+2]=='4' && charArray[i+3]=='6' && charArray[i+4]==';' ){
					length ++;
					i+=4;
				}
				//&nbsp;
				if (strLength>(i+5) && charArray[i+1]=='n' && charArray[i+2]=='b' && charArray[i+3]=='s' && charArray[i+4]=='p' && charArray[i+5]==';'){
					length ++;
					i+=5;
				}
				//&quot;
				if (strLength>(i+5) && charArray[i+1]=='q' && charArray[i+2]=='u' && charArray[i+3]=='o' && charArray[i+4]=='t' && charArray[i+5]==';'){
					length ++;
					i+=5;
				}
				//&acute;
				if (strLength>(i+6) && charArray[i+1]=='a' && charArray[i+2]=='c' && charArray[i+3]=='u' && charArray[i+4]=='t' && charArray[i+5]=='e' && charArray[i+6]==';'){
					length ++;
				}
				//&cedil;
				if (strLength>(i+6) && charArray[i+1]=='c' && charArray[i+2]=='e' && charArray[i+3]=='d' && charArray[i+4]=='i' && charArray[i+5]=='l' && charArray[i+6]==';'){
					length ++;
					i+=6;
				}
			}else {
				length ++;
			}
		}
		return length;
	}
		
	public static String getOutputPintString(String str,int len){
		try {
			if(str == null || str.trim().length() == 0){
				return "";
			}
			byte [] b = str.trim().getBytes();
			if(b.length <= len){
				return str;
			}
			StringBuffer sb = new StringBuffer();
			double sub = 0;
			for (int i = 0; i < str.length(); i++) {
				if(sub >= len-2){
					sb.append("..");
					break;
				}
				String tmp =  str.substring(i, i+1);
				sub += tmp.getBytes().length > 2 ? 2 : tmp.getBytes().length;
				char c = str.charAt(i);
				if(c >=65 && c <= 90){
					sub += 0.5;
				}
				sb.append(tmp);
			}
			return sb.toString();
		} catch (Exception e) {
			return str;
		}
	}
	
	public static void temp() throws Exception {
		StringBuffer param = new StringBuffer();
		URL url = new URL("http://127.0.0.1:8080/talents/pm/pmInfo/manageList.action");
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setRequestMethod("POST");
		urlConn.setConnectTimeout(20000);
		urlConn.setReadTimeout(5000);
		urlConn.setDoOutput(true);
        byte[] pb = param.toString().getBytes();
        urlConn.getOutputStream().write(pb, 0, pb.length);
        urlConn.getOutputStream().flush();
        urlConn.getOutputStream().close();
        InputStream is = urlConn.getInputStream();
        BufferedReader in = null;
		StringBuilder sb = new StringBuilder();
		try {
			in = new BufferedReader(new InputStreamReader(is , Charset.forName("UTF-8")));
			int cp;
			while ((cp = in.read()) != -1) {
				sb.append((char) cp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
		}
		System.out.println(sb.toString());
	}
	
	/**
	 * 去掉左边的0
	 * @param str
	 * @return
	 */
	public static String removeLeftZore(String str){
		if(str == null) return str;
		int len = str.length();
		int index = 0;
		char strs[] = str.toCharArray(); 
		for(int i = 0 ; i < len ; i++){ 
			if('0' != strs[i]){
				index = i;
				break;
			}
		}
		String strLast = str.substring(index, len);
		return strLast;
	}
	
	public static String addLeftZore(String str){
		if(str == null) return str;
		int length = str.length();
 		if(length < 3){
			for(int i = 0 ; i < 3 - length ; i++){
				str = "0" + str;
			}
		}
		return str;
	}
	
	/**
	 * 转换档案ID，去除扫码获得的前面补0的数据
	 * @param oldStr
	 * @return
	 */
	public static String convertFileId(String oldStr) {
		int len = oldStr.length(); // 取长度
		if(len==0){
			return "";
		}
		String newStr = "";
		int i = 0;
		while (oldStr.charAt(i) == '0' && i < len)
			i++; // 找到第一个不是0的位置
		if (i < len) {
			newStr = oldStr.substring(i, len); // 拷贝
		}
		return newStr;
	}
	
	public static String checksetnull(String str){
		if(str == null || str.trim().length() == 0 || str.equals("null")){
			str = null;
		}
		return str;
	}
	
	public static String checkSetDefault(String str , String defaultValue){
		if(StringUtil.isEmpty(str)){
			str = defaultValue;
		}
		return str;
	}
	
	public static Object checkSetDefaultObject(Object value , Object defaultValue){
		if(value == null){
			value = defaultValue;
		}
		return value;
	}
	
	public static boolean isEmpty(String word){
		if(word == null || word.trim().length() == 0){
			return true;
		}
		return false;
	}
	
	public static void makeDir(String path){
		try{
			String tmp[]= StringUtil.splitStr(path,'/');
			path=tmp[0];
			for(int i=1;i<tmp.length;i++){
				path+="/"+tmp[i];
				newFolder(path);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	public static boolean newFolder(String folderPath) {
		  try {
		    String filePath = folderPath;
		    filePath = filePath.toString();
		    File myFilePath = new File(filePath);
		    if (!myFilePath.exists()) {
		      myFilePath.mkdir();
		    }
		    return true;
		  }
		  catch (Exception ex) {
			 ex.printStackTrace();
		    return false;
		  }
		}
	
	public static String toLower(String str){
		if(StringUtil.isEmpty(str)){
			return str;
		}
		return str.toLowerCase();
	}
	
	public static String exec(String cmdLine) {
		String result = null;
        try {
            String[] cmd = new String[3];
            cmd[0] = "cmd.exe";
            cmd[1] = "/C";
            cmd[2] = cmdLine;
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            InputStreamReader ir = new InputStreamReader(proc.getInputStream(),"gbk");
            LineNumberReader input = new LineNumberReader(ir);  
            String line = null;
            boolean ifWright = false;
            while ((line = input.readLine()) != null){
            	System.out.println(line);
            	if(line.indexOf("本地连接") > -1){
            		ifWright = true;
            	}
            	if(ifWright && line.indexOf("IPv4 地址") > -1){
            		String[] array = StringUtil.splitStr(line , ':');
            		if(array != null && array.length == 2){
            			result = array[1].trim();
            		}
            		break;
            	}                
            }
            // any error???
            int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
        }
		return result;
	}
}
