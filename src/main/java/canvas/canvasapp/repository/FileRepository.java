package canvas.canvasapp.repository;

import canvas.canvasapp.model.db.File;
import canvas.canvasapp.model.db.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
	List<File> findByCourseId(Long courseId);

	List<File> findAllByFolder(Folder folder);
}
