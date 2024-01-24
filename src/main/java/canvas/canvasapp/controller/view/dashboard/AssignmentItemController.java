package canvas.canvasapp.controller.view.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@FxmlView("/view/component/dashboard/assignment.fxml")
public class AssignmentItemController {
	@FXML
	Pane colorPane;
	@FXML
	Label courseNameLabel;
	@FXML
	Label assignmentNameLabel;

	public void setColor(Color color) {
		// set color pane color
		colorPane.setBackground(Background.fill(color));
		// set course name border color
		courseNameLabel.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));

	}

	public void setCourseName(String courseName) {
		courseNameLabel.setText(courseName);
	}

	public void setAssignmentname(String assignmentName) {
		assignmentNameLabel.setText(assignmentName);
	}

}
