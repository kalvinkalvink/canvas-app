package canvas.canvasapp.task.fetch;

import canvas.canvasapp.controller.view.PreferenceController;
import canvas.canvasapp.dao.AssignmentRepository;
import canvas.canvasapp.dao.CourseRepository;
import canvas.canvasapp.event.publisher.fetch.FetchedEventPublisher;
import canvas.canvasapp.util.CanvasApi;
import canvas.canvasapp.util.predicate.DistinctByPredicate;
import edu.ksu.canvas.interfaces.CourseReader;
import edu.ksu.canvas.model.Course;
import edu.ksu.canvas.requestOptions.ListCurrentUserCoursesOptions;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Scope("prototype")
public class FetchCourseTask extends Task<Void> {
	@Autowired
	CourseRepository courseRepository;
	@Autowired
	AssignmentRepository assignmentRepository;
	@Autowired
	CanvasApi canvasApi;
	@Autowired
	PreferenceController preferenceController;
	@Autowired
	FetchedEventPublisher fetchedEventPublisher;

	@Override
	protected Void call() {
		try {
			log.info("Fetching courses");
			log.info("Course database size: {}", courseRepository.count());

			CourseReader courseReader = canvasApi.getReader(CourseReader.class);
			List<Course> canvasCourseList = courseReader.listCurrentUserCourses(new ListCurrentUserCoursesOptions());
			log.info("Fetched {} courses from canvas", canvasCourseList.size());

//			canvasCourseList.stream().forEach(canvasCoruse->{
//				System.out.printf("%d, %s, %s\n",canvasCoruse.getId(), canvasCoruse.getName(), canvasCoruse.getCourseCode());
//			});

//			List<canvas.canvasapp.model.Course> all = courseRepository.findAll();
//			all.forEach(course -> {
//				System.out.printf("%d %s %s\n", course.getId(), course.getName(), course.getSelected());
//			});

			List<canvas.canvasapp.model.Course> courseList = canvasCourseList.stream()
					.filter(canvasCourse -> Objects.nonNull(canvasCourse.getName()) && Objects.nonNull(canvasCourse.getId()))
					.filter(canvasCourse -> courseRepository.findById(canvasCourse.getId()).isEmpty())
					.filter(canvasCourse -> courseRepository.findByName(canvasCourse.getName()).isEmpty())
					.filter(DistinctByPredicate.distinctBy(Course::getName))	// filter duplicated course name
					.peek(canvasCourse -> log.debug("Added {} {} to db", canvasCourse.getId(), canvasCourse.getName()))
					.map(canvasCourse -> new canvas.canvasapp.model.Course()
							.setId(canvasCourse.getId())
							.setName(canvasCourse.getName())
							.setCourseCode(canvasCourse.getCourseCode())
							.setSelected(false)
					).distinct()
					.collect(Collectors.toList());
			courseRepository.saveAll(courseList);
		} catch (IOException e) {
			log.error("Failed to fetch courses", e);
		}

		// fire course fetched event
		fetchedEventPublisher.publishEvent(this, FetchedEventPublisher.FetchEventType.COURSE_FETCH);
		return null;
	}


}
