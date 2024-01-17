package canvas.canvasapp.controller.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.stereotype.Controller;

import java.io.IOException;
@Controller
public class CourseController {

	@FXML
	protected void showDashBoardTab(ActionEvent event) throws IOException {
		SceneController.showDashboardScene(event);
	}
	@FXML
	protected void showFilesTab(ActionEvent event) throws IOException {
		SceneController.showFilesScene(event);
	}

}
