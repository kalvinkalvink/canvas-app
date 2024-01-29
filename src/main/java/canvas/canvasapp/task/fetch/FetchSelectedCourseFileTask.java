package canvas.canvasapp.task.fetch;

import canvas.canvasapp.model.Course;
import canvas.canvasapp.service.database.CourseService;
import canvas.canvasapp.service.database.FileService;
import canvas.canvasapp.service.database.FolderService;
import canvas.canvasapp.util.CanvasApi;
import edu.ksu.canvas.interfaces.FileReader;
import edu.ksu.canvas.requestOptions.ListCourseFileOptions;
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
public class FetchSelectedCourseFileTask implements Runnable {
	@Autowired
	private CourseService courseService;
	@Autowired
	private FileService fileService;
	@Autowired
	private FolderService folderService;
	@Autowired
	private CanvasApi canvasApi;

	@Override
	public void run() {
		log.info("Fetching selected course file");
		List<Course> selectedCourseList = courseService.findAllSelected();

		FileReader fileReader = canvasApi.getReader(FileReader.class);
		selectedCourseList.forEach(selectedCourse -> {
			List<canvas.canvasapp.model.File> courseFileList = new ArrayList<>();
					log.trace("Fetching file for selected course {}", selectedCourse.getName());
					try {
						fileReader.listCourseFile(new ListCourseFileOptions(selectedCourse.getId().toString())).stream()
								.peek(canvasFile -> log.trace("Fetching course file {}", canvasFile.getFilename()))
								.map(canvasFile -> new canvas.canvasapp.model.File()
										.setId(canvasFile.getId())
										.setUuid(canvasFile.getUuid())
										.setDisplayName(canvasFile.getDisplayName())
										.setFilename(canvasFile.getFilename())
										.setUrl(canvasFile.getUrl())
										.setSize(canvasFile.getSize())
										.setCreatedAt(canvasFile.getCreatedAt())
										.setUpdatedAt(canvasFile.getUpdatedAt())
										.setModifiedAt(canvasFile.getModifiedAt())
										.setHidden(canvasFile.getHidden())
										.setThumbnailUrl(canvasFile.getThumbnailUrl())
										.setPreviewUrl(canvasFile.getPreviewUrl())
										.setMimeClass(canvasFile.getMimeClass())
										.setCourse(selectedCourse)
										.setFolder(folderService.findById(canvasFile.getFolderId()).get())
								)
								.forEach(courseFileList::add);
						fileService.saveAll(courseFileList);
					} catch (IOException e) {
						log.error("Error while fetching file for course: {}", selectedCourse.getName(), e);
					}
				}
		);
		fileService.publishUpdateEvent();
	}
}
