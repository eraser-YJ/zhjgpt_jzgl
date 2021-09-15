package com.jc.csmp.common.log;

import org.apache.log4j.Logger;

/**
 * @author lc  Administrator
 * @description 记录日志 独角仙
 */
public class Uog {
    private Logger log;

    public static Uog getInstanceOnjob() {
        return new Uog("job");
    }

    public static Uog getInstanceOnMongo() {
        return new Uog("mongo");
    }

    public static Uog getInstanceOnPush() {
        return new Uog("push");
    }

    public static Uog getInstanceOnRule() {
        return new Uog("rule");
    }

    private Uog(String name) {
        log = Logger.getLogger(name);
    }

    public void debug(String message) {
//		System.out.println(message);
        log.debug(message);
    }

    public void debug(String message, Throwable ex) {
//		ex.printStackTrace();
        log.debug(message, ex);
    }

    public void info(String message) {
//		System.out.println(message);
        log.info(message);
    }

    public void info(String message, Throwable ex) {
//		System.out.println(message);
//		ex.printStackTrace();
        log.info(message, ex);
    }

    public void error(String message) {
//		System.out.println(message);
        log.error(message);
    }

    public void error(String message, Throwable ex) {
//		ex.printStackTrace();
        log.error(message, ex);
    }

    public void error(Throwable ex) {
//		ex.printStackTrace();
        log.error(ex.getMessage(), ex);
    }

}
