package canvas.canvasapp.event.listener.application;

import canvas.canvasapp.controller.view.MainController;
import canvas.canvasapp.event.application.StageReadyEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {
	private final String applicationTitle;

	@Autowired
	private FxWeaver fxWeaver;

	public StageListener(@Value("${spring.application.ui.title}") String applicationTitle) {
		this.applicationTitle = applicationTitle;
	}

	@Override
	public void onApplicationEvent(StageReadyEvent event) {
		Stage stage = event.getStage();

		Scene scene = new Scene(fxWeaver.loadView(MainController.class));
		stage.setScene(scene);
		stage.show();
	}
}