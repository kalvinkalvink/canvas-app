package canvas.canvasapp.service.application;

import canvas.canvasapp.helpers.type.application.AppSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.prefs.Preferences;

@Slf4j
@Service
public class CanvasPreferenceService {
	private final Preferences canvasPreference;

	private CanvasPreferenceService() {
		this.canvasPreference = Preferences.userNodeForPackage(getClass());
	}
	// setter

	public void store(AppSetting appSetting, String value) {
		canvasPreference.put(appSetting.getKey(), value);
	}

	public void store(AppSetting appSetting, Boolean value) {
		canvasPreference.putBoolean(appSetting.getKey(), value);
	}

	public void store(AppSetting appSetting, Integer value) {
		canvasPreference.putInt(appSetting.getKey(), value);
	}

	public void store(AppSetting appSetting, Double value) {
		canvasPreference.putDouble(appSetting.getKey(), value);
	}

	public void store(AppSetting appSetting, Long value) {
		canvasPreference.putLong(appSetting.getKey(), value);
	}

	// getter
	public String get(AppSetting appSetting, String defaultValue) {
		return canvasPreference.get(appSetting.getKey(), defaultValue);
	}

	public Boolean get(AppSetting appSetting, Boolean defaultValue) {
		return canvasPreference.getBoolean(appSetting.getKey(), defaultValue);
	}

	public Integer get(AppSetting appSetting, Integer defaultValue) {
		return canvasPreference.getInt(appSetting.getKey(), defaultValue);
	}

	public Double get(AppSetting appSetting, Double defaultValue) {
		return canvasPreference.getDouble(appSetting.getKey(), defaultValue);
	}

	public Long get(AppSetting appSetting, Long defaultValue) {
		return canvasPreference.getLong(appSetting.getKey(), defaultValue);
	}
}
