package canvas.canvasapp.service.application;

import canvas.canvasapp.model.application.network.HttpResponse;
import canvas.canvasapp.task.executor.FixedThreadPoolExecutor;
import canvas.canvasapp.task.file.FileDownloadTask;
import canvas.canvasapp.task.file.SubmitAssignmentTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Slf4j
@Service
public class CanvasFileService {
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private FixedThreadPoolExecutor fixedThreadPoolExecutor;

	public CompletableFuture<Void> downloadFile(String fileUrl, String savePath, boolean autoCreateParentDirectory) {
		FileDownloadTask fileDownloadTask = applicationContext.getBean(FileDownloadTask.class);
		fileDownloadTask.setDownloadOption(fileUrl, savePath, autoCreateParentDirectory);
		return CompletableFuture.runAsync(fileDownloadTask);
	}
	public Future<HttpResponse> submitAssignment(File fileToupload, String courseId, String assignmentId){
		SubmitAssignmentTask submitAssignmentTask = applicationContext.getBean(SubmitAssignmentTask.class);
		submitAssignmentTask.setUploadOptions(fileToupload, courseId, assignmentId);
		return fixedThreadPoolExecutor.submitTask(submitAssignmentTask);
	}
}
