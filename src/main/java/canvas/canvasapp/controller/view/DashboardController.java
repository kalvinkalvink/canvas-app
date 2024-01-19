package canvas.canvasapp.controller.view;

import canvas.canvasapp.task.FetchAssignmentsTask;
import edu.ksu.canvas.model.assignment.Assignment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

	private FetchAssignmentsTask fetchAssignmentsTask;

	public DashboardController(FxWeaver fxWeaver) {
		this.fxWeaver = fxWeaver;
	}

	@FXML
	private void initialize() {
		initView();
	}

	private void initView() {
		try {
			if (fetchAssignmentsTask != null && fetchAssignmentsTask.isRunning()) {
				fetchAssignmentsTask.cancel();
			}
			fetchAssignmentsTask = new FetchAssignmentsTask();
			Thread fetchAssignmentThread = new Thread(fetchAssignmentsTask);
			fetchAssignmentThread.start();

			Map<Date, List<Assignment>> dateListMap = fetchAssignmentsTask.get();
			System.out.println(dateListMap);
		} catch (ExecutionException | InterruptedException e) {
			log.error("Failed to fetch and update dashboard assignments", e);
		}
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
