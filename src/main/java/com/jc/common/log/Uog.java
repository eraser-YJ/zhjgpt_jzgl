package com.jc.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jc.busi.standard.util.UserContext;

/**
 * @description 记录日志 独角仙
 * @author lc  Administrator
 */
public class Uog {
	private Logger log;

	public static Uog getInstanceOnSoap() {
		return new Uog("soap");
	}

	public static Uog getInstanceOnOper() {
		return new Uog("oper");
	}

	public static Uog getInstanceOnCommon() {
		return new Uog("common");
	}

	private Uog(String name) {
		log = LoggerFactory.getLogger(name);
	}

	public void debug(String message) {
		log.debug(UserContext.getLogProcessId() + " " + message);
	}

	public void debug(String message, Throwable ex) {
		String newMsg = UserContext.getLogProcessId() + " " + message;
		log.debug(newMsg, ex);
	}

	public void info(String message) {
		log.info(UserContext.getLogProcessId() + " " + message);
	}

	public void info(String message, Throwable ex) {
		String newMsg = UserContext.getLogProcessId() + " " + message;
		log.info(newMsg, ex);
	}

	public void error(String message) {
		log.error(UserContext.getLogProcessId() + " " + message);
	}

	public void error(String message, Throwable ex) {
		String newMsg = UserContext.getLogProcessId() + " " + message;
		log.error(newMsg, ex);
	}
}
