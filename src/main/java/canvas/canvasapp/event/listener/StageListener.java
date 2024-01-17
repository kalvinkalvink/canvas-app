package canvas.canvasapp.event.listener;

import canvas.canvasapp.event.StageReadyEvent;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {
	private final String applicationTitle;


	@Autowired
	public StageListener(@Value("${spring.application.ui.title}") String applicationTitle) {
		this.applicationTitle = applicationTitle;
	}

	@Override
	public void onApplicationEvent(StageReadyEvent event) {
		event.getStage();
	}
}
