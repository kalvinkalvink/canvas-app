package canvas.canvasapp.controller.view;

import canvas.canvasapp.controller.view.course.CourseController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SceneController {
	@Autowired
	FxWeaver fxWeaver;
	//	private final FxWeaver fxWeaver;
	@Setter
	private Stage mainStage;



	private void changeScene(Scene scene) {
		mainStage.setScene(scene);
		mainStage.show();
	}

	public void showDashboardTab() {
		Scene scene = new Scene(fxWeaver.loadView(DashboardController.class));
		changeScene(scene);

	}

	public void showCourseTab() {
		Scene scene = new Scene(fxWeaver.loadView(CourseController.class));
		changeScene(scene);
	}


//	@Autowired
//	public SceneController(FxWeaver fxWeaver) {
//		this.fxWeaver = fxWeaver;
//	}
//
//	public static void getInitialScene(Stage stage) throws IOException {
//		main = FXMLLoader.load(SceneController.class.getResource(ScenePath.DASHBOARD.getPath()));
//		Scene scene = new Scene(main);
//		stage.setTitle("Canvas App");
//		stage.setScene(scene);
//		stage.show();
//	}
//
//
//	private static void changeScreen(ActionEvent event, String path) throws IOException {
//		main = FXMLLoader.load(SceneController.class.getResource(path));
//		Scene visitScene = new Scene(main);
//		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
//		window.setScene(visitScene);
//		window.show();
//	}
//
//	public static void showDashboardScene(ActionEvent event) throws IOException {
//		changeScreen(event, ScenePath.DASHBOARD.getPath());
//	}
//
//	public static void showCourseScene(ActionEvent event) throws IOException {
//		changeScreen(event, ScenePath.COURSE.getPath());
//	}
//
//	public static void showFilesScene(ActionEvent event) throws IOException {
//		changeScreen(event, ScenePath.FILES.getPath());
//	}


}
