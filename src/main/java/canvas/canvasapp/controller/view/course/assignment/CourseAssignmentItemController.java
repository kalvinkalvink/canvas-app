package canvas.canvasapp.controller.view.course.assignment;

import canvas.canvasapp.service.database.CourseService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Slf4j
@Controller
@FxmlView("/view/component/course/assignment/course-assignment.fxml")
public class CourseAssignmentItemController {
	@FXML
	private Label assignmentNameLabel;
	@FXML
	private HBox dateInfoHBox;

	public void setAssignmentName(String name) {
		assignmentNameLabel.setText(name);
	}

	public void addToDateInfoHBox(Node node) {
		dateInfoHBox.getChildren().add(node);
	}
}
