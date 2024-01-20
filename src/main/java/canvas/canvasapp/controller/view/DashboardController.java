package canvas.canvasapp.controller.view;

import canvas.canvasapp.event.StartupFinishEvent;
import canvas.canvasapp.model.Assignment;
import canvas.canvasapp.task.load.LoadUpcomingAssignmentTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

@Slf4j
@Component
@FxmlView("/view/dashboard.fxml")
public class DashboardController implements IViewController {
	private final FxWeaver fxWeaver;
	@Autowired
	SceneController sceneController;
	@FXML
	private Button courseTabBtn;
	@FXML
	private Button filesTabBtn;
	@FXML
	private VBox assigmentListVBox;

	@Autowired
	private LoadUpcomingAssignmentTask loadUpcomingAssignmentTask;

	public DashboardController(FxWeaver fxWeaver) {
		this.fxWeaver = fxWeaver;
	}

	@FXML
	private void initialize() {
		initView();
	}

	private void initView() {
		log.info("Initializing dashboard view");
		if (loadUpcomingAssignmentTask.isRunning()) {
			loadUpcomingAssignmentTask.cancel();
		}
		try {
			loadUpcomingAssignmentTask.run();
			Map<Date, List<Assignment>> assignmentByDueDate = loadUpcomingAssignmentTask.get();
			assignmentByDueDate.forEach(new BiConsumer<Date, List<Assignment>>() {
				@Override
				public void accept(Date date, List<Assignment> assignments) {
					System.out.printf("------ %s ------------\n", date);
					assignments.forEach(assignment -> System.out.println(assignment.getName()));
				}
			});
		} catch (InterruptedException | ExecutionException e) {
			log.error("Failed to load dashboard assignment",e);
		}
	}

	@EventListener
	public void handleUpdateTask(StartupFinishEvent startupFinishEvent){
		initView();
	}

	@FXML
	private void showCourseTab(ActionEvent event) throws IOException {
		sceneController.showCourseTab();
	}

	@FXML
	private void showFilesTab(ActionEvent event) throws IOException {
	}


	@Override
	public void show() {
		sceneController.showDashboardTab();
	}
}
