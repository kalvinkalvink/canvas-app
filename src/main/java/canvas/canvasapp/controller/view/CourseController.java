package canvas.canvasapp.controller.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@FxmlView("/view/course.fxml")
public class CourseController implements IViewController {
	@Autowired
	SceneController sceneController;

	private final FxWeaver fxWeaver;

	public CourseController(FxWeaver fxWeaver) {
		this.fxWeaver = fxWeaver;
	}

	@FXML
	void showDashBoardTab(ActionEvent event) throws IOException {
		sceneController.showDashboardTab();
	}

	@FXML
	void showFilesTab(ActionEvent event) throws IOException {

	}

	@Override
	public void show() {
		sceneController.showCourseTab();
	}

}
