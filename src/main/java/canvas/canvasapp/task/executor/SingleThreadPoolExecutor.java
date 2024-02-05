package canvas.canvasapp.task.executor;

import canvas.canvasapp.exception.task.TaskUncaughtExceptionHandler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class SingleThreadPoolExecutor {
	private final ExecutorService executorService;

	public SingleThreadPoolExecutor() {
		ThreadFactory threadFactory = new ThreadFactoryBuilder()
				.setNameFormat("single-thread-pool-%d")
				.setUncaughtExceptionHandler(new TaskUncaughtExceptionHandler())
				.build();
		this.executorService = Executors.newSingleThreadExecutor(threadFactory);

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
