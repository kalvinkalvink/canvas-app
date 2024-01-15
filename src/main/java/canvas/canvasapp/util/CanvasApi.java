package canvas.canvasapp.util;
import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.interfaces.CanvasReader;
import edu.ksu.canvas.interfaces.CanvasWriter;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;
import edu.ksu.canvas.oauth.OauthToken;

public class CanvasApi {
	private static CanvasApi instance;
	private static CanvasApiFactory apiFactory;
	private static OauthToken oauthToken;

	private CanvasApi() {
		String canvasBaseUrl = "https://canvas.cityu.edu.hk";
		oauthToken = new NonRefreshableOauthToken("1839~gQ2SiPEvVevHC2pJNTk7UUDkhjrjth1PS49wuUoWcyV1zxuewgHFwImA9WTvPAmI");
		apiFactory = new CanvasApiFactory(canvasBaseUrl);
	}

	public static CanvasApi getInstance() {
		if (instance == null) {
			instance = new CanvasApi();
		}
		return instance;
	}

	public <T extends CanvasReader> T getReader(Class<T> type) {
		return apiFactory.getReader(type, oauthToken);
	}

	public <T extends CanvasWriter> T getWriter(Class<T> type) {
		return apiFactory.getWriter(type, oauthToken);
	}
}

