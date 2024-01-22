package canvas.canvasapp.task.startup;

import canvas.canvasapp.task.exception.TaskFailedHandler;
import canvas.canvasapp.task.executor.FixedThreadPoolExecutor;
import canvas.canvasapp.task.fetch.FetchCourseTask;
import canvas.canvasapp.task.fetch.FetchSelectedAssignmentTask;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
@Scope("prototype")
public class StartupFetchTask extends Task<Void> {
	@Autowired
	FetchCourseTask fetchCourseTask;
	@Autowired
	FetchSelectedAssignmentTask fetchSelectedAssignmentTask;
	@Autowired
	FixedThreadPoolExecutor fixedThreadPoolExecutor;
	@Override
	protected Void call() throws InterruptedException {
		// only add all the base task
		// e.g. course fetch task will fire CourseFetchFinishEvent to trigger AssignmentFetchTask
		ExecutorCompletionService<Void> executorCompleteService = fixedThreadPoolExecutor.getNewExecutorCompleteService();
		// adding task in order
		ArrayList<Task<Void>> fetchTaskList = new ArrayList<>();

		// set task failed handler
		fetchCourseTask.setOnFailed(new TaskFailedHandler());

		// add all task to list for easy management
		fetchTaskList.add(fetchCourseTask);

		// submit task for running
		fetchTaskList.forEach(fetchTask->{
			executorCompleteService.submit(Executors.callable(fetchTask, null));
		});

		// wait for task to finish
//		for (int i = 0; i < fetchTaskList.size(); i++) {
//			executorCompleteService.take();
//		}
		return null;
	}
}
