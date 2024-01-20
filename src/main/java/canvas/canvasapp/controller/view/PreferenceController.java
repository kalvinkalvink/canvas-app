package canvas.canvasapp.controller.view;

import canvas.canvasapp.dao.CourseRepository;
import canvas.canvasapp.event.StartupFinishEvent;
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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class PreferenceController {

	private LoadCourseTask loadCourseTask;
	private final CourseRepository courseRepository;
	private ListProperty<String> courseItems;
	@Getter
	private ListProperty<String> courseSelections;

	private PreferencesFx preferencesFx;

	@Autowired
	public PreferenceController(LoadCourseTask loadCourseTask
			, CourseRepository courseRepository) {
		this.loadCourseTask = loadCourseTask;
		this.courseRepository = courseRepository;
		initPreference();
	}


	private void initPreference() {
		initData();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				preferencesFx = PreferencesFx.of(PreferenceController.class,
						Category.of("Course",
								Group.of("Display courses",
										Setting.of("course", courseItems, courseSelections)))

				).addEventHandler(PreferencesFxEvent.EVENT_PREFERENCES_SAVED, new EventHandler<PreferencesFxEvent>() {
					// preferebce menu close
					@Override
					public void handle(PreferencesFxEvent preferencesFxEvent) {
						savePreferece();
					}
				});
			}
		});

	}

	private void savePreferece() {
		// save selected course
		log.info("Saving preference");
		courseRepository.setAllSelectedToFalse();
		List<Course> courseList = courseRepository.findAll();
		List<Course> selectedCoruseList = courseSelections.stream()
				.map(courseName -> courseRepository.findByName(courseName))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(course -> course.setSelected(true)).toList();
		courseRepository.saveAll(selectedCoruseList);
	}


	private void initData() {
		log.info("Initializing preference menu data");
		try {
			if (!loadCourseTask.isRunning()) {
				loadCourseTask.run();
				List<Course> courseList = loadCourseTask.get();
				courseItems = new SimpleListProperty<>(FXCollections.observableArrayList(
						courseList.stream().map(course -> course.getName()).collect(Collectors.toList())
				));
				courseSelections = new SimpleListProperty<>(FXCollections.observableArrayList());
			}
		} catch (InterruptedException | ExecutionException e) {
			log.warn("Error while initializing preference menu data", e);
		}
	}

	@EventListener
	private void startupFinishEventListener(StartupFinishEvent startupFinishEvent) {
		initPreference();
	}

	public void showPreferenceMenu(ActionEvent event) {
		preferencesFx.show();
	}

}
