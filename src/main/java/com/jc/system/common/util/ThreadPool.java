package com.jc.system.common.util;

import java.util.concurrent.*;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class ThreadPool {

	private ThreadPool() {
		throw new IllegalStateException("ThreadPool class");
	}
/*
 * 1、固定大小线程池

import java.util.concurrent.Executors;  
import java.util.concurrent.ExecutorService;

ExecutorService pool = Executors.newFixedThreadPool(2);

pool.execute(t1);

pool.shutdown();

2、单任务线程池

ExecutorService pool = Executors.newSingleThreadExecutor();

3、可变尺寸线程池

ExecutorService pool = Executors.newCachedThreadPool();

4、延迟连接池

import java.util.concurrent.Executors;  
import java.util.concurrent.ScheduledExecutorService;  
import java.util.concurrent.TimeUnit;

ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

pool.schedule(t4, 10, TimeUnit.MILLISECONDS);

5、单任务延迟连接池

ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
 * 
 * */
static ExecutorService pool = null;
	public static ExecutorService getThreadPool() {
		//构造一个线程池
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
				0,Integer.MAX_VALUE,60L,TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(1),
				new ThreadPoolExecutor.DiscardOldestPolicy());
		if(ThreadPool.pool == null) {
			pool = threadPool;
		}
		return pool;
	}
}
