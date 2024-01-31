package canvas.canvasapp.controller.view;

import canvas.canvasapp.controller.view.course.CourseController;
import canvas.canvasapp.controller.view.course.CourseListController;
import canvas.canvasapp.event.view.CourseItemClickEvent;
import canvas.canvasapp.model.File;
import canvas.canvasapp.model.Folder;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import canvas.canvasapp.service.database.CourseService;
import canvas.canvasapp.service.database.FileService;
import canvas.canvasapp.service.database.FolderService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller("mainController")
@FxmlView("/view/main.fxml")
public class MainController {
	@Autowired
	private FxWeaver fxWeaver;
	@Autowired
	private CanvasPreferenceService canvasPreferenceService;
	@Autowired
	FolderService folderService;
	@Autowired
	FileService fileService;

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
		showTabContent(TAB.DASHBOARD_TAB);
	}

	@FXML
	public void test(ActionEvent event) {

		List<Folder> all = folderService.findAll();
		System.out.println(all.size());
		all.stream().forEach(System.out::println);
	}
	@FXML
	public void test2(ActionEvent event){
		List<File> all = fileService.findAll();
		System.out.println(all.size());

		all.stream().forEach(System.out::println);
	}

	@FXML
	public void showPreferenceMenu(ActionEvent event) {
		preferenceController.showPreferenceMenu(event);
	}

	private void showTabContent(TAB tab) {
//		if (currentTab != null && tab == currentTab) return;        // stop if current in that tab
		this.currentTab = tab;
		// switch tab button color
		switch (tab) {
			case DASHBOARD_TAB -> {
				dashboardTabButton.setBackground(Background.fill(Paint.valueOf("ffffff")));
			}
		}

		// clear and set content
		ObservableList<Node> contentPaneChildren = this.tabContentStackPane.getChildren();
		contentPaneChildren.clear();
		switch (tab) {
			case DASHBOARD_TAB -> contentPaneChildren.add(fxWeaver.loadView(DashboardController.class));
			case COURSE_TAB -> contentPaneChildren.add(fxWeaver.loadView(CourseController.class));
		}

	}

	@EventListener
	private void courseListItemClickEventListener(CourseItemClickEvent courseItemClickEvent) {
		log.debug("Course list item clicked: {}", courseItemClickEvent.getCourse().getName());
		this.showTabContent(TAB.COURSE_TAB);
	}

	@FXML
	private void showDashboardTab() {
		log.debug("Dashboard tab button clicked");
		showTabContent(TAB.DASHBOARD_TAB);
	}

	@FXML
	private void showCourseTab() {
		log.debug("Course tab button clicked");
		// show course list for selection
		Stage courseListWindow = new Stage();
		courseListWindow.setScene(new Scene((Parent) fxWeaver.load(CourseListController.class).getView().get()));
		courseListWindow.setTitle("Course List");
		courseListWindow.show();
	}

	@FXML
	private void showFilesTab() {
		log.debug("Files tab button clicked");
	}
}
