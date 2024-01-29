package canvas.canvasapp.controller.view.course.assignment;

import canvas.canvasapp.model.Assignment;
import canvas.canvasapp.model.Course;
import canvas.canvasapp.service.database.AssignmentService;
import canvas.canvasapp.service.view.course.CourseViewService;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@FxmlView("/view/component/course/assignment.fxml")
public class AssignmentController {
	@Autowired
	FxWeaver fxWeaver;
	@Autowired
	CourseViewService courseViewService;
	@Autowired
	AssignmentService assignmentService;

	@FXML
	Accordion assignmentAccordion;
	@FXML
	TitledPane upcomingAssignmentTiledPane;
	@FXML
	TitledPane pastAssignmentTiledPane;

	@FXML
	private void initialize() {
		Course course = courseViewService.getCourse();
		log.info("Initializing course {} assignemnt", course.getName());
		List<Assignment> courseAssignmentList = assignmentService.getAssignmentsByCourseId(course.getId());
		ArrayList<Assignment> upcomingAssignmentList = new ArrayList<>();
		ArrayList<Assignment> pastAssignmentList = new ArrayList<>();

		Date todayDate = new Date();
		courseAssignmentList.forEach(assignment -> {
			if (assignment.getDueAt().before(todayDate)) pastAssignmentList.add(assignment);
			else upcomingAssignmentList.add(assignment);
		});
		// upcoming assignment
		ListView<Node> upcomingAssignmentListView = new ListView<>();
		upcomingAssignmentList.forEach(upcomingAssignment -> {
			FxControllerAndView<CourseAssignmentItemController, Node> fxControllerAndView = fxWeaver.load(CourseAssignmentItemController.class);
			// adding info to assignemnt card
			CourseAssignmentItemController controller = fxControllerAndView.getController();
			controller.setAssignmentName(upcomingAssignment.getName());
			if (upcomingAssignment.getUnlockAt().after(todayDate)) {
				controller.addToDateInfoHBox(new Text(String.format("Not Available until %s", upcomingAssignment.getUnlockAt())));
				controller.addToDateInfoHBox(new Separator(Orientation.VERTICAL));
			}
			controller.addToDateInfoHBox(new Text(String.format("Due %s", upcomingAssignment.getDueAt())));
			fxControllerAndView.getView().ifPresent(upcomingAssignmentListView.getItems()::add);
		});
		upcomingAssignmentTiledPane.setContent(upcomingAssignmentListView);
		// past assignment
		ListView<Node> pastAssignmentListView = new ListView<>();
		pastAssignmentList.forEach(pastAssignment -> {
			FxControllerAndView<CourseAssignmentItemController, Node> fxControllerAndView = fxWeaver.load(CourseAssignmentItemController.class);
			// adding info to assignment card
			CourseAssignmentItemController controller = fxControllerAndView.getController();
			controller.setAssignmentName(pastAssignment.getName());
			controller.addToDateInfoHBox(new Text(String.format("Due %s", pastAssignment.getDueAt())));
		});
		pastAssignmentTiledPane.setContent(pastAssignmentListView);

		assignmentAccordion.setExpandedPane(upcomingAssignmentTiledPane);
	}
}
