package canvas.canvasapp.util;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Service
public class TaskManager {
	private ExecutorService executor;
	private List<Runnable> tasks;

	public TaskManager() {
		executor = Executors.newFixedThreadPool(5);
		tasks = new ArrayList<>();
	}

	public void submitTask(Runnable task) {
		tasks.add(task);
		executor.submit(task);
	}

	public List<Runnable> getRunningTasks() {
		return new ArrayList<>(tasks);
	}

	public void cancelTask(Runnable task) {
		if (tasks.contains(task)) {
			tasks.remove(task);
			// Attempt to cancel the task if it is still running
			if (executor != null && !executor.isShutdown()) {
				executor.shutdownNow();
			}
		}
	}

	public void shutdown() {
		if (executor != null && !executor.isShutdown()) {
			executor.shutdownNow();
		}
	}
}