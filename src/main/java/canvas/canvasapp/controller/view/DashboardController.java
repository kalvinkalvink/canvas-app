package canvas.canvasapp.controller.view;

import canvas.canvasapp.controller.view.dashboard.ByDateCardController;
import canvas.canvasapp.controller.view.dashboard.DashboardAssignmentItemController;
import canvas.canvasapp.event.task.database.AssignmentUpdatedEvent;
import canvas.canvasapp.event.task.database.CourseUpdatedEvent;
import canvas.canvasapp.model.db.Assignment;
import canvas.canvasapp.model.db.Course;
import canvas.canvasapp.service.database.AssignmentService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

@Slf4j
@Controller
@FxmlView("/view/component/tab/dashboard.fxml")
public class DashboardController {
	private final FxWeaver fxWeaver;

	@FXML
	ListView<VBox> assigmentListView;

	@Autowired
	AssignmentService assignmentService;

	public DashboardController(FxWeaver fxWeaver) {
		this.fxWeaver = fxWeaver;
	}

	@FXML
	void initialize() {
		// setting placeholder when no assignment is displayed
		VBox noAssignmentPlaceHolderVBox = new VBox(
				new Text("No Assignment yet"),
				new Text("Course not selected (please select in preference"),
				new Text("Course selected do not have assignments")
		);
		assigmentListView.setPlaceholder(noAssignmentPlaceHolderVBox);
		assigmentListView.setMouseTransparent(true);
		initView();
	}

	private void initView() {
		log.info("Initializing dashboard view");
		Platform.runLater(() -> {
			if (Objects.isNull(assigmentListView)) return;
			assigmentListView.getItems().clear();
			Map<Date, List<Assignment>> assignmentByDueDate = assignmentService.getUpcomingAssignmentForSelectedCourse();
			assignmentByDueDate.forEach(new BiConsumer<Date, List<Assignment>>() {
				@Override
				public void accept(Date date, List<Assignment> assignmentList) {
					log.trace("Assignment date {}, assignment list size: {}", date, assignmentList.size());
					FxControllerAndView<ByDateCardController, VBox> byDateCardFxControllerAndView = fxWeaver.load(ByDateCardController.class);
					ByDateCardController byDateCardController = byDateCardFxControllerAndView.getController();
					byDateCardController.setDueDate(date);
					assignmentList.forEach(assignment -> {
						FxControllerAndView<DashboardAssignmentItemController, VBox> assignmentItemFxControllerAndView = fxWeaver.load(DashboardAssignmentItemController.class);
						DashboardAssignmentItemController dashboardAssignmentItemController = assignmentItemFxControllerAndView.getController();

						Course currCourse = assignment.getCourse();
						if (Objects.isNull(currCourse))
							return;
						if (Objects.isNull(currCourse.getColor()))
							dashboardAssignmentItemController.setColor(Color.GRAY);    // default color
						else
							dashboardAssignmentItemController.setColor(currCourse.getColor());    // assigned color

						dashboardAssignmentItemController.setCourseName(currCourse.getName());
						dashboardAssignmentItemController.setAssignmentname(assignment.getName());
						byDateCardController.addAssignment(assignmentItemFxControllerAndView.getView().get());
					});
					byDateCardFxControllerAndView.getView().ifPresentOrElse(assigmentListView.getItems()::add, () -> {
						log.error("Error while loading By date car view for course.");
					});

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

}
