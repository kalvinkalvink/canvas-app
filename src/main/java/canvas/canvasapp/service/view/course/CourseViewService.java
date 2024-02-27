package canvas.canvasapp.service.view.course;

import canvas.canvasapp.event.view.CourseItemClickEvent;
import canvas.canvasapp.model.db.Course;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

@Controller
public class CourseViewService {
	@Autowired
	ApplicationEventPublisher applicationEventPublisher;
	@Getter
	private Course course;

	public void publishCourseItemClickedEvent(Object source, Course selectedCourse) {
		course = selectedCourse;
		applicationEventPublisher.publishEvent(new CourseItemClickEvent(source, selectedCourse));
	}
}
