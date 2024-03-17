package canvas.canvasapp.controller.view;

import canvas.canvasapp.CanvasApp;
import canvas.canvasapp.controller.view.course.CourseController;
import canvas.canvasapp.controller.view.course.CourseListController;
import canvas.canvasapp.controller.view.guide.UserGuideController;
import canvas.canvasapp.event.view.CourseItemClickEvent;
import canvas.canvasapp.lib.document.PptxToPDFConverter;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import canvas.canvasapp.service.database.FileService;
import canvas.canvasapp.service.database.FolderService;
import canvas.canvasapp.type.application.AppSetting;
import canvas.canvasapp.type.application.TAB;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;

@Slf4j
@Controller("mainController")
@FxmlView("/view/main.fxml")
public class MainController {
	@Autowired
	private FxWeaver fxWeaver;
	@Autowired
	FolderService folderService;
	@Autowired
	FileService fileService;

	// tab menu
	private TAB currentTab;

	@FXML
	private VBox tabButtonVBox;

	@Autowired
	private PreferenceController preferenceController;
	@Autowired
	private CanvasPreferenceService canvasPreferenceService;

	@FXML
	private StackPane tabContentStackPane;

	@FXML
	private void initialize() {
		log.info("Main Controller initializing");
		showTabContent(TAB.DASHBOARD_TAB);
	}

	@FXML
	public void test(ActionEvent event) throws Exception {
//		Toast.toast(ToastType.INFO, "Information", "Here is some information you cannot do without.");

	}

	@FXML
	public void test2(ActionEvent event) throws Exception {
		String pptName = "C:\\Users\\kalvinkalvink\\Downloads\\canvas-course-sync\\CS4486 Artificial Intelligence\\Lectures\\lecture01.pptx";
		String pdfName = "C:\\Users\\kalvinkalvink\\Downloads\\canvas-course-sync\\CS4486 Artificial Intelligence\\Lectures\\lecture01.pdf";
//		pdfDocumentService.replaceText("C:\\Users\\kalvinkalvink\\Downloads\\canvas-course-sync\\CS4486 Artificial Intelligence\\Lectures\\lecture03.pdf", "Evaluation Warning", "1123");

		PptxToPDFConverter pptxToPDFConverter = new PptxToPDFConverter(new FileInputStream(pptName), new FileOutputStream(pdfName), true, true);
		pptxToPDFConverter.convert();
	}

	@FXML
	public void showPreferenceMenu(ActionEvent event) {
		preferenceController.showPreferenceMenu(event);
	}

	private void showTabContent(TAB tab) {
		this.currentTab = tab;
		// switch tab button color
		ObservableList<Node> tabVBoxChildren = tabButtonVBox.getChildren();
		tabVBoxChildren.forEach(buttonNode -> {
			Button tabButton = (Button) buttonNode;
			tabButton.getStyleClass().remove("tab-button-selected");
			tabButton.getStyleClass().remove("tab-button");

			if (tabButton.getText().equals(tab.getName()))
				tabButton.getStyleClass().add("tab-button-selected");
			else
				tabButton.getStyleClass().add("tab-button");
		});

		// clear and set content
		ObservableList<Node> contentPaneChildren = this.tabContentStackPane.getChildren();
		contentPaneChildren.clear();
		switch (tab) {
			case DASHBOARD_TAB -> contentPaneChildren.add(fxWeaver.loadView(DashboardController.class));
			case COURSE_TAB -> contentPaneChildren.add(fxWeaver.loadView(CourseController.class));
			case FILES_TAB -> contentPaneChildren.add(fxWeaver.loadView(FileController.class));
		}

	}

	@EventListener
	private void courseListItemClickEventListener(CourseItemClickEvent courseItemClickEvent) {
		log.debug("Course list item clicked: {}", courseItemClickEvent.getCourse().getName());
		showTabContent(TAB.COURSE_TAB);
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
		Stage courseListStage = new Stage();
		courseListStage.setTitle("Course List");
		courseListStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
				if (!newValue)
					courseListStage.close();
			}
		});
		courseListStage.setScene(new Scene((Parent) fxWeaver.load(CourseListController.class).getView().get()));
		courseListStage.show();
	}

	@FXML
	private void showFilesTab() {
		log.debug("Files tab button clicked");
		showTabContent(TAB.FILES_TAB);
	}

	@FXML
	public void userGuideButtonClicked(ActionEvent event) {
		Stage userGuideStage = new Stage();
		Node userGuideNode = fxWeaver.loadView(UserGuideController.class);
		userGuideStage.setScene(new Scene((Parent) userGuideNode));
		userGuideStage.show();
	}

	@FXML
	public void canvasLogoClicked(MouseEvent mouseEvent) {
		// open canvas website
		String canvasBaseUrl = canvasPreferenceService.get(AppSetting.CANVAS_BASE_URL, "");
		if (!canvasBaseUrl.isEmpty())
			CanvasApp.hostServices.showDocument(canvasBaseUrl);
	}
}
