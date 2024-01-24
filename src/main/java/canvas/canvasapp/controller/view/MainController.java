package canvas.canvasapp.controller.view;

import canvas.canvasapp.model.Course;
import canvas.canvasapp.service.CourseService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller("mainController")
@FxmlView("/view/main.fxml")
public class MainController {
	@Autowired
	private FxWeaver fxWeaver;

	// tab menu
	enum TAB {
		DASHBOARD_TAB,
		COURSE_TAB,
		FILES_TAB
	}

	@FXML
	private Button dashboardTabButton;
	@FXML
	private Button courseTabBtn;
	@FXML
	private Button filesTabBtn;
	private TAB currentTab;

	@Autowired
	PreferenceController preferenceController;
	@Autowired
	CourseService courseService;

	@FXML
	private StackPane tabContentStackPane;

	@FXML
	private void initialize() {
		log.info("Main Controller initializing");
		HBox.setHgrow(tabContentStackPane, Priority.ALWAYS);
		showTab(TAB.DASHBOARD_TAB);
	}

	@FXML
	public void test(ActionEvent event) {
//		ListProperty<String> courseSelections = preferenceController.getCourseSelections();
//		courseSelections.forEach(course->{
//			System.out.println(course);
//		});
//		courseRepository.findAll().stream().filter(Course::getSelected).forEach(System.out::println);
		List<Course> bySelectedIsTrue = courseService.getAllSelected();
		bySelectedIsTrue.forEach(course -> System.out.println(course.getColor()));
	}

	@FXML
	public void showPreferenceMenu(ActionEvent event) {
		preferenceController.showPreferenceMenu(event);
	}

	private void showTab(TAB tab) {
		if (currentTab != null && tab == currentTab) return;		// stop if current in that tab
		this.currentTab = tab;
		// clear and set content


		ObservableList<Node> contentPaneChildren = this.tabContentStackPane.getChildren();
		contentPaneChildren.clear();
		switch (tab) {
			case DASHBOARD_TAB -> contentPaneChildren.add(fxWeaver.loadView(DashboardController.class));
			case COURSE_TAB -> contentPaneChildren.add(fxWeaver.loadView(CourseController.class));
		}

	}


	@FXML
	private void showDashboardTab() {
		log.debug("Dashboard tab button clicked");
		showTab(TAB.DASHBOARD_TAB);
	}

	@FXML
	private void showCourseTab() {
		log.debug("Course tab button clicked");
		showTab(TAB.COURSE_TAB);
	}

	@FXML
	private void showFilesTab() {
		log.debug("Files tab button clicked");
	}
}
