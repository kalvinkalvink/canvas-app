package canvas.canvasapp.event.listener.setting;

import canvas.canvasapp.event.setting.CanvasApiIInfoUpdatedEvent;
import canvas.canvasapp.helpers.type.application.AppSetting;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import canvas.canvasapp.util.CanvasApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CanvasApiInfoUpdateEventListener implements ApplicationListener<CanvasApiIInfoUpdatedEvent> {
	@Autowired
	private CanvasApi canvasApi;
	@Autowired
	private CanvasPreferenceService canvasPreferenceService;

	@Override
	public void onApplicationEvent(CanvasApiIInfoUpdatedEvent event) {
		String canvasApiBaseUrl = canvasPreferenceService.get(AppSetting.CANVAS_BASE_URL, "");
		String canvasApiToken = canvasPreferenceService.get(AppSetting.CANVAS_API_TOKEN, "");
		if (!(canvasApiBaseUrl.isEmpty() || canvasApiToken.isEmpty())) {
			canvasApi.init(canvasApiBaseUrl, canvasApiToken);
		}
	}
}
