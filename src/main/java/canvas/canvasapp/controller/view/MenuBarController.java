package canvas.canvasapp.controller.view;

import canvas.canvasapp.dao.CourseRepository;
import canvas.canvasapp.model.Course;
import javafx.beans.property.ListProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
@Controller
@FxmlView("/view/menu-bar.fxml")
public class MenuBarController {
	@Autowired
	PreferenceController preferenceController;
	@Autowired
	CourseRepository courseRepository;
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
//		courseRepository.findAll().stream().filter(Course::getSelected).forEach(System.out::println);
	}
}
