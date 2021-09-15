package com.jc.busi.standard.service.filter;

import java.io.OutputStream;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import com.jc.common.log.Uog;

/**
 * @author lc  Administrator @
 */
public class OutInterceptor extends AbstractPhaseInterceptor<Message> {

	// 日志
	private Uog log = Uog.getInstanceOnSoap();

	private int limit = 902400;

	public OutInterceptor() {
		// 拦截器在调用方法之前拦截SOAP消息
		super(Phase.PRE_STREAM);
	}

	public void handleMessage(Message message) throws Fault {
		OutputStream os = (OutputStream) message.getContent(OutputStream.class);
		if (os == null) {
			return;
		}
		CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(os);
		message.setContent(OutputStream.class, newOut);
		newOut.registerCallback(new LoggingCallback(message, os));
	}

	// 出现错误输出错误信息和栈信息
	public void handleFault(Message message) {
		Exception exeption = message.getContent(Exception.class);
		log.error("OutInterceptor:" + exeption.getMessage(), exeption);
	}

	class LoggingCallback implements CachedOutputStreamCallback {
		private final Message message;
		private final OutputStream origStream;

		public LoggingCallback(Message msg, OutputStream os) {
			this.message = msg;
			this.origStream = os;
		}

		public void onFlush(CachedOutputStream cos) {
		}

		public void onClose(CachedOutputStream cos) {
			StringBuilder xml = new StringBuilder();
			try {
				cos.writeCacheTo(xml, OutInterceptor.this.limit);
			} catch (Exception ex) {
			}
			// 打印日志，保存日志保存这里就可以，可写库或log4j记录日志
			log.debug("结果xml：" + xml);
			try {
				cos.lockOutputStream();
				cos.resetOut(null, false);
			} catch (Exception ex) {
			}
			this.message.setContent(OutputStream.class, this.origStream);
		}
	}

}