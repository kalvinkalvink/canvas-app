package canvas.canvasapp.controller.view;

import canvas.canvasapp.controller.view.course.AnnouncementController;
import canvas.canvasapp.controller.view.course.AssignmentController;
import canvas.canvasapp.controller.view.course.FilesController;
import canvas.canvasapp.controller.view.course.GradeController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@FxmlView("/view/component/tab/course.fxml")
public class CourseController {

	enum CoursePage {
		ANNOUNCEMENT,
		ASSIGNMENT,
		GRADE,
		FILE
	}

	private CoursePage currentPage;

	@Autowired
	SceneController sceneController;
	@FXML
	Button announcementButton;
	@FXML
	Button assignmentButton;
	@FXML
	Button gradeButton;
	@FXML
	Button fileButton;
	@FXML
	StackPane courseContentStackPane;
	private final FxWeaver fxWeaver;

	public CourseController(FxWeaver fxWeaver) {
		this.fxWeaver = fxWeaver;
	}

	@FXML
	private void initialize() {
		// set announcement page to content pane
		this.showPage(CoursePage.ANNOUNCEMENT);
	}

	private void showPage(CoursePage coursePage) {
		// skip if current page is same as the page needed to show
		if (coursePage != null && currentPage == coursePage) return;
		currentPage = coursePage;
		// set button border when selected

		// setting page to stackpane
		ObservableList<Node> stackPaneChildren = courseContentStackPane.getChildren();
		stackPaneChildren.clear();
		switch (coursePage) {
			case ANNOUNCEMENT -> stackPaneChildren.add(fxWeaver.loadView(AnnouncementController.class));
			case ASSIGNMENT -> stackPaneChildren.add(fxWeaver.loadView(AssignmentController.class));
			case FILE -> stackPaneChildren.add(fxWeaver.loadView(FilesController.class));
			case GRADE -> stackPaneChildren.add(fxWeaver.loadView(GradeController.class));
		}
	}

}
