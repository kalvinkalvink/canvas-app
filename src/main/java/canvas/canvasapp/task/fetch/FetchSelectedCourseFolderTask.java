package canvas.canvasapp.task.schedule.fetch;

import canvas.canvasapp.model.Course;
import canvas.canvasapp.service.database.CourseService;
import canvas.canvasapp.service.database.FolderService;
import canvas.canvasapp.util.CanvasApi;
import edu.ksu.canvas.interfaces.FolderReader;
import edu.ksu.canvas.model.Folder;
import edu.ksu.canvas.requestOptions.ListCourseFolderOptioins;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Scope("prototype")
public class FetchSelectedCourseFolderTask implements Runnable {
	@Autowired
	private FolderService folderService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private CanvasApi canvasApi;

	@Override
	public void run() {
		if (!canvasApi.isInitialized()) return;

		log.info("Fetching selected course folder");
		List<Course> selectedCourseList = courseService.findAllSelected();
		FolderReader folderReader = canvasApi.getReader(FolderReader.class);

		List<canvas.canvasapp.model.Folder> courseFolderList = new ArrayList<>();
		selectedCourseList.stream()
				.map(selectedCourse -> {
					try {
						return folderReader.listCourseFolder(new ListCourseFolderOptioins(selectedCourse.getId().toString()));
					} catch (IOException e) {
						log.error("Error while fetching folder for course {}", selectedCourse.getName(), e);
						return new ArrayList<Folder>();
					}
				}).flatMap(List::stream)
				.map(canvasFolder -> new canvas.canvasapp.model.Folder()
						.setId(canvasFolder.getId())
						.setName(canvasFolder.getName())
						.setFullName(canvasFolder.getFullName())
						.setContextId(canvasFolder.getContextId())
						.setContextType(canvasFolder.getContextType())
						.setParentFolderId(canvasFolder.getParentFolderId())
						.setCreatedAt(canvasFolder.getCreatedAt())
						.setCanUpload(canvasFolder.isCanUpload())
						.setLockAt(canvasFolder.getLockAt())
						.setPosition(canvasFolder.getPosition())
						.setFoldersUrl(canvasFolder.getFoldersUrl())
						.setFilesUrl(canvasFolder.getFilesUrl())
						.setFilesCount(canvasFolder.getFilesCount())
						.setFoldersCount(canvasFolder.getFoldersCount())
						.setHidden(canvasFolder.isHidden())
						.setLockedForser(canvasFolder.isLockedForser())
						.setHiddenForUser(canvasFolder.isHiddenForUser())
						.setForSubmissions(canvasFolder.isForSubmissions())
						.setCanUpload(canvasFolder.isCanUpload())
						.setCourse(courseService.findById(canvasFolder.getContextId()).get())
				)
				.forEach(courseFolderList::add);
		folderService.saveAll(courseFolderList);
		folderService.publishUpdateEvent();
		log.info("Fetched selected course folder");

	}
}
