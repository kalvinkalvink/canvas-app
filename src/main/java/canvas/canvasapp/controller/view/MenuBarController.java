package canvas.canvasapp.controller.view;

import javafx.beans.property.ListProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
@Component
@FxmlView("/view/menu-bar.fxml")
public class MenuBarController {
	@Autowired
	PreferenceController preferenceController;

	@FXML
	public void showPreferenceMenu(ActionEvent event) {
		preferenceController.showPreferenceMenu(event);
	}

	@FXML
	public void test(ActionEvent event) {
		ListProperty<String> courseSelections = preferenceController.getCourseSelections();
		courseSelections.forEach(course->{
			System.out.println(course);
		});
	}
}
