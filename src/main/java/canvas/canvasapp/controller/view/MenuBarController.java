package canvas.canvasapp.controller.view;

import canvas.canvasapp.model.Course;
import canvas.canvasapp.service.CourseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@FxmlView("/view/menu-bar.fxml")
public class MenuBarController {
	@Autowired
	PreferenceController preferenceController;
	@Autowired
	CourseService courseService;
	@FXML
	public void showPreferenceMenu(ActionEvent event) {
		preferenceController.showPreferenceMenu(event);
	}

	@FXML
	public void test(ActionEvent event) {
//		ListProperty<String> courseSelections = preferenceController.getCourseSelections();
//		courseSelections.forEach(course->{
//			System.out.println(course);
//		});
//		courseRepository.findAll().stream().filter(Course::getSelected).forEach(System.out::println);
		List<Course> bySelectedIsTrue = courseService.getAllSelected();
		bySelectedIsTrue.forEach(course-> System.out.println(course.getColor()));
	}
}
