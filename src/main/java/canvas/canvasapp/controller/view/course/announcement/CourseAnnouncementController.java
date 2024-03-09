package canvas.canvasapp.controller.view.course.announcement;

import canvas.canvasapp.model.db.Announcement;
import canvas.canvasapp.model.db.Course;
import canvas.canvasapp.service.database.AnnouncementService;
import canvas.canvasapp.service.view.course.CourseViewService;
import canvas.canvasapp.util.CanvasApi;
import edu.ksu.canvas.interfaces.AnnouncementReader;
import edu.ksu.canvas.requestOptions.ListCourseAnnouncementOptions;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@FxmlView("/view/component/course/announcement.fxml")
public class CourseAnnouncementController {
	@Autowired
	private CourseViewService courseViewService;
	@Autowired
	private AnnouncementService announcementService;
	@Autowired
	private FxWeaver fxWeaver;
	@Autowired
	private CanvasApi canvasApi;
	@FXML
	private ListView<Node> announcementListView;

	@FXML
	private void initialize() {
		Course course = courseViewService.getCourse();
		log.info("Initializing announcement with course: {}", course.getName());
		List<Announcement> selectedCourseAnnouncementList = announcementService.findAllByCourseId(course.getId());
		// constructing announcement card
		ObservableList announcementListViewItems = announcementListView.getItems();
		selectedCourseAnnouncementList.stream()
				.forEach(announcement -> {
					FxControllerAndView<CourseAnnouncementCardController, Node> announcementCardControllerNodeFxControllerAndView = fxWeaver.load(CourseAnnouncementCardController.class);
					Optional<Node> announcementCardViewOptional = announcementCardControllerNodeFxControllerAndView.getView();
					// setting variable
					CourseAnnouncementCardController courseAnnouncementCardController = announcementCardControllerNodeFxControllerAndView.getController();
					courseAnnouncementCardController.setAnnouncementTitleLabel(announcement.getTitle());
					courseAnnouncementCardController.setPostedOnText(announcement.getPostedAt());
					// adding card view to listview
					announcementCardViewOptional.ifPresent(announcementCardView -> {
						announcementCardView.setOnMouseClicked(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent mouseEvent) {
								if (mouseEvent.getClickCount() == 2) {
									try {
										// fetch announcement content
										AnnouncementReader announcementReader = canvasApi.getReader(AnnouncementReader.class);
										List<edu.ksu.canvas.model.announcement.Announcement> announcementList = announcementReader.listCourseAnnouncement(new ListCourseAnnouncementOptions(course.getId().toString()).startDate(course.getCreatedAt()).endDate(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())));
										announcementList.forEach(announcement1 -> {
											if (announcement.getId().equals(announcement1.getId())) {
												// creating a new scene with webview
												Stage webviewStage = new Stage();
												webviewStage.setTitle("Announcement");
												WebView webView = new WebView();
												webView.getEngine().loadContent(announcement1.getMessage());
												webviewStage.setScene(new Scene(webView, 500,500));
												webviewStage.show();
												// close window when out of focus
												webviewStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
													@Override
													public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
														if(!newValue){
															webviewStage.close();
														}
													}
												});
											}
										});

									} catch (IOException e) {
										log.error(String.format("Failed for fetch announcement %d in course %d", announcement.getId(), announcement.getCourse().getId()), e);
									}
								}
							}
						});
						announcementListViewItems.add(announcementCardView);
					});
				});
	}

}
