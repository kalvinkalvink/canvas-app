package canvas.canvasapp.event.listener;

import canvas.canvasapp.task.executor.FixedThreadPoolExecutor;
import canvas.canvasapp.task.executor.ScheduledThreadPoolExecutor;
import canvas.canvasapp.task.executor.SingleThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationListenerBase {
	@Autowired
	protected FixedThreadPoolExecutor fixedThreadPoolExecutor;
	@Autowired
	protected SingleThreadPoolExecutor singleThreadPoolExecutor;
	@Autowired
	protected ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
}
