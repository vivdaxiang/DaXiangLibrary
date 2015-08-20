package com.daxiang.android.http.executor;

import android.content.Context;

/**
 * 
 * @author daxiang
 * 
 *         2015-3-26
 */
public class TaskExecutorConfiguration {

	public int threadPoolSize;

	private TaskExecutorConfiguration(Builder builder) {

		this.threadPoolSize = builder.threadPoolSize;
		// 后续会增加更多配置；
	}

	// ********************************Builder**************************
	public static class Builder {
		private Context mContext;
		private int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2;

		// 后续会增加更多配置；
		public Builder(Context mContext) {
			this.mContext = mContext;
		}

		public Builder threadPoolSize(int threadPoolSize) {

			if (threadPoolSize <= 0) {
				throw new IllegalArgumentException(
						"threadPoolSize must be larger than 0!");
			}

			this.threadPoolSize = threadPoolSize;
			return this;
		}

		public TaskExecutorConfiguration build() {
			return new TaskExecutorConfiguration(this);
		}
	}
}
