package com.daxiang.android.http.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author daxiang
 * 
 *         2015-3-26
 */
public class TaskExecutor {

	private static TaskExecutor instance;
	private ExecutorService executorService;

	private TaskExecutor() {

	}

	public static TaskExecutor getInstance() {
		if (instance == null) {
			synchronized (TaskExecutor.class) {
				if (instance == null) {
					instance = new TaskExecutor();
				}
			}
		}
		return instance;
	}

	public void init(TaskExecutorConfiguration configuration) {
		if (executorService == null) {
			executorService = Executors
					.newFixedThreadPool(configuration.threadPoolSize);
		}
		// 后续会增加更多配置；
	}

	public void submit(Runnable task) {

		if (executorService != null) {
			executorService.execute(task);
		} else {
			throw new RuntimeException("TaskExecutor 未初始化!");
		}

	}
}
