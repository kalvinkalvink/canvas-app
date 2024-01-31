package canvas.canvasapp.event.publisher.setting;

import canvas.canvasapp.event.setting.CanvasApiIInfoUpdatedEvent;
import canvas.canvasapp.helpers.type.setting.SettingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SettingEventPublisher {
	@Autowired
	ApplicationEventPublisher eventPublisher;

	public void publishEvent(Object source, SettingEvent settingEvent){
		switch (settingEvent){
			case CANVAS_API_INFO_UPDATED -> eventPublisher.publishEvent(new CanvasApiIInfoUpdatedEvent(source));
		}
	}
}
