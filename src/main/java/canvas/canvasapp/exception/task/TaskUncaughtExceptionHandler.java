package canvas.canvasapp.exception.task;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		log.error("Uncaught exception in thread: {}", t.getName());
		e.printStackTrace();

	}
}
