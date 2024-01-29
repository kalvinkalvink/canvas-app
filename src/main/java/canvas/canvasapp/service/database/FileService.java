package canvas.canvasapp.service.database;

import canvas.canvasapp.event.publisher.database.DatabaseUpdatedEventPublisher;
import canvas.canvasapp.model.File;
import canvas.canvasapp.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FileService implements IDatabaseUpdateEvent {
	@Autowired
	FileRepository fileRepository;
	@Autowired
	DatabaseUpdatedEventPublisher databaseUpdatedEventPublisher;

	// getter
	public List<File> findByCourseId(Long courseId) {
		return fileRepository.findByCourseId(courseId);
	}
	// setter
	public void saveAll(List<File> fileList) {
		fileRepository.saveAll(fileList);
	}

	@Override
	public void publishUpdateEvent() {
		databaseUpdatedEventPublisher.publishEvent(this, DatabaseUpdatedEventPublisher.UpdateEventType.FILE_UPDATED);
	}
}
