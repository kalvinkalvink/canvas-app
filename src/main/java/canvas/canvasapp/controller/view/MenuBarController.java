package canvas.canvasapp.controller.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MenuBarController {
	@Autowired
	PreferenceController preferenceController;

	@FXML
	public void showPreferenceMenu(ActionEvent event) {
		preferenceController.showPreferenceMenu(event);
	}

	@FXML
	public void test(ActionEvent event) {

	}
}
