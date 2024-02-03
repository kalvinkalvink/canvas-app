package canvas.canvasapp.controller.view;

import canvas.canvasapp.event.publisher.setting.SettingEventPublisher;
import canvas.canvasapp.event.task.database.CourseUpdatedEvent;
import canvas.canvasapp.helpers.type.application.AppSetting;
import canvas.canvasapp.helpers.type.setting.SettingEvent;
import canvas.canvasapp.model.Course;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import canvas.canvasapp.service.database.CourseService;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.PreferencesFxEvent;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class PreferenceController {

	@Autowired
	private CanvasPreferenceService canvasPreferenceService;
	@Autowired
	private SettingEventPublisher settingEventPublisher;
	private static PreferencesFx preferencesFx;
	private final CourseService courseService;
	private boolean startSavedPreference = false;
	private ListProperty<String> courseItems;
	////////// setting /////////
	// selected course //
	private ListProperty<String> courseDisplaySelections = new SimpleListProperty<>(FXCollections.observableArrayList());
	// course color
	private Setting[] courseColorSettingArray;
	private ArrayList<SimpleObjectProperty<Color>> colorSimpleObjectPropertyList;

	// syncing //
	private ListProperty<String> courseSyncSelections = new SimpleListProperty<>(FXCollections.observableArrayList());
	private BooleanProperty syncCourseBooleanProperty = new SimpleBooleanProperty();
	private SimpleObjectProperty<File> syncFolderBasePathStringProperty = new SimpleObjectProperty<>();
	private SimpleIntegerProperty syncIntervalIntegerProperty = new SimpleIntegerProperty(10);

	// canvas api
	private SimpleStringProperty canvasApiBaseUrlStringProperty = new SimpleStringProperty();
	private SimpleStringProperty canvasApiTokenStringProperty = new SimpleStringProperty();
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
							Group.of("Displayed Courses",
									Setting.of("course", courseItems, courseDisplaySelections)),
							Group.of("Course Color",
									courseColorSettingArray)
					),
					Category.of("Syncing",
							Group.of("Course To Sync",
									Setting.of("Synced Courses", courseDisplaySelections, courseSyncSelections)
							),
							Group.of("Sync Course Setting",
									Setting.of("Sync Course", syncCourseBooleanProperty),
									Setting.of("Syncing Base Folder Path", syncFolderBasePathStringProperty, "Browse", Paths.get(System.getProperty("user.home")).toFile(), true),
									Setting.of("Sync Interval (in seconds)", syncIntervalIntegerProperty)
							)
					),
					Category.of("Canvas",
							Group.of("Api Config",
									Setting.of("Canvas Api Url", canvasApiBaseUrlStringProperty),
									Setting.of("Canvas Api Token", canvasApiTokenStringProperty)
							))
			).addEventHandler(PreferencesFxEvent.EVENT_PREFERENCES_SAVED, new EventHandler<PreferencesFxEvent>() {
				// preferebce menu close
				@Override
				public void handle(PreferencesFxEvent preferencesFxEvent) {
					savePreferenceToPref();
					savePreferenceToDb();
				}
			});
			if (!startSavedPreference) {    // save preference at the start of the application to load changed to the databse
				savePreferenceToPref();
				savePreferenceToDb();
				startSavedPreference = true;
			}
		});

	}

	private void savePreferenceToPref() {
		// course sync //
		canvasPreferenceService.store(AppSetting.COURSE_SYNC_INTERVAL, Integer.toString(syncIntervalIntegerProperty.get()));
		// check if sync folder base path is null
		File syncFolderFile = syncFolderBasePathStringProperty.get();
		canvasPreferenceService.store(AppSetting.COURSE_SYNC_FOLDER_PATH, Objects.nonNull(syncFolderFile) ? syncFolderFile.toString() : System.getProperty("user.home"));
		canvasPreferenceService.store(AppSetting.SYNC_COURSE, syncCourseBooleanProperty.getValue().toString());
		// course sync end //
		// canvas api //
		if (Objects.nonNull(canvasApiBaseUrlStringProperty.getValue()) && Objects.nonNull(canvasApiTokenStringProperty.getValue())) {
			canvasPreferenceService.store(AppSetting.CANVAS_BASE_URL, canvasApiBaseUrlStringProperty.getValue());
			canvasPreferenceService.store(AppSetting.CANVAS_API_TOKEN, canvasApiTokenStringProperty.getValue());
			settingEventPublisher.publishEvent(this, SettingEvent.CANVAS_API_INFO_UPDATED);
		}
	}


	private void savePreferenceToDb() {
		// save selected course
		log.info("Saving preference");
		List<Course> courseList = courseService.findAll();
		// unsetting all course to not selected and not synced
		courseList.forEach(course -> {
			course.setSelected(false);
			course.setSynced(false);
		});

		courseList.forEach(course -> {
			// updating selected course to true
			if (courseDisplaySelections.contains(course.getName()))
				course.setSelected(true);
			// updating synced course to true
			if (courseSyncSelections.contains(course.getName()))
				course.setSynced(true);

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
