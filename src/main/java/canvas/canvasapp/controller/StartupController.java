package canvas.canvasapp.controller;

import canvas.canvasapp.event.StartupFinishEvent;
import canvas.canvasapp.task.exception.TaskFailedHandler;
import canvas.canvasapp.task.executor.FixedThreadPoolExecutor;
import canvas.canvasapp.task.startup.StartupFetchTask;
import javafx.concurrent.WorkerStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;

@Slf4j
@Controller
public class StartupController {
    @Autowired
	StartupFetchTask startupFetchTask;
	@Autowired
	FixedThreadPoolExecutor fixedThreadPoolExecutor;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    public void initApplication() {
        try {
            fetchDataFromCanvas();
        } catch (ExecutionException | InterruptedException e) {
            log.error("Failed to init application", e);
        }
	}

    private void fetchDataFromCanvas() throws ExecutionException, InterruptedException {
        log.info("Fetching data from canvas");

//		startupFetchTask.setOnSucceeded((WorkerStateEvent event)->{
//			applicationEventPublisher.publishEvent(new StartupFinishEvent(startupFetchTask));
//		});
		fixedThreadPoolExecutor.executeTask(startupFetchTask);
    }
}
