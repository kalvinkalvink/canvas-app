package canvas.canvasapp.task.executor;

import canvas.canvasapp.exception.task.TaskUncaughtExceptionHandler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class ScheduledThreadPoolExecutor {
	private final int threadPoolSize = 20;
	private final ScheduledExecutorService executorService;

	public ScheduledThreadPoolExecutor() {
		ThreadFactory threadFactory = new ThreadFactoryBuilder()
				.setNameFormat("scheduled-thread-pool-%d")
				.setUncaughtExceptionHandler(new TaskUncaughtExceptionHandler())
				.build();
		executorService = Executors.newScheduledThreadPool(threadPoolSize,threadFactory);
	}

	public Future<?> scheduleTask(Runnable task, int initDelay, int period) {
		return executorService.scheduleAtFixedRate(task, initDelay, period, TimeUnit.SECONDS);
	}




	public ExecutorCompletionService<Void> getNewExecutorCompleteService() {
		return new ExecutorCompletionService<Void>(executorService);
	}

	public void shutdown() {
		executorService.shutdown();
	}
}
