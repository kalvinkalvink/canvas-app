package canvas.canvasapp.controller.view.course.assignment;

import canvas.canvasapp.model.db.Assignment;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Objects;


@Slf4j
@Controller
@FxmlView("/view/component/course/assignment/course-assignment.fxml")
public class CourseAssignmentItemController {
	@FXML
	private Label assignmentNameLabel;
	@FXML
	private HBox dateInfoHBox;

	private Assignment assignment;
	public void setAssignment(Assignment assignment){
		this.assignment = assignment;
		Date todayDate = new Date();
		// setting name
		assignmentNameLabel.setText(assignment.getName());
		// setting date info box
		ObservableList<Node> dateInfoHBoxChildren = dateInfoHBox.getChildren();
		if (Objects.nonNull(assignment.getUnlockAt()) && assignment.getUnlockAt().after(todayDate)) {
			dateInfoHBoxChildren.add(new Text(String.format("Not Available until %s", assignment.getUnlockAt())));
			dateInfoHBoxChildren.add(new Separator(Orientation.VERTICAL));
		}
		dateInfoHBoxChildren.add(new Text(String.format("Due %s", assignment.getDueAt())));
	}
}
