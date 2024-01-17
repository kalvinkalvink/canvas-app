package canvas.canvasapp.controller;

import canvas.canvasapp.task.StartupFetchDataTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutionException;

@Slf4j
@Controller
public class StartupController {
	@Autowired
	StartupFetchDataTask startupFetchDataTask;
	public void initApplication(){
		try{
			fetchDataFromCanvas();
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private void fetchDataFromCanvas() throws ExecutionException, InterruptedException {
		log.info("Fetching data from canvas");
		startupFetchDataTask.run();
	}
}
