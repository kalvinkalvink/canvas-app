package canvas.canvasapp.controller;

import canvas.canvasapp.controller.view.guide.UserGuideController;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import canvas.canvasapp.task.executor.FixedThreadPoolExecutor;
import canvas.canvasapp.task.startup.StartupTask;
import canvas.canvasapp.type.application.AppSetting;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutionException;

@Slf4j
@Controller
public class StartupController {
    @Autowired
	StartupTask startupTask;
	@Autowired
	FixedThreadPoolExecutor fixedThreadPoolExecutor;
	@Autowired
	private FxWeaver fxWeaver;
	@Autowired
	private CanvasPreferenceService canvasPreferenceService;
    public void initApplication() {
        try {

            fetchDataFromCanvas();
			showStartupGuide();
        } catch (ExecutionException | InterruptedException e) {
            log.error("Failed to init application", e);
        }
	}

	private void fetchDataFromCanvas() throws ExecutionException, InterruptedException {
		fixedThreadPoolExecutor.executeTask(startupTask);
    }

	private void showStartupGuide() {
		if (!canvasPreferenceService.get(AppSetting.NO_SHOW_START_UP_GUIDE, false)) {
			Platform.runLater(()->{
				Stage userGuideStage = new Stage();
				Node userGuideNode = fxWeaver.loadView(UserGuideController.class);
				userGuideStage.setScene(new Scene((Parent) userGuideNode));
				userGuideStage.show();
			});
		}
	}
}
