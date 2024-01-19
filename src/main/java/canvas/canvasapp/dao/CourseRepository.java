package canvas.canvasapp.dao;

import canvas.canvasapp.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByNameAndSelectedIsTrue(String name);
}
