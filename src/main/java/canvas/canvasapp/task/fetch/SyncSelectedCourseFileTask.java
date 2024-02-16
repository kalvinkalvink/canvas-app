package canvas.canvasapp.task.fetch;

import canvas.canvasapp.helpers.type.application.AppSetting;
import canvas.canvasapp.model.Course;
import canvas.canvasapp.model.File;
import canvas.canvasapp.model.Folder;
import canvas.canvasapp.repository.CourseRepository;
import canvas.canvasapp.service.application.CanvasFileService;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import canvas.canvasapp.service.database.CourseService;
import canvas.canvasapp.service.database.FileService;
import canvas.canvasapp.service.database.FolderService;
import canvas.canvasapp.service.helper.DocumentFormatConverterService;
import canvas.canvasapp.util.CanvasApi;
import canvas.canvasapp.util.FileTypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
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
	private CanvasFileService canvasFileService;
	@Autowired
	private CanvasPreferenceService canvasPreferenceService;
	@Autowired
	private CanvasApi canvasApi;
	@Autowired
	private DocumentFormatConverterService documentFormatConverterService;

	@Override
	public void run() {
		if (!canvasApi.isInitialized()) return;
		if (!canvasPreferenceService.get(AppSetting.SYNC_COURSE, false)) {    // sync course not enabled
			log.info("Sync file not enabled, skipping");
			return;
		}
		String syncFolderBasePath = FilenameUtils.separatorsToSystem(canvasPreferenceService.get(AppSetting.COURSE_SYNC_FOLDER_PATH, ""));
		Path syncFolderPath = Paths.get(syncFolderBasePath);
		if (syncFolderBasePath.isEmpty())    // no path selected, no sync
			return;
		List<Course> syncedCourseList = courseService.findAllSynced();
		log.info("Syncing {} course files to base folder: {}", syncedCourseList.size(), syncFolderBasePath);
		log.info(syncedCourseList.toString());


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
							Path destCourseFilePath = syncFolderPath.resolve(courseFileRelativePath).resolve(fileName);
							String destinationPath = FilenameUtils.separatorsToSystem(destCourseFilePath.toString());
							log.debug("Saving file to {}", destinationPath);
 							CompletableFuture<Void> downloadFileCompletableFuture = canvasFileService.downloadFile(file.getUrl(), destinationPath, true);


							// convert to pdf if needed
							downloadFileCompletableFuture.thenAccept((Void) -> {
								if (canvasPreferenceService.get(AppSetting.AUTO_CONVERT_DOC_TO_PDF, false) && FileTypeUtils.isDoc(destinationPath)) {
									documentFormatConverterService.documentToPdf(destCourseFilePath.toString());
								}
							});
						});
					});
		} catch (RuntimeException e) {
			log.error("Error while syncing course", e);
		}
		log.info("Sync selected course files complete");
	}
}
