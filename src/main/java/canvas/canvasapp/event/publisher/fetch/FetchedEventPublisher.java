package canvas.canvasapp.event.publisher.fetch;

import canvas.canvasapp.event.task.fetch.AssignmentFetchedEvent;
import canvas.canvasapp.event.task.fetch.CourseFetchedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class FetchedEventPublisher {

	private final ApplicationEventPublisher eventPublisher;
	public enum FetchEventType {
		COURSE_FETCH,
		ASSIGNMENT_FETCH
	}

	public FetchedEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	public void publishEvent(Object source, FetchEventType fetchEventType){
		switch (fetchEventType){
			case COURSE_FETCH -> eventPublisher.publishEvent(new CourseFetchedEvent(source));
			case ASSIGNMENT_FETCH -> eventPublisher.publishEvent(new AssignmentFetchedEvent(source));
		}
	}
}
