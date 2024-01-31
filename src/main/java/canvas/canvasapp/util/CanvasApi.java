package canvas.canvasapp.util;

import canvas.canvasapp.event.publisher.setting.SettingEventPublisher;
import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.interfaces.CanvasReader;
import edu.ksu.canvas.interfaces.CanvasWriter;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;
import edu.ksu.canvas.oauth.OauthToken;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CanvasApi {
	@Getter
	private boolean isInitialized = false;
	private static OauthToken oauthToken;
	private static CanvasApiFactory apiFactory;
	@Autowired
	private SettingEventPublisher settingEventPublisher;

	public void init(String canvasBaseUrl, String apiToken) {
		oauthToken = new NonRefreshableOauthToken(apiToken);
		apiFactory = new CanvasApiFactory(canvasBaseUrl);
		isInitialized = true;
	}

	public <T extends CanvasReader> T getReader(Class<T> type) {
		return apiFactory.getReader(type, oauthToken);
	}

	public <T extends CanvasWriter> T getWriter(Class<T> type) {
		return apiFactory.getWriter(type, oauthToken);
	}
}

