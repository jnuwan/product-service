package com.microservice.product_service.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

	//Async task executor for handle the order
	@Bean("orderAsyncTaskExecutor")
	public Executor asyncTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setQueueCapacity(100);
		executor.setMaxPoolSize(5);
		executor.setThreadNamePrefix("OrderAsyncTaskExecutor -");
		executor.initialize();
		return executor;
	}

}
