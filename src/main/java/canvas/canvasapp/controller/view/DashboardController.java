package canvas.canvasapp.controller.view;

import canvas.canvasapp.controller.view.dashboard.AssignmentItemController;
import canvas.canvasapp.controller.view.dashboard.ByDateCardController;
import canvas.canvasapp.event.task.fetch.AssignmentFetchedEvent;
import canvas.canvasapp.model.Assignment;
import canvas.canvasapp.model.Course;
import canvas.canvasapp.task.load.LoadUpcomingAssignmentTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import java.util.*;
import java.util.concurrent.ExecutionException;
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
	private LoadUpcomingAssignmentTask loadUpcomingAssignmentTask;

	public DashboardController(FxWeaver fxWeaver) {
		this.fxWeaver = fxWeaver;
	}

	@FXML
	private void initialize() {
		initView();
	}

	private void initView() {
		log.info("Initializing dashboard view");
		Platform.runLater(() -> {
			if (loadUpcomingAssignmentTask.isRunning())
				loadUpcomingAssignmentTask.cancel();
			assigmentListView.getItems().clear();
			try {
				loadUpcomingAssignmentTask.run();
				Map<Date, List<Assignment>> assignmentByDueDate = loadUpcomingAssignmentTask.get();
				assignmentByDueDate.forEach(new BiConsumer<Date, List<Assignment>>() {
					@Override
					public void accept(Date date, List<Assignment> assignmentList) {
						FxControllerAndView<ByDateCardController, VBox> byDateCardFxControllerAndView = fxWeaver.load(ByDateCardController.class);
						ByDateCardController byDateCardController = byDateCardFxControllerAndView.getController();
						byDateCardController.setDueDate(date);
						assignmentList.forEach(assignment -> {
							FxControllerAndView<AssignmentItemController, VBox> assignmentItemFxControllerAndView = fxWeaver.load(AssignmentItemController.class);
							AssignmentItemController assignmentItemController = assignmentItemFxControllerAndView.getController();
							System.out.println("finding course for "+ assignment.getCourse().getId());
							Course currCourse = assignment.getCourse();
							if(Objects.isNull(currCourse))
								return;
							if(Objects.isNull(currCourse.getColor()))
								assignmentItemController.setColor(Color.GRAY);	// default color
							else
								assignmentItemController.setColor(currCourse.getColor());	// assigned color
							System.out.println(currCourse.getColor());
							assignmentItemController.setCourseName(currCourse.getName());
							assignmentItemController.setAssignmentname(assignment.getName());
							byDateCardController.addAssignment(assignmentItemFxControllerAndView.getView().get());
						});
						assigmentListView.getItems().add(byDateCardFxControllerAndView.getView().get());
					}
				});
			} catch (InterruptedException | ExecutionException e) {
				log.error("Failed to load dashboard assignment", e);
			}
		});

	}

	@EventListener
	public void assignmentUpdatedEventListener(AssignmentFetchedEvent assignmentFetchedEvent) {
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
