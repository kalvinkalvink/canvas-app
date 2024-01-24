package canvas.canvasapp.controller;

import canvas.canvasapp.task.executor.FixedThreadPoolExecutor;
import canvas.canvasapp.task.startup.StartupTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutionException;

@Slf4j
@Controller
public class StartupController {
    @Autowired
	StartupTask startupTask;
	@Autowired
	FixedThreadPoolExecutor fixedThreadPoolExecutor;
    public void initApplication() {
        try {
            fetchDataFromCanvas();
        } catch (ExecutionException | InterruptedException e) {
            log.error("Failed to init application", e);
        }
	}

    private void fetchDataFromCanvas() throws ExecutionException, InterruptedException {
		fixedThreadPoolExecutor.executeTask(startupTask);
    }
}
