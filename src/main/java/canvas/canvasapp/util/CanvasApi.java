package canvas.canvasapp.util;
import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.interfaces.CanvasReader;
import edu.ksu.canvas.interfaces.CanvasWriter;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;
import edu.ksu.canvas.oauth.OauthToken;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class CanvasApi {
	private static OauthToken oauthToken;
	private static CanvasApiFactory apiFactory;

	private CanvasApi() {
		String canvasBaseUrl = "https://canvas.cityu.edu.hk";
		oauthToken = new NonRefreshableOauthToken("1839~gQ2SiPEvVevHC2pJNTk7UUDkhjrjth1PS49wuUoWcyV1zxuewgHFwImA9WTvPAmI");
		apiFactory = new CanvasApiFactory(canvasBaseUrl);
	}

	public <T extends CanvasReader> T getReader(Class<T> type) {
		return apiFactory.getReader(type, oauthToken);
	}

	public <T extends CanvasWriter> T getWriter(Class<T> type) {
		return apiFactory.getWriter(type, oauthToken);
	}
}

