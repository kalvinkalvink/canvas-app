package canvas.canvasapp.controller.view.course.announcement;

import canvas.canvasapp.model.db.Announcement;
import canvas.canvasapp.model.db.Course;
import canvas.canvasapp.service.database.AnnouncementService;
import canvas.canvasapp.service.view.course.CourseViewService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@FxmlView("/view/component/course/announcement.fxml")
public class CourseAnnouncementController {
	@Autowired
	CourseViewService courseViewService;
	@Autowired
	AnnouncementService announcementService;
	@Autowired
	FxWeaver fxWeaver;
	@FXML
	ListView announcementListView;

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
					announcementCardViewOptional.ifPresent(announcementListViewItems::add);
				});
	}

}
