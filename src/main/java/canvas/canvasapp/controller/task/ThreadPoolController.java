package canvas.canvasapp.controller.task;

import canvas.canvasapp.task.executor.FileFixedThreadPoolExecutor;
import canvas.canvasapp.task.executor.FixedThreadPoolExecutor;
import canvas.canvasapp.task.executor.ScheduledThreadPoolExecutor;
import canvas.canvasapp.task.executor.SingleThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ThreadPoolController {
	@Autowired
	SingleThreadPoolExecutor singleThreadPoolExecutor;
	@Autowired
	ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
	@Autowired
	FixedThreadPoolExecutor fixedThreadPoolExecutor;
	@Autowired
	FileFixedThreadPoolExecutor fileFixedThreadPoolExecutor;

	public void stopAllThreadPool() {
		singleThreadPoolExecutor.shutdown();
		scheduledThreadPoolExecutor.shutdown();
		fixedThreadPoolExecutor.shutdown();
		fileFixedThreadPoolExecutor.shutdown();
	}
}
