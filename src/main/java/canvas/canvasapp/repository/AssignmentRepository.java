package canvas.canvasapp.repository;

import canvas.canvasapp.model.db.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
	List<Assignment> findByCourseId(Long courseId);
}
