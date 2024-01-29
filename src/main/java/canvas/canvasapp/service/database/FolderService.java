package canvas.canvasapp.service.database;

import canvas.canvasapp.event.publisher.database.DatabaseUpdatedEventPublisher;
import canvas.canvasapp.model.Folder;
import canvas.canvasapp.repository.FolderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FolderService implements IDatabaseUpdateEvent {
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private DatabaseUpdatedEventPublisher databaseUpdatedEventPublisher;

	// getter
	public List<Folder> findAll() {
		return folderRepository.findAll();
	}

	public List<Folder> findByCourseId(Long courseId) {
		return folderRepository.findByCourseId(courseId);
	}

	public Optional<Folder> findById(Long folderId) {
		return folderRepository.findById(folderId);
	}

	// setter
	public void saveAll(List<Folder> folderList) {
		folderRepository.saveAll(folderList);
	}

	@Override
	public void publishUpdateEvent() {
		databaseUpdatedEventPublisher.publishEvent(this, DatabaseUpdatedEventPublisher.UpdateEventType.FOLDER_UPDATED);
	}
}
