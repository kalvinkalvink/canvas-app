package canvas.canvasapp.service.application;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class FileDownloadService {
	public void downloadFile(String fileUrl, String savePath, boolean autoCreateParentDirectory) {
		log.trace("Downloading file to path: {}", savePath);
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpGet httpGet = new HttpGet(fileUrl);
			try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
				byte[] data = EntityUtils.toByteArray(response.getEntity());
				try (FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {
					if (autoCreateParentDirectory) {
						Path parentDir = Paths.get(savePath).getParent();
						Files.createDirectories(parentDir);
					}
					fileOutputStream.write(data);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
