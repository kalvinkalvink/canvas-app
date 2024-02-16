package canvas.canvasapp.task.schedule.fetch;

import canvas.canvasapp.model.Course;
import canvas.canvasapp.service.database.AnnouncementService;
import canvas.canvasapp.service.database.CourseService;
import canvas.canvasapp.util.CanvasApi;
import edu.ksu.canvas.interfaces.AnnouncementReader;
import edu.ksu.canvas.model.announcement.Announcement;
import edu.ksu.canvas.requestOptions.ListCourseAnnouncementOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Scope("prototype")
public class FetchSelectedCourseAnnouncementTask implements Runnable {
	@Autowired
	CourseService courseService;
	@Autowired
	AnnouncementService announcementService;
	@Autowired
	CanvasApi canvasApi;

	@Override
	public void run() {
		if (!canvasApi.isInitialized()) return;
		log.info("Fetching selected course annnouncement data from canvas");
		List<Course> selectedCourseList = courseService.findAllSelected();
		List<canvas.canvasapp.model.Announcement> fetchedAnnouncementList = new ArrayList<>();
		selectedCourseList.stream()
				.map(selectedCourse -> {
					try {
						AnnouncementReader announcementReader = canvasApi.getReader(AnnouncementReader.class);
						return announcementReader.listCourseAnnouncement(new ListCourseAnnouncementOptions(selectedCourse.getId().toString()));
					} catch (IOException e) {
						log.error("Error fetching course {} assignment", selectedCourse.getName());
						return new ArrayList<Announcement>();
					}
				}).flatMap(List::stream)
				.map(canvasAnnouncement -> new canvas.canvasapp.model.Announcement()
						.setId(canvasAnnouncement.getId())
						.setTitle(canvasAnnouncement.getTitle())
						.setCreatedAt(canvasAnnouncement.getCreatedAt())
						.setLockAt(canvasAnnouncement.getLockAt())
						.setHtmlUrl(canvasAnnouncement.getHtmlUrl())
						.setUrl(canvasAnnouncement.getUrl())
						.setPostedAt(canvasAnnouncement.getPostedAt())
						.setPosition(canvasAnnouncement.getPosition())
						.setContextCode(canvasAnnouncement.getContextCode())
						.setCourse(courseService.findById(Long.parseLong(canvasAnnouncement.getContextCode().substring(canvasAnnouncement.getContextCode().indexOf("_") + 1))).get())
				)
				.forEach(fetchedAnnouncementList::add);
		announcementService.saveAll(fetchedAnnouncementList);
		announcementService.publishUpdateEvent();
	}
}
