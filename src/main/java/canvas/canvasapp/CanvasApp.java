package canvas.canvasapp;

import canvas.canvasapp.controller.StartupController;
import canvas.canvasapp.controller.task.ThreadPoolController;
import canvas.canvasapp.event.application.StageReadyEvent;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class CanvasApp extends Application {
	private ConfigurableApplicationContext applicationContext;

	@Autowired
	StartupController startupController;
	@Autowired
	ThreadPoolController threadPoolController;

	@Override
	public void init() throws Exception {

		// init spring application context
		ApplicationContextInitializer<GenericApplicationContext> initializer = new ApplicationContextInitializer<GenericApplicationContext>() {
			@Override
			public void initialize(GenericApplicationContext applicationContext) {
				applicationContext.registerBean(Application.class, ()->CanvasApp.this);
				applicationContext.registerBean(Parameters.class, ()->getParameters());
				applicationContext.registerBean(HostServices.class, ()->getHostServices());

			}
		};
		applicationContext = new SpringApplicationBuilder(CanvasSpringApp.class)
				.sources(CanvasSpringApp.class)
				.initializers(initializer)
				.run(getParameters().getRaw().toArray(new String[0]));
		// init database
		startupController.initApplication();
	}

	@Override
	public void start(Stage stage) throws IOException {
		applicationContext.publishEvent(new StageReadyEvent(stage));
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		// spring
		applicationContext.stop();

		// thread pool
		threadPoolController.stopAllThreadPool();
		Platform.exit();
	}

	public static void main(String[] args) {
		launch();
	}


}