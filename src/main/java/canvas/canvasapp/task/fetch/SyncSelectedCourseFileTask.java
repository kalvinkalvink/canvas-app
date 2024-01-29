package canvas.canvasapp.task.fetch;

import canvas.canvasapp.helpers.type.application.AppSetting;
import canvas.canvasapp.model.Course;
import canvas.canvasapp.model.File;
import canvas.canvasapp.model.Folder;
import canvas.canvasapp.repository.CourseRepository;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import canvas.canvasapp.service.application.FileDownloadService;
import canvas.canvasapp.service.database.CourseService;
import canvas.canvasapp.service.database.FileService;
import canvas.canvasapp.service.database.FolderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Component
@Scope("prototype")
public class SyncSelectedCourseFileTask implements Runnable {

	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private CourseService courseService;
	@Autowired
	private FolderService folderService;
	@Autowired
	private FileService fileService;
	@Autowired
	private FileDownloadService fileDownloadService;
	@Autowired
	private CanvasPreferenceService canvasPreferenceService;

	@Override
	public void run() {
		if (!canvasPreferenceService.get(AppSetting.SYNC_COURSE, false)) {    // sync course not enabled
			log.info("Sync file not enabled, skipping");
			return;
		}
		String syncFolderBasePath = FilenameUtils.separatorsToSystem(canvasPreferenceService.get(AppSetting.COURSE_SYNC_FOLDER_PATH, ""));
		if (syncFolderBasePath.isEmpty())
			return;
		Path syncFolderPath = Paths.get(syncFolderBasePath);
		List<Course> syncedCourseList = courseService.findAllSynced();
		log.info("Syncing {} course files to base folder: {}", syncedCourseList.size(), syncFolderBasePath);


		try {
			syncedCourseList.stream()
					.peek(course -> log.debug("Syncing course {} files", course.getName()))
					.forEach(courseToSync -> {
						// fetch course folders and files
						List<File> courseFileList = fileService.findByCourseId(courseToSync.getId());
						courseFileList.forEach(file -> {
							Folder folder = file.getFolder();
							Path path = Paths.get(folder.getFullName());
							if (path.getNameCount() > 1) {    // not only "course file" folder
								// remove the first folder name
								path = path.subpath(1, path.getNameCount());
							}

							// prepend course name to path
							Path courseFileRelativePath = Paths.get(courseToSync.getName()).resolve(path);
							// append course file relatiev path to the sync base folder path
							String fileName = file.getDisplayName().replace(":", "_");    // sanitize filename
							Path courseFilePath = syncFolderPath.resolve(courseFileRelativePath).resolve(fileName);
							fileDownloadService.downloadFile(file.getUrl(), courseFilePath.toString(), true);
						});
					});
		} catch (RuntimeException e) {
			log.error("Error while syncing course", e);
		}
	}
}
