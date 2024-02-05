package canvas.canvasapp.task.file;

import canvas.canvasapp.helpers.type.application.AppSetting;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
@Scope("prototype")
public class FileDownloadTask implements Runnable {
	@Autowired
	private CanvasPreferenceService canvasPreferenceService;
	private final boolean skipIfFileExist = true;

	private String fileUrl;
	private String savePath;
	private boolean autoCreateParentDirectory;

	public void setDownloadOption(String fileUrl, String savePath, boolean autoCreateParentDirectory) {
		this.fileUrl = fileUrl;
		this.savePath = savePath;
		this.autoCreateParentDirectory = autoCreateParentDirectory;
	}
	@Override
	public void run() {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

			Path filePath = Paths.get(savePath);
			if (skipIfFileExist && filePath.toFile().exists()) {
				log.debug("Skipping {} download because file already exist", filePath.getFileName());
				return;
			}
			if (autoCreateParentDirectory) {
				log.debug("Creating directory for path: {}", savePath);
				Files.createDirectories(filePath.getParent());
			}
			String apiToken = canvasPreferenceService.get(AppSetting.CANVAS_API_TOKEN, "");
			log.trace("Downloading file to path: {}", savePath);
			String bearerToken = "Bearer " + canvasPreferenceService.get(AppSetting.CANVAS_API_TOKEN, "");
			HttpGet httpGet = new HttpGet(fileUrl);
			httpGet.setHeader(HttpHeaders.AUTHORIZATION, bearerToken);

			try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
				byte[] data = EntityUtils.toByteArray(response.getEntity());
				try (FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {
					fileOutputStream.write(data);
				}
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
