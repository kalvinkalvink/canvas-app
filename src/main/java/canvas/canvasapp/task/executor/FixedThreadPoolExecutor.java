package canvas.canvasapp.task.executor;

import canvas.canvasapp.exception.task.TaskUncaughtExceptionHandler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Slf4j
@Component
public class FixedThreadPoolExecutor {
	private final int threadPoolSize = 8;
	private final ExecutorService executorService;

	public FixedThreadPoolExecutor() {
		ThreadFactory threadFactory = new ThreadFactoryBuilder()
				.setNameFormat("fixed-thread-pool-%d")
				.setUncaughtExceptionHandler(new TaskUncaughtExceptionHandler())
				.build();
		this.executorService = Executors.newFixedThreadPool(threadPoolSize, threadFactory);
	}

	public <T> Future<T> submitTask(Callable<T> task) {
		return executorService.submit(task);
	}

	public Future<?> submitTask(Runnable task) {
		return executorService.submit(task);
	}

	public void executeTask(Runnable task) {
		executorService.execute(task);
	}

	public ExecutorCompletionService<Void> getNewExecutorCompleteService() {
		return new ExecutorCompletionService<Void>(executorService);
	}

	public void shutdown() {
		executorService.shutdown();
	}

}
