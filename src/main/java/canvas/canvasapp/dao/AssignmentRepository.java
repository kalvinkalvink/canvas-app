package canvas.canvasapp.dao;

import canvas.canvasapp.model.Assignment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends CrudRepository<Assignment, Long> {
}
