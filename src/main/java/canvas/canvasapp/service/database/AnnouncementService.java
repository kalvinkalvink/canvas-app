package canvas.canvasapp.service.database;

import canvas.canvasapp.event.publisher.database.DatabaseUpdatedEventPublisher;
import canvas.canvasapp.model.db.Announcement;
import canvas.canvasapp.repository.AnnouncementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AnnouncementService implements IDatabaseUpdateEvent {
	@Autowired
	AnnouncementRepository announcementRepository;
	@Autowired
	DatabaseUpdatedEventPublisher databaseUpdatedEventPublisher;

	public void add(Announcement announcement) {
		announcementRepository.save(announcement);
	}

	public void saveAll(List<Announcement> announcementList) {
		announcementRepository.saveAll(announcementList);
	}

	public Optional<Announcement> findById(Long id) {
		return announcementRepository.findById(id);
	}

	public List<Announcement> findAllByCourseId(Long courseId) {
		return announcementRepository.findAllByCourseId(courseId);
	}

	@Override
	public void publishUpdateEvent() {
		databaseUpdatedEventPublisher.publishEvent(this, DatabaseUpdatedEventPublisher.UpdateEventType.ANNOUNCEMENT_UPDATED);
	}
}
