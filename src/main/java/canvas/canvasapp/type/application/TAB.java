package canvas.canvasapp.type.application;

import lombok.Getter;

public enum TAB {
	DASHBOARD_TAB("Dashboard"),
	COURSE_TAB("Course"),
	FILES_TAB("Files");
	@Getter
	private final String name;

	TAB(String tabName) {
		name = tabName;
	}
}
