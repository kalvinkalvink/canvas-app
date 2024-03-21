package canvas.canvasapp.controller.view.course.assignment;

import canvas.canvasapp.event.task.database.AssignmentUpdatedEvent;
import canvas.canvasapp.exception.task.DataFetchTaskNotSupported;
import canvas.canvasapp.model.db.Assignment;
import canvas.canvasapp.model.db.Course;
import canvas.canvasapp.service.application.CanvasDataFetchTaskService;
import canvas.canvasapp.service.database.AssignmentService;
import canvas.canvasapp.service.view.course.CourseViewService;
import canvas.canvasapp.type.database.TableFetchType;
import canvas.canvasapp.util.CanvasApi;
import edu.ksu.canvas.interfaces.AssignmentReader;
import edu.ksu.canvas.requestOptions.GetSingleAssignmentOptions;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
@FxmlView("/view/component/course/assignment.fxml")
public class CourseAssignmentController {
	@Autowired
	FxWeaver fxWeaver;
	@Autowired
	private CourseViewService courseViewService;
	@Autowired
	private AssignmentService assignmentService;
	@Autowired
	private CanvasApi canvasApi;
	@Autowired
	private CanvasDataFetchTaskService canvasDataFetchTaskService;

	@FXML
	Accordion assignmentAccordion;
	@FXML
	TitledPane upcomingAssignmentTiledPane;
	@FXML
	TitledPane pastAssignmentTiledPane;

	@FXML
	private void initialize() {
		try {
			canvasDataFetchTaskService.startFetchTask(TableFetchType.ASSIGNMENT);
		} catch (DataFetchTaskNotSupported e) {
			log.error("Failed to fetch course assignment", e);
		}
		initView();
	}

	private void initView() {
		Platform.runLater(() -> {
			Course course = courseViewService.getCourse();
			log.info("Initializing course {} assignemnt", course.getName());
			AssignmentReader assignmentReader = canvasApi.getReader(AssignmentReader.class);


			List<Assignment> courseAssignmentList = assignmentService.getAssignmentsByCourseId(course.getId());
			ArrayList<Assignment> upcomingAssignmentList = new ArrayList<>();
			ArrayList<Assignment> pastAssignmentList = new ArrayList<>();

			Date todayDate = new Date();
			courseAssignmentList.forEach(assignment -> {
				if (Objects.nonNull(assignment.getDueAt()) && assignment.getDueAt().before(todayDate))
					pastAssignmentList.add(assignment);
				else upcomingAssignmentList.add(assignment);
			});
			// upcoming assignment
			ListView<Node> upcomingAssignmentListView = new ListView<>();
			upcomingAssignmentList.forEach(upcomingAssignment -> {
				FxControllerAndView<CourseAssignmentItemController, Node> fxControllerAndView = fxWeaver.load(CourseAssignmentItemController.class);
				// adding info to assignemnt card
				CourseAssignmentItemController controller = fxControllerAndView.getController();
				controller.setAssignment(upcomingAssignment);
				fxControllerAndView.getView().ifPresent(courseAssignmentView -> {
					setOnAssignmentClickAction(courseAssignmentView, assignmentReader, course, upcomingAssignment);
					upcomingAssignmentListView.getItems().add(courseAssignmentView);
				});
			});

			upcomingAssignmentTiledPane.setContent(upcomingAssignmentListView);


			// past assignment
			ListView<Node> pastAssignmentListView = new ListView<>();
			pastAssignmentList.forEach(pastAssignment -> {
				FxControllerAndView<CourseAssignmentItemController, Node> fxControllerAndView = fxWeaver.load(CourseAssignmentItemController.class);
				// adding info to assignment card
				CourseAssignmentItemController controller = fxControllerAndView.getController();
				controller.setAssignment(pastAssignment);
				fxControllerAndView.getView().ifPresent(courseAssignmentView -> {
					setOnAssignmentClickAction(courseAssignmentView, assignmentReader, course, pastAssignment);
				});
			});
			pastAssignmentTiledPane.setContent(pastAssignmentListView);
			assignmentAccordion.setExpandedPane(upcomingAssignmentTiledPane);
		});
	}

	public void setOnAssignmentClickAction(Node courseAssignmentView, AssignmentReader assignmentReader, Course course, Assignment courseAssignment) {
		courseAssignmentView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() != 2) return;        // only handle double click
				try {
					Optional<edu.ksu.canvas.model.assignment.Assignment> singleAssignment = assignmentReader.getSingleAssignment(new GetSingleAssignmentOptions(course.getId().toString(), courseAssignment.getId()));
					singleAssignment.ifPresent(canvasAssignment -> {
						// display the assignment details ui
						FxControllerAndView<CourseAssignmentDetailsController, Node> courseAssignmentPopupControllerNodeFxControllerAndView = fxWeaver.load(CourseAssignmentDetailsController.class);
						CourseAssignmentDetailsController controller = courseAssignmentPopupControllerNodeFxControllerAndView.getController();
						controller.setAssignment(canvasAssignment);
						controller.initView();

						// create new window
						courseAssignmentPopupControllerNodeFxControllerAndView.getView().ifPresent(courseAssignmentDetailsView -> {
							// create a popup window
							Stage courseAssignmentDetailsStage = new Stage();
							courseAssignmentDetailsStage.setTitle("Course assignment details");
							courseAssignmentDetailsStage.setScene(new Scene((Parent) courseAssignmentDetailsView, 500, 700));
							courseAssignmentDetailsStage.show();
						});
					});
				} catch (IOException e) {
					log.error("Failed to fetch course {} assignment {}", course.getId(), courseAssignment.getId());
				}
			}
		});
	}

	@EventListener
	public void assignmentUpdatedEventListener(AssignmentUpdatedEvent assignmentUpdatedEvent) {
		if (Objects.isNull(courseViewService.getCourse())) return;    // no course selected
		log.debug("Updating course assignment because course table updated");
		initView();
	}
}
