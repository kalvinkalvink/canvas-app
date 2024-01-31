package canvas.canvasapp.task.executor;

import canvas.canvasapp.exception.task.TaskUncaughtExceptionHandler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

@Slf4j
@Component
public class FileFixedThreadPoolExecutor {
	private final int threadPoolSize = 4;
	private ExecutorService executorService;

	public FileFixedThreadPoolExecutor() {
		ThreadFactory threadFactory = new ThreadFactoryBuilder()
				.setNameFormat("fixed-thread-pool-%d")
				.setUncaughtExceptionHandler(new TaskUncaughtExceptionHandler())
				.build();
		this.executorService = Executors.newFixedThreadPool(threadPoolSize,threadFactory);
	}

	public Future<?> submitTask(Runnable task){
		return executorService.submit(task);
	}
	public void executeTask(Runnable task){
		executorService.execute(task);
	}

	public void shutdown(){
		executorService.shutdown();
	}

}
