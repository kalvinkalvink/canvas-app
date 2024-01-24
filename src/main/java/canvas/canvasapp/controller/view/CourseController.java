package canvas.canvasapp.controller.view;

import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@FxmlView("/view/component/tab/course.fxml")
public class CourseController{
	@Autowired
	SceneController sceneController;

	private final FxWeaver fxWeaver;

	public CourseController(FxWeaver fxWeaver) {
		this.fxWeaver = fxWeaver;
	}




}
