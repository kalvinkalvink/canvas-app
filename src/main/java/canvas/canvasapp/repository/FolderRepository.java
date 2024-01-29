package canvas.canvasapp.repository;

import canvas.canvasapp.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
	public List<Folder> findByCourseId(Long coruseId);

}