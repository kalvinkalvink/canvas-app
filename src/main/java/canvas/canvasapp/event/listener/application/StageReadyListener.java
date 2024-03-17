package canvas.canvasapp.event.listener.application;

import canvas.canvasapp.controller.view.MainController;
import canvas.canvasapp.event.application.StageReadyEvent;
import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class StageReadyListener implements ApplicationListener<StageReadyEvent> {
	private final String applicationTitle;

	@Autowired
	private FxWeaver fxWeaver;
	@Autowired
	private ResourceLoader resourceLoader;


	public StageReadyListener(@Value("${spring.application.ui.title}") String applicationTitle) {
		this.applicationTitle = applicationTitle;
	}

	@Override
	public void onApplicationEvent(StageReadyEvent event) {
		// set headless mode to false for system tray to work
		System.setProperty("java.awt.headless", "false");


		// setup stage
		Stage stage = event.getStage();
		Scene scene = new Scene(fxWeaver.loadView(MainController.class), 1000, 600);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/CanvasLogo.png")));
		stage.setTitle("Canvas Desktop App");

		// setup tray icon
		FXTrayIcon fxTrayIcon = new FXTrayIcon(stage, getClass().getClassLoader().getResource("images/CanvasLogo.png"));
		fxTrayIcon.setOnClick(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {		// show and hide main window when tray icon is pressed
				if (stage.isShowing()) stage.hide();
				else stage.show();
			}
		});
		fxTrayIcon.show();

		stage.setScene(scene);
		stage.show();
	}
}
