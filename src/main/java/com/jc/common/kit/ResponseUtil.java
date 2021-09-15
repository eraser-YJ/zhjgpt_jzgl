package com.jc.common.kit;

import com.jc.foundation.util.JsonUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * 返回值处理
 * 
 * @author lc  liubq
 * @since 2018年9月29日
 */
public class ResponseUtil {
	/**
	 * 返回前台信息
	 * 
	 * @param response
	 * @param success
	 * @param message
	 */
	public static void returnMsg(HttpServletResponse response, Object jsonBean) {
		try {
			response.setHeader("Content-Type", "text/html; charset=utf-8");
			Writer writer = response.getWriter();
			if (jsonBean != null) {
				writer.write(JsonUtil.java2Json(jsonBean));
			} else {
				writer.write("");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
