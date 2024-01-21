package canvas.canvasapp.task.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
public class FixedThreadPoolExecutor {
	private final int threadPoolSize = 8;
	private ExecutorService executorService;

	public FixedThreadPoolExecutor() {
		this.executorService = Executors.newFixedThreadPool(threadPoolSize);
	}
	public Future<?> submitTask(Runnable task){
		return executorService.submit(task);
	}
	public void executeTask(Runnable task){
		executorService.execute(task);
	}
	public ExecutorCompletionService<Void> getNewExecutorCompleteService(){
		return new ExecutorCompletionService<Void>(executorService);
	}
	public void shutdown(){
		executorService.shutdown();
	}

}
