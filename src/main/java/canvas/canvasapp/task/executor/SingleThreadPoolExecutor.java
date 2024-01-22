package canvas.canvasapp.task.executor;

import canvas.canvasapp.task.exception.TaskFailedHandler;
import javafx.concurrent.Task;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class SingleThreadPoolExecutor {
	private ExecutorService executorService;

	public SingleThreadPoolExecutor() {
		this.executorService = Executors.newSingleThreadExecutor();
	}

	public Future<?> submitTask(Runnable task) {
		return executorService.submit(task);
	}

	public Future<?> submitTask(Task<?> task) {
		task.setOnFailed(new TaskFailedHandler());
		return submitTask((Runnable) task);
	}

	public void executeTask(Runnable task) {
		executorService.execute(task);
	}

	public void executeTask(Task<?> task) {
		task.setOnFailed(new TaskFailedHandler());
		this.executeTask((Runnable) task);
	}

	public ExecutorCompletionService<Void> getNewExecutorCompleteService() {
		return new ExecutorCompletionService<Void>(executorService);
	}

	public void shutdown() {
		executorService.shutdown();
	}
}
