package canvas.canvasapp.task.fetch;

import canvas.canvasapp.helpers.CourseFileHelper;
import canvas.canvasapp.service.application.document.DocumentToPdfConverterService;
import canvas.canvasapp.model.db.Course;
import canvas.canvasapp.model.db.File;
import canvas.canvasapp.service.application.CanvasFileService;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import canvas.canvasapp.service.database.CourseService;
import canvas.canvasapp.service.database.FileService;
import canvas.canvasapp.type.application.AppSetting;
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
	private CourseService courseService;
	@Autowired
	private FileService fileService;
	@Autowired
	private CanvasFileService canvasFileService;
	@Autowired
	private CanvasPreferenceService canvasPreferenceService;
	@Autowired
	private CanvasApi canvasApi;
	@Autowired
	private DocumentToPdfConverterService documentToPdfConverterService;
	@Autowired
	private CourseFileHelper courseFileHelper;

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
							Path destDocumentPath = courseFileHelper.courseFileToSystemFile(courseToSync, file);
							String destinationPath = FilenameUtils.separatorsToSystem(destDocumentPath.toString());
							log.debug("Saving file to {}", destinationPath);
							CompletableFuture<Void> downloadFileCompletableFuture = canvasFileService.downloadFile(file.getUrl(), destinationPath, true);


							// convert to pdf if needed
							downloadFileCompletableFuture.thenAccept((Void) -> {
								if (canvasPreferenceService.get(AppSetting.AUTO_CONVERT_DOC_TO_PDF, false) && FileTypeUtils.isDoc(destinationPath)) {
									try {
										documentToPdfConverterService.convertDocumentToPdf(destinationPath);
									} catch (Exception e) {
										log.error(String.format("Error while converting document '%s' to pdf", destinationPath), e);
									}
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
