package com.canmeizhexue.common.utils.executors;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理者
 * @author silence
 */
public class ThreadExecutor {

	private final static String TAG = ThreadExecutor.class.getSimpleName();
	/**Basic线程池线程数*/
	private final static int BASIC_THREAD_COUNT = 2;
	private static final Executor BASIC_EXECUTOR = Executors.newFixedThreadPool(BASIC_THREAD_COUNT,new LowPriorityThreadFactory());
	private static final Executor HIGHT_EXECUTOR = new ThreadPoolExecutor(3, 5, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	
	private ThreadExecutor(){
	}

	/**
	 * 执行普通的异步操作(优先级比正常稍低)
	 */
	public static void execute(Runnable command){
		BASIC_EXECUTOR.execute(command);
	}
	/**
	 * 执行普通的异步操作(优先级正常)
	 */
	public static void run(Runnable command){
		HIGHT_EXECUTOR.execute(command);
	}
	
	private static class LowPriorityThreadFactory implements ThreadFactory {
	    public Thread newThread(Runnable r) {
	       Thread t = new Thread(r);
	       t.setPriority(Thread.NORM_PRIORITY-1);
	       return t;
	    }
	}
	public static Executor getExecutor(){
		return HIGHT_EXECUTOR;
	}
}