package canvas.canvasapp.controller.view;

import canvas.canvasapp.dao.CourseDao;
import canvas.canvasapp.model.Course;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class PreferenceController {

	private CourseDao courseDao = new CourseDao();
	private ListProperty<String> courseItems;
	private ListProperty<String> courseSelections;

	public PreferenceController() {
		List<Course> allCourse = courseDao.getAll();
		courseItems = new SimpleListProperty<>(FXCollections.observableArrayList(
				allCourse.stream().map(course -> course.getName()).collect(Collectors.toList())
		));
		courseSelections = new SimpleListProperty<>(FXCollections.observableArrayList());
	}

	public void showPreferenceMenu(ActionEvent event) {

		PreferencesFx preferencesFx = PreferencesFx.of(PreferenceController.class,
				Category.of("Course",
						Group.of("Display courses",
								Setting.of("course", courseItems, courseSelections)))
		);
		preferencesFx.show();
	}

	private List<Setting> getCourseSettingList() {
//		List<Course> courseList = courseDao.getAll();
//		HashMap<Course, BooleanProperty> courseSelectedMap = new HashMap<>();
//		List<Setting> settingList = courseList.stream().map(course -> {
//			boolean isSelected = course.getSelected().equals(Course.Selected.Y);
//			BooleanProperty selectedBooleanProperty = new SimpleBooleanProperty(isSelected);
//			System.out.println(course.getName() + " " + isSelected);
//			SelectedCourseSetting selectedCourseSetting = new SelectedCourseSetting()
//					.setCourse(course)
//					.setSelectedBooleanProperty(selectedBooleanProperty);
//			selectedCourseSettingList.add(selectedCourseSetting);
//			return Setting.of(course.getName(), selectedBooleanProperty);
//		}).toList();
//		return settingList;
		return null;
	}
}
