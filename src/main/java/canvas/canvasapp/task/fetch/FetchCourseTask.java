package canvas.canvasapp.task.fetch;

import canvas.canvasapp.service.database.CourseService;
import canvas.canvasapp.util.CanvasApi;
import canvas.canvasapp.util.predicate.DistinctByPredicate;
import edu.ksu.canvas.interfaces.CourseReader;
import edu.ksu.canvas.model.Course;
import edu.ksu.canvas.requestOptions.ListCurrentUserCoursesOptions;
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
public class FetchCourseTask implements Runnable {
	@Autowired
	CourseService courseService;
	@Autowired
	CanvasApi canvasApi;


	@Override
	public void run() {
		if(!canvasApi.isInitialized())return;

		try {
			log.info("Fetching courses data from canvas");
			log.debug("Course database size: {}", courseService.count());

			CourseReader courseReader = canvasApi.getReader(CourseReader.class);
			List<Course> canvasCourseList = courseReader.listCurrentUserCourses(new ListCurrentUserCoursesOptions());
			log.debug("Fetched {} courses from canvas", canvasCourseList.size());

//			canvasCourseList.stream().forEach(canvasCoruse->{
//				System.out.printf("%d, %s, %s\n",canvasCoruse.getId(), canvasCoruse.getName(), canvasCoruse.getCourseCode());
//			});

//			List<canvas.canvasapp.model.db.Course> all = courseRepository.findAll();
//			all.forEach(course -> {
//				System.out.printf("%d %s %s\n", course.getId(), course.getName(), course.getSelected());
//			});

			List<canvas.canvasapp.model.db.Course> courseList = canvasCourseList.stream()
					.filter(canvasCourse -> Objects.nonNull(canvasCourse.getName()) && Objects.nonNull(canvasCourse.getId()))
					.filter(canvasCourse -> courseService.findById(canvasCourse.getId()).isEmpty())
					.filter(canvasCourse -> courseService.findByName(canvasCourse.getName()).isEmpty())
					.filter(DistinctByPredicate.distinctBy(Course::getName))	// filter duplicated course name
					.peek(canvasCourse -> log.debug("Added {} {} to db", canvasCourse.getId(), canvasCourse.getName()))
					.map(canvasCourse -> new canvas.canvasapp.model.db.Course()
							.setId(canvasCourse.getId())
							.setName(canvasCourse.getName())
							.setCourseCode(canvasCourse.getCourseCode())
							.setSelected(false)
							.setSynced(false)
					).distinct()
					.collect(Collectors.toList());
			courseService.saveAll(courseList);
			courseService.publishUpdateEvent();
		} catch (IOException e) {
			log.error("Failed to fetch courses", e);
		}

	}
}
