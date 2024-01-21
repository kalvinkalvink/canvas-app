package canvas.canvasapp.task.executor;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SingleThreadPoolExecutor {
	private ExecutorService executorService;
	public SingleThreadPoolExecutor(){
		this.executorService = Executors.newSingleThreadExecutor();
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
