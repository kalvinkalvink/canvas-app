package canvas.canvasapp.event.view;

import canvas.canvasapp.helpers.type.view.course.CoursePage;
import canvas.canvasapp.model.Course;
import org.springframework.context.ApplicationEvent;

public class CourseItemClickEvent extends ApplicationEvent {
    private final Course course;

    public CourseItemClickEvent(Object source, Course coursePage) {
        super(source);
        this.course = coursePage;
    }

    public Course getCourse() {
        return course;
    }
}
