package canvas.canvasapp.controller.view;

import canvas.canvasapp.helpers.ScenePath;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Controller;

import java.io.IOException;
@Controller
public class SceneController {
	private final FxWeaver fxWeaver;
	private static Parent main;

	public SceneController(FxWeaver fxWeaver) {
		this.fxWeaver = fxWeaver;
	}

	public static void getInitialScene(Stage stage) throws IOException {
		main = FXMLLoader.load(SceneController.class.getResource(ScenePath.DASHBOARD.getPath()));
		Scene scene = new Scene(main);
		stage.setTitle("Canvas App");
		stage.setScene(scene);
		stage.show();
	}


	private static void changeScreen(ActionEvent event, String path) throws IOException {
		main = FXMLLoader.load(SceneController.class.getResource(path));
		Scene visitScene = new Scene(main);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(visitScene);
		window.show();
	}

	public static void showDashboardScene(ActionEvent event) throws IOException {
		changeScreen(event, ScenePath.DASHBOARD.getPath());
	}

	public static void showCourseScene(ActionEvent event) throws IOException {
		changeScreen(event, ScenePath.COURSE.getPath());
	}

	public static void showFilesScene(ActionEvent event) throws IOException {
		changeScreen(event, ScenePath.FILES.getPath());
	}


}
