package canvas.canvasapp.controller.view;

import canvas.canvasapp.task.FetchAssignmentsTask;
import edu.ksu.canvas.model.assignment.Assignment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
public class DashboardController {

	@FXML
	private Button courseTabBtn;
	@FXML
	private Button filesTabBtn;
	@FXML
	private VBox assigmentListVBox;

	private FetchAssignmentsTask fetchAssignmentsTask;
	@FXML
	private void initialize() {
		try{
			if(fetchAssignmentsTask != null && fetchAssignmentsTask.isRunning()){
				fetchAssignmentsTask.cancel();
			}
			fetchAssignmentsTask = new FetchAssignmentsTask();
			fetchAssignmentsTask.run();
			Map<Date, List<Assignment>> dateListMap = fetchAssignmentsTask.get();
			System.out.println("nice");
			System.out.println(dateListMap);
		} catch (ExecutionException | InterruptedException e) {
			log.error("Failed to fetch and update dashboard assignments" , e);
		}
	}



	@FXML
	private void showCourseTab(ActionEvent event) throws IOException {
		SceneController.showCourseScene(event);
	}

	@FXML
	private void showFilesTab(ActionEvent event) throws IOException {
		SceneController.showFilesScene(event);
	}
}
