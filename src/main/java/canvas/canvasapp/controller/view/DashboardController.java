package canvas.canvasapp.controller.view;

import canvas.canvasapp.controller.view.dashboard.AssignmentItemController;
import canvas.canvasapp.controller.view.dashboard.ByDateCardController;
import canvas.canvasapp.event.task.database.AssignmentUpdatedEvent;
import canvas.canvasapp.event.task.database.CourseUpdatedEvent;
import canvas.canvasapp.model.Assignment;
import canvas.canvasapp.model.Course;
import canvas.canvasapp.service.AssignmentService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

@Slf4j
@Controller
@FxmlView("/view/dashboard.fxml")
public class DashboardController implements IViewController {
	private final FxWeaver fxWeaver;

	@FXML
	private Button courseTabBtn;
	@FXML
	private Button filesTabBtn;
	@FXML
	private ListView<VBox> assigmentListView;
	@Autowired
	SceneController sceneController;

	@Autowired
	AssignmentService assignmentService;

	public DashboardController(FxWeaver fxWeaver) {
		this.fxWeaver = fxWeaver;
	}

	@FXML
	private void initialize() {
		assigmentListView.setPlaceholder(new Label(String.format("Course not selected (please select in preference),\nor Course selected do not have assignments")));
		initView();
	}

	private void initView() {
		log.info("Initializing dashboard view");
		Platform.runLater(() -> {
			assigmentListView.getItems().clear();
			Map<Date, List<Assignment>> assignmentByDueDate = assignmentService.getUpcomingAssignmentForSelectedCourse();
			assignmentByDueDate.forEach(new BiConsumer<Date, List<Assignment>>() {
				@Override
				public void accept(Date date, List<Assignment> assignmentList) {
					log.debug("Assignment date {}, assignment list size: {}", date, assignmentList.size());
					FxControllerAndView<ByDateCardController, VBox> byDateCardFxControllerAndView = fxWeaver.load(ByDateCardController.class);
					ByDateCardController byDateCardController = byDateCardFxControllerAndView.getController();
					byDateCardController.setDueDate(date);
					assignmentList.forEach(assignment -> {
						FxControllerAndView<AssignmentItemController, VBox> assignmentItemFxControllerAndView = fxWeaver.load(AssignmentItemController.class);
						AssignmentItemController assignmentItemController = assignmentItemFxControllerAndView.getController();

						Course currCourse = assignment.getCourse();
						if (Objects.isNull(currCourse))
							return;
						if (Objects.isNull(currCourse.getColor()))
							assignmentItemController.setColor(Color.GRAY);    // default color
						else
							assignmentItemController.setColor(currCourse.getColor());    // assigned color

						assignmentItemController.setCourseName(currCourse.getName());
						assignmentItemController.setAssignmentname(assignment.getName());
						byDateCardController.addAssignment(assignmentItemFxControllerAndView.getView().get());
					});
					assigmentListView.getItems().add(byDateCardFxControllerAndView.getView().get());
				}
			});
		});


	}

	@EventListener
	public void assignmentUpdatedEventListener(AssignmentUpdatedEvent assignmentUpdatedEvent) {
		log.debug("Updating dashboard beucase assignmwent table updated");
		initView();
	}

	@EventListener
	public void courseUpdatedEventListener(CourseUpdatedEvent courseUpdatedEvent) {
		log.debug("Updating dashboard beucase course table updated");
		initView();
	}

	@FXML
	private void showCourseTab(ActionEvent event) throws IOException {
		sceneController.showCourseTab();
	}

	@FXML
	private void showFilesTab(ActionEvent event) throws IOException {
	}


	@Override
	public void show() {
		sceneController.showDashboardTab();
	}
}
