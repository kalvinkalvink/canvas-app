package canvas.canvasapp.repository;

import canvas.canvasapp.model.db.Course;
import canvas.canvasapp.model.db.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, Long> {
	List<Folder> findByCourseId(Long coruseId);

	Optional<Folder> findByParentFolderIdIsNullAndCourse(Course course);

	List<Folder> findAllByParentFolderId(Long folderId);

}