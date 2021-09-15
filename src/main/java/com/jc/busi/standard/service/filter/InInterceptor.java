package com.jc.busi.standard.service.filter;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import com.jc.busi.standard.util.UserContext;
import com.jc.common.log.Uog;

/**
 * @description 入口拦截
 * @author lc  Administrator
 */
public class InInterceptor extends AbstractPhaseInterceptor<Message> {
	// 日志
	private Uog log = Uog.getInstanceOnSoap();

	private int limit = 921600;

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public InInterceptor() {
		// 拦截器在调用方法之前拦截SOAP消息
		super(Phase.RECEIVE);
	}

	public void handleMessage(Message message) throws Fault {
		logging(message);
	}

	private void logging(Message message) throws Fault {
		// 初始化
		UserContext.init();
		HttpServletRequest httprequest = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
		String ip = httprequest != null ? httprequest.getRemoteAddr() : null;
		log.error("客户端IP：" + ip + "的日志记录号：" + UserContext.getLogProcessId());
		// 拦截
		StringBuilder xml = new StringBuilder();
		InputStream is = (InputStream) message.getContent(InputStream.class);
		if (is != null) {
			CachedOutputStream bos = new CachedOutputStream();
			try {
				IOUtils.copy(is, bos);
				bos.flush();
				is.close();
				message.setContent(InputStream.class, bos.getInputStream());
				if (bos.size() > this.limit) {
					throw new Exception("长度超出限制");
				}
				bos.writeCacheTo(xml, this.limit);

				// 打印日志，保存日志保存这里就可以，可写库或log4j记录日志
				log.info("接收到的xml：" + xml);
				UserContext.setXML(xml.toString());
			} catch (Exception e) {
				log.error("InInterceptor异常：", e);
				throw new Fault(e);
			} finally {
				if (bos != null) {
					try {
						bos.close();
					} catch (Exception e) {
						throw new Fault(e);
					}
				}
			}

		}

	}

	// 出现错误输出错误信息和栈信息
	public void handleFault(Message message) {
		Exception exeption = message.getContent(Exception.class);
		log.error("InInterceptor:" + exeption.getMessage(), exeption);
	}

}