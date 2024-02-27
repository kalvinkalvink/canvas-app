package canvas.canvasapp.type.application;

import lombok.Getter;

public enum AppSetting {
	// course sync
	SYNC_COURSE(join("course", "sync")),
	COURSE_SYNC_FOLDER_PATH(join("course", "sync", "folder", "path")),
	COURSE_SYNC_INTERVAL(join("course", "sync", "interval")),
	AUTO_CONVERT_DOC_TO_PDF(join("document", "convert", "auto")),
	// canvas api
	CANVAS_BASE_URL(join("canvas", "api", "url")),
	CANVAS_API_TOKEN(join("canvas", "api", "token"));

	private static final String AppName = "canvasapp";
	@Getter
	private final String key;

	private static String join(String... path) {
		return AppName + "." + String.join(".", path);
	}

	// setting key
	AppSetting(String key) {
		this.key = key;
	}

}
