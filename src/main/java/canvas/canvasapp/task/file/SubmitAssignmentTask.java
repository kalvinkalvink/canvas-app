package canvas.canvasapp.task.file;


import canvas.canvasapp.model.application.network.HttpResponse;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import canvas.canvasapp.type.application.AppSetting;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

@Slf4j
@Component
@Scope("prototype")
public class SubmitAssignmentTask implements Callable<HttpResponse> {
	@Autowired
	private CanvasPreferenceService canvasPreferenceService;

	private File fileToUpload;
	private String courseId;
	private String assignmentId;

	public void setUploadOptions(File fileToUpload, String courseId, String assignmentId) {
		this.fileToUpload = fileToUpload;
		this.courseId = courseId;
		this.assignmentId = assignmentId;
	}


	@Override
	public HttpResponse call() throws Exception {
		System.out.println("course id: " + courseId);
		System.out.println("assignment id: " + assignmentId);
		String canvasBaseUrl = canvasPreferenceService.get(AppSetting.CANVAS_BASE_URL, "");
		String bearerToken = "Bearer " + canvasPreferenceService.get(AppSetting.CANVAS_API_TOKEN, "");
		String postUrl = canvasBaseUrl + "/api/v1/courses/" + courseId + "/assignments/" + assignmentId + "/submissions";

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			// building request
			HttpEntity data = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.addBinaryBody("upfile", fileToUpload, ContentType.DEFAULT_BINARY, fileToUpload.getName())
					.build();
			HttpUriRequest request = RequestBuilder.post(postUrl)
					.setHeader(HttpHeaders.AUTHORIZATION, bearerToken)
					.setEntity(data)
					.build();
			// execute request
			CloseableHttpResponse closableHttpClientResponse = httpClient.execute(request);
			closableHttpClientResponse.close();
			return new HttpResponse()
					.setStatusCode(closableHttpClientResponse.getStatusLine().getStatusCode())
					.setHttpEntity(closableHttpClientResponse.getEntity());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
