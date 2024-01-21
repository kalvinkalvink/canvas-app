package canvas.canvasapp.controller.view.dashboard;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
@Scope("prototype")
@FxmlView("/view/dashboard/by-date-card.fxml")
public class ByDateCardController {
	@FXML
	Label dueDateLabel;
	@FXML
	VBox assignmentVBox;
	@Autowired
	private FxWeaver fxWeaver;
	@Autowired
	public ByDateCardController(FxWeaver fxWeaver) {
		this.fxWeaver = fxWeaver;
	}
	public void setDueDate(Date dueDate){
		dueDateLabel.setText(dueDate.toString());
	}
	public void addAssignment(Node node){
		this.assignmentVBox.getChildren().add(node);
	}
}
