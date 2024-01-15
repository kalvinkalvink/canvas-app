package canvas.canvasapp;

import canvas.canvasapp.controller.StartupController;
import canvas.canvasapp.controller.view.SceneController;
import canvas.canvasapp.util.HibernateUtil;
import javafx.application.Application;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.io.IOException;

public class CanvasApp extends Application {
	@Override
	public void init() throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.close();
		StartupController.initApplication();
	}

	@Override
	public void start(Stage stage) throws IOException {
		SceneController.getInitialScene(stage);
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		HibernateUtil.shutdown();
	}

	public static void main(String[] args) {
		launch();
	}
}