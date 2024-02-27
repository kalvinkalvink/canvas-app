package canvas.canvasapp.type.view;

public enum ScenePath {
	DASHBOARD("/view/component/tab/dashboard.fxml"),
	COURSE("/view/component/tab/course.fxml"),
	FILES("/view/files.fxml");

	private final String path;

	ScenePath(String path) {
		this.path = path;
	}
	public String getPath(){
		return path;
	}
}
