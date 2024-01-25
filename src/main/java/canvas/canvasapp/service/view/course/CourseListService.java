package canvas.canvasapp.service.view.course;

import canvas.canvasapp.event.view.CourseItemClickEvent;
import canvas.canvasapp.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

@Controller
public class CourseListService {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    private Course course;

    public void publishCourseItemClickedEvent(Object source, Course selectedCourse) {

        applicationEventPublisher.publishEvent(new CourseItemClickEvent(source, selectedCourse));
    }
}
