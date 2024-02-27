package canvas.canvasapp.repository;

import canvas.canvasapp.model.db.Course;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
	//    Optional<Course> findByNameAndSelectedIsTrue(String name);
	@Transactional
	@Modifying
	@Query("update Course set selected = false")
	void setAllSelectedToFalse();

	List<Course> getBySelectedIsTrue();

	List<Course> getBySyncedIsTrue();

	Optional<Course> findByName(String name);
}
