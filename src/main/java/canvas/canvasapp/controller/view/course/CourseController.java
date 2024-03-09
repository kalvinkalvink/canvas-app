package canvas.canvasapp.controller.view.course;

import canvas.canvasapp.controller.view.course.announcement.CourseAnnouncementController;
import canvas.canvasapp.controller.view.course.assignment.CourseAssignmentController;
import canvas.canvasapp.controller.view.course.file.CourseFilesController;
import canvas.canvasapp.controller.view.course.grade.CourseGradeController;
import canvas.canvasapp.type.view.course.CoursePage;
import canvas.canvasapp.model.db.Course;
import canvas.canvasapp.service.database.CourseService;
import canvas.canvasapp.service.view.course.CourseViewService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@FxmlView("/view/component/tab/course.fxml")
public class CourseController {
	private final CoursePage currentPage = null;

	@Autowired
	CourseViewService courseViewService;
	@Autowired
	CourseService courseService;
	@FXML
	Text courseNameText;
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
		initButtonEvent();
		Course course = courseViewService.getCourse();
		log.info("Initializing course view");
		this.courseNameText.setText(course.getName());
//		System.out.println(currentCourse);
		// set announcement page to content pane
		this.showPage(CoursePage.ANNOUNCEMENT);
	}
	private void initButtonEvent(){
		announcementButton.setOnAction(event -> showPage(CoursePage.ANNOUNCEMENT));
		assignmentButton.setOnAction(event -> showPage(CoursePage.ASSIGNMENT));
		fileButton.setOnAction(event -> showPage(CoursePage.FILE));
		gradeButton.setOnAction(event -> showPage(CoursePage.GRADE));
	}

	private void showPage(CoursePage coursePage) {
		// set button border when selected

		// setting page to stackpane
		ObservableList<Node> stackPaneChildren = courseContentStackPane.getChildren();
		stackPaneChildren.clear();
		FxControllerAndView<CourseAnnouncementController, Node> fxControllerAndView = null;
		switch (coursePage) {
			case ANNOUNCEMENT -> stackPaneChildren.add(fxWeaver.loadView(CourseAnnouncementController.class));
			case ASSIGNMENT -> stackPaneChildren.add(fxWeaver.loadView(CourseAssignmentController.class));
			case FILE -> stackPaneChildren.add(fxWeaver.loadView(CourseFilesController.class));
			case GRADE -> stackPaneChildren.add(fxWeaver.loadView(CourseGradeController.class));
		}
	}
}
