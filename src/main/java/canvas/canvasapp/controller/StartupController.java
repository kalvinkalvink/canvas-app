package canvas.canvasapp.controller;

import canvas.canvasapp.event.StartupFinishEvent;
import canvas.canvasapp.task.StartupFetchDataTask;
import javafx.concurrent.WorkerStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutionException;

@Slf4j
@Controller
public class StartupController {
    @Autowired
    StartupFetchDataTask startupFetchDataTask;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    public void initApplication() {
        try {
            fetchDataFromCanvas();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetchDataFromCanvas() throws ExecutionException, InterruptedException {
        log.info("Fetching data from canvas");
        System.out.println("Fetching data from canvas");
        Thread fetchDataTaskThread = new Thread(startupFetchDataTask);
        startupFetchDataTask.setOnSucceeded((WorkerStateEvent event) -> {
            applicationEventPublisher.publishEvent(new StartupFinishEvent(startupFetchDataTask));
        });
        fetchDataTaskThread.start();
    }
}
