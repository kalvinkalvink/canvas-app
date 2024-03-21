package canvas.canvasapp.service.application;

import canvas.canvasapp.exception.task.DataFetchTaskNotSupported;
import canvas.canvasapp.task.executor.FixedThreadPoolExecutor;
import canvas.canvasapp.task.fetch.*;
import canvas.canvasapp.type.database.TableFetchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class CanvasDataFetchTaskService {
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private FixedThreadPoolExecutor fixedThreadPoolExecutor;

	public void startFetchTask(TableFetchType tableFetchType) throws DataFetchTaskNotSupported {
		Runnable fetchTask = null;
		switch (tableFetchType) {
			case TableFetchType.ANNOUNCEMENT -> {
				fetchTask = applicationContext.getBean(FetchSelectedCourseAnnouncementTask.class);
			}
			case TableFetchType.ASSIGNMENT -> {
				fetchTask = applicationContext.getBean(FetchSelectedCourseAssignmentTask.class);
			}
			case TableFetchType.COURSE -> {
				fetchTask = applicationContext.getBean(FetchCourseTask.class);
			}
			case TableFetchType.FOLDER -> {
				fetchTask = applicationContext.getBean(FetchSelectedCourseFolderTask.class);
			}
			case TableFetchType.FILE -> {
				fetchTask = applicationContext.getBean(FetchSelectedCourseFileTask.class);
			}
			default -> {
				throw new DataFetchTaskNotSupported("Fetch table not supported", tableFetchType);
			}
		}
		if(Objects.nonNull(fetchTask)){
			fixedThreadPoolExecutor.executeTask(fetchTask);
		}
	}
}
