package canvas.canvasapp.task.startup;

import canvas.canvasapp.type.application.AppSetting;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import canvas.canvasapp.task.executor.FixedThreadPoolExecutor;
import canvas.canvasapp.task.executor.ScheduledThreadPoolExecutor;
import canvas.canvasapp.task.executor.SingleThreadPoolExecutor;
import canvas.canvasapp.task.fetch.FetchCourseTask;
import canvas.canvasapp.task.fetch.SyncSelectedCourseFileTask;
import canvas.canvasapp.util.CanvasApi;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
@Scope("prototype")
public class StartupTask implements Runnable {
	// canvas api
	@Autowired
	private CanvasApi canvasApi;
	// thread pool executor
	@Autowired
	private FixedThreadPoolExecutor fixedThreadPoolExecutor;
	@Autowired
	private SingleThreadPoolExecutor singleThreadPoolExecutor;
	@Autowired
	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
	// task
	@Autowired
	private FetchCourseTask fetchCourseTask;
	@Autowired
	private SyncSelectedCourseFileTask syncSelectedCourseFileTask;
	@Autowired
	private CanvasPreferenceService canvasPreferenceService;

	@SneakyThrows
	@Override
	public void run() {
		// wait for canvas api info to init to start task
		while (!canvasApi.isInitialized()) {
			log.info("Waiting for canvas api to init before running startup task");
			Thread.sleep(5000);
		}
		initFixedThreadPoolTask();
		initScheduledThreadPoolTask();
		initSingleThreadPoolTask();
	}

	private void initFixedThreadPoolTask() {
		// only add all the base task
		// e.g. course fetch task will fire CourseFetchFinishEvent to trigger AssignmentFetchTask
		ExecutorCompletionService<Void> executorCompleteService = fixedThreadPoolExecutor.getNewExecutorCompleteService();
		// adding task in order
		ArrayList<Runnable> fetchTaskList = new ArrayList<>();

		// add all task to list for easy management
		fetchTaskList.add(fetchCourseTask);

		// submit task for running
		fetchTaskList.forEach(fetchTask -> {
			executorCompleteService.submit(Executors.callable(fetchTask, null));
		});

		// wait for task to finish
//		for (int i = 0; i < fetchTaskList.size(); i++) {
//			executorCompleteService.take();
//		}
	}

	private Future<?> syncCourseFileTaskFuture;
	private void initScheduledThreadPoolTask() {
		int syncTimeInterval = Integer.parseInt(canvasPreferenceService.get(AppSetting.COURSE_SYNC_INTERVAL, "300"));
		syncCourseFileTaskFuture = scheduledThreadPoolExecutor.scheduleTask(syncSelectedCourseFileTask, 10, syncTimeInterval);// schedule at 5 mins
	}


	private void initSingleThreadPoolTask() {

	}


}
