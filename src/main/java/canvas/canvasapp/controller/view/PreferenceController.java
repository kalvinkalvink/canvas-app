package canvas.canvasapp.controller.view;

import canvas.canvasapp.event.task.database.CourseUpdatedEvent;
import canvas.canvasapp.model.Course;
import canvas.canvasapp.service.CourseService;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.PreferencesFxEvent;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class PreferenceController {
	private static PreferencesFx preferencesFx;
	private final CourseService courseService;
	private boolean startSavedPreference = false;
	////////// setting /////////
	// selected course
	private ListProperty<String> courseItems;
	@Getter
	private ListProperty<String> courseSelections;
	// course color
	private Setting[] courseColorSettingArray;
	private ArrayList<SimpleObjectProperty<Color>> colorSimpleObjectPropertyList;
	///////// setting end ///////


	@Autowired
	public PreferenceController(CourseService courseService) {
		this.courseService = courseService;
		initPreference();
	}


	private void initPreference() {

		initData();
		Platform.runLater(() -> {
			preferencesFx = PreferencesFx.of(PreferenceController.class,
					Category.of("Course",
							Group.of("Display Courses",
									Setting.of("course", courseItems, courseSelections)),
							Group.of("Course Color",
									courseColorSettingArray)
					)
			).addEventHandler(PreferencesFxEvent.EVENT_PREFERENCES_SAVED, new EventHandler<PreferencesFxEvent>() {
				// preferebce menu close
				@Override
				public void handle(PreferencesFxEvent preferencesFxEvent) {
					savePreferece();
				}
			});
			if (!startSavedPreference) {	// save preference at the start of the application to load changed to the databse
				savePreferece();
				startSavedPreference = true;
			}
		});

	}


	private void savePreferece() {
		// save selected course
		log.info("Saving preference");
		List<Course> courseList = courseService.findAll();
		// unsetting all course to not selected
		courseList.forEach(course -> course.setSelected(false));
		// updating selected course to true
		courseList.forEach(course -> {
			if (courseSelections.contains(course.getName()))
				course.setSelected(true);
		});
		// selected course color
		courseList.forEach(course -> {
			for (int i = 0; i < courseColorSettingArray.length; i++) {
				String courseName = courseColorSettingArray[i].getDescription();
				if (course.getName().equals(courseName)) {
					SimpleObjectProperty<Color> colorSimpleObjectProperty = colorSimpleObjectPropertyList.get(i);
					Color color = colorSimpleObjectProperty.getValue();
					course.setColor(color);
				}
			}
		});
		courseService.saveAll(courseList);
		courseService.publishUpdateEvent();
	}


	private void initData() {

		List<Course> courseList = courseService.findAll();

		// setting selected course list
		courseItems = new SimpleListProperty<>(
				FXCollections.observableArrayList(
						courseList.stream()
								.map(Course::getName)
								.collect(Collectors.toList())
				)
		);
		courseSelections = new SimpleListProperty<>(FXCollections.observableArrayList());

		// setting course color list
		this.colorSimpleObjectPropertyList = new ArrayList<SimpleObjectProperty<Color>>();
		this.courseColorSettingArray = courseList.stream()
				.filter(Course::getSelected)
				.map(course -> {
					SimpleObjectProperty<Color> colorSimpleObjectProperty = new SimpleObjectProperty<>(Color.RED);
					colorSimpleObjectPropertyList.add(colorSimpleObjectProperty);
					return Setting.of(course.getName(), colorSimpleObjectProperty);
				})
				.toArray(Setting[]::new);
	}


	@EventListener
	private void courseUpdateEventListener(CourseUpdatedEvent courseUpdatedEvent) {
		log.debug("Updating preference because course table updated");
		initPreference();

	}

	public void showPreferenceMenu(ActionEvent event) {
		preferencesFx.show();
	}

}
