package canvas.canvasapp.controller.view;

import canvas.canvasapp.dao.CourseRepository;
import canvas.canvasapp.event.task.fetch.CourseFetchedEvent;
import canvas.canvasapp.model.Course;
import canvas.canvasapp.task.load.LoadCourseTask;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class PreferenceController {
	// tasks
	private LoadCourseTask loadCourseTask;

	// repository
	private final CourseRepository courseRepository;

	////////// setting /////////
	// selected course
	private ListProperty<String> courseItems;
	@Getter
	private ListProperty<String> courseSelections;
	// course color
	private Setting[] courseColorSettingArray;
	private ArrayList<SimpleObjectProperty<Color>> colorSimpleObjectPropertyList;
	///////// setting end ///////

	private PreferencesFx preferencesFx;
	private List<Course> courseList;

	@Autowired
	public PreferenceController(LoadCourseTask loadCourseTask
			, CourseRepository courseRepository) {
		this.loadCourseTask = loadCourseTask;
		this.courseRepository = courseRepository;
		initPreference();
	}


	private void initPreference() {
		Platform.runLater(
				() -> {
					initData();
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
				}
		);
	}

	private void savePreferece() {
		// save selected course
		log.info("Saving preference");
		List<Course> courseList = courseRepository.findAll();
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
				if(course.getName().equals(courseName)){
					SimpleObjectProperty<Color> colorSimpleObjectProperty = colorSimpleObjectPropertyList.get(i);
					Color color = colorSimpleObjectProperty.getValue();
					System.out.println("saving color" + color.toString());
					course.setColor(color);
				}
			}
		});
		courseRepository.saveAll(courseList);
	}


	private void initData() {
		log.info("Initializing preference menu data");
		try {
			if (loadCourseTask.isRunning())
				loadCourseTask.cancel();
			loadCourseTask.run();
			this.courseList = loadCourseTask.get();

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
		} catch (InterruptedException | ExecutionException e) {
			log.warn("Error while initializing preference menu data", e);
		}
	}

	@EventListener
	private void courseFetchedEventListener(CourseFetchedEvent courseFetchedEvent) {
		initPreference();
	}

	public void showPreferenceMenu(ActionEvent event) {
		preferencesFx.show();
	}

}
