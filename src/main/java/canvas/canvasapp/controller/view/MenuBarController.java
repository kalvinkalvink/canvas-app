package canvas.canvasapp.controller.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuBarController {
	PreferenceController preferenceController;
	public MenuBarController() {
		preferenceController = new PreferenceController();
	}

	@FXML
	protected void showPreferenceMenu(ActionEvent event) {
		preferenceController.showPreferenceMenu(event);
	}

	public void test(ActionEvent event) {

	}
}
