package canvas.canvasapp.repository;

import canvas.canvasapp.model.db.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
	List<Announcement> findAllByCourseId(Long courseId);
}
