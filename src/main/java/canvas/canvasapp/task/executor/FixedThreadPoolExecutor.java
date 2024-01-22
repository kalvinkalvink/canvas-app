package canvas.canvasapp.task.executor;

import canvas.canvasapp.task.exception.TaskFailedHandler;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Component
public class FixedThreadPoolExecutor {
	private final int threadPoolSize = 8;
	private ExecutorService executorService;

	public FixedThreadPoolExecutor() {
		this.executorService = Executors.newFixedThreadPool(threadPoolSize);
	}
	public Future<?> submitTask(Runnable task){
		return executorService.submit(task);
	}
	public Future<?> submitTask(Task<?> task) {
		task.setOnFailed(new TaskFailedHandler());
		return submitTask((Runnable) task);
	}
	public void executeTask(Runnable task){
		executorService.execute(task);
	}
	public void executeTask(Task<?> task) {
		task.setOnFailed(new TaskFailedHandler());
		this.executeTask((Runnable) task);
	}
	public ExecutorCompletionService<Void> getNewExecutorCompleteService(){
		return new ExecutorCompletionService<Void>(executorService);
	}
	public void shutdown(){
		executorService.shutdown();
	}

}
