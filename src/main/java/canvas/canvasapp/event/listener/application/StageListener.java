package canvas.canvasapp.event.listener.application;

import canvas.canvasapp.controller.view.SceneController;
import canvas.canvasapp.event.application.StageReadyEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {
	private final String applicationTitle;

	@Autowired
	SceneController sceneController;

	public StageListener(@Value("${spring.application.ui.title}") String applicationTitle) {

		this.applicationTitle = applicationTitle;
	}

	@Override
	public void onApplicationEvent(StageReadyEvent event) {
		Stage stage = event.getStage();
		sceneController.setMainStage(stage);
		sceneController.showDashboardTab();
	}
}
