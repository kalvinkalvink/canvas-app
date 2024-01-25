package canvas.canvasapp.service.view.course;

import canvas.canvasapp.event.view.CourseItemClickEvent;
import canvas.canvasapp.helpers.type.view.course.CoursePage;
import canvas.canvasapp.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

import java.util.EnumMap;

@Controller
public class CourseItemService {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    private Course course;

    public void publishCourseItemClickedEvent(Object source, Course selectedCourse) {

        applicationEventPublisher.publishEvent(new CourseItemClickEvent(source, selectedCourse));
    }
}
