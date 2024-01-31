package canvas.canvasapp.service.application;

import canvas.canvasapp.task.executor.FileFixedThreadPoolExecutor;
import canvas.canvasapp.task.file.FileDownloadTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CanvasFileService {
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private FileFixedThreadPoolExecutor fileFixedThreadPoolExecutor;

	public void downloadFile(String fileUrl, String savePath, boolean autoCreateParentDirectory) {
		FileDownloadTask fileDownloadTask = applicationContext.getBean(FileDownloadTask.class);
		fileDownloadTask.setDownloadOption(fileUrl, savePath, autoCreateParentDirectory);
		fileFixedThreadPoolExecutor.executeTask(fileDownloadTask);
	}
}
