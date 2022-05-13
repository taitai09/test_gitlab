package omc.spop.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class SpringAsyncConfig {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected Logger errorLogger = LoggerFactory.getLogger("error");
	private final static int CORE_POOL_SIZE = 8;
	private final static int MAX_POOLSIZE = 8;
	private final static int QUEUE_CAPACITY = 8;
	private final static int KEEP_ALIVE_SECONDS = 30;
	private final static String THREAD_NAME_PREFIX = "threadPoolTaskExecutor-";
	
	@Bean(name = "threadPoolTaskExecutor", destroyMethod = "destroy")
	public Executor threadPoolTaskExecutor() {
		/**
		 * Executor를 사용하고자 할 때마다 Config 클래스를 추가해야 한다.
		 * 이유 : core pool size / max pool size / queue capacity 등의 관리를 별도를 관리하지 못하기 때문
		 * Property 파일로 관리할 경우도 동일함. 단, 개수 조절이 가능한 단계
		 * 사용 방법 : @Async("Bean name")을 사용하는 메소드는 기본적으로 void 형을 지원
		 * 리턴 정보가 필요하면 Futher<?> 형으로 정의
		 */
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		
		taskExecutor.setCorePoolSize(CORE_POOL_SIZE);	// 최초 Thread 개수
		taskExecutor.setMaxPoolSize(MAX_POOLSIZE);		// Max Thread 개수
		taskExecutor.setQueueCapacity(QUEUE_CAPACITY);	// 큐의 허용량
		taskExecutor.setThreadNamePrefix(THREAD_NAME_PREFIX);
		taskExecutor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
		taskExecutor.initialize();
		
		return new HandlingExecutor(taskExecutor);
	}
	
	public class HandlingExecutor implements AsyncTaskExecutor {
		private AsyncTaskExecutor executor;
		
		public HandlingExecutor(AsyncTaskExecutor executor) {
			this.executor = executor;
		}
		
		@Override
		public void execute(Runnable task) {						// Thread
			executor.execute(createWrappedRunnable(task));
		}
		
		@Override
		public void execute(Runnable task, long startTimeout) {		// Scheduler
			executor.execute(createWrappedRunnable(task), startTimeout);
		}
		
		@Override
		public Future<?> submit(Runnable task) {
			return executor.submit(createWrappedRunnable(task));
		}
		
		@Override
		public <T> Future<T> submit(final Callable<T> task) {
			return executor.submit(createCallable(task));
		}
		
		private <T> Callable<T> createCallable(final Callable<T> task) {
			return new Callable<T>() {
				@Override
				public T call() throws Exception {
					try {
						return task.call();
					} catch(Exception ex) {
						handle(ex);
						throw ex;
					}
				}
			};
		}
		
		private Runnable createWrappedRunnable(final Runnable task) {
			return new Runnable() {
				@Override
				public void run() {
					try {
						task.run();
					} catch(Exception ex) {
						handle(ex);
					}
				}
			};
		}
		
		private void handle(Exception ex) {
			errorLogger.info("Failed to execute task. : {}", ex.getMessage());
			errorLogger.error("Failed to execute task. ", ex);
		}
		
		public void destroy() {
			if(executor instanceof ThreadPoolTaskExecutor) {
				((ThreadPoolTaskExecutor) executor).shutdown();
			}
		}
	}
}
