package canvas.canvasapp.helpers;

import canvas.canvasapp.model.db.Course;
import canvas.canvasapp.model.db.File;
import canvas.canvasapp.model.db.Folder;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import canvas.canvasapp.type.application.AppSetting;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CourseFileHelper {
	@Autowired
	private CanvasPreferenceService canvasPreferenceService;
	public Path courseFileToSystemFile(Course course, File file){
		String fileBasePath = FilenameUtils.separatorsToSystem(canvasPreferenceService.get(AppSetting.COURSE_SYNC_FOLDER_PATH, ""));
		Path syncFolderPath = Paths.get(fileBasePath);
		Folder folder = file.getFolder();
		Path path = Paths.get(folder.getFullName());
		if (path.getNameCount() > 1) {    // not only "course file" folder
			// remove the first folder name
			path = path.subpath(1, path.getNameCount());
		}

		// prepend course name to path
		Path courseFileRelativePath = Paths.get(course.getName()).resolve(path);
		// append course file relatiev path to the sync base folder path
		String fileName = file.getDisplayName().replace(":", "_");    // sanitize filename
		return syncFolderPath.resolve(courseFileRelativePath).resolve(fileName);
	}
}
