package canvas.canvasapp.controller.view.course.announcement;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Slf4j
@Controller
@FxmlView("/view/component/course/announcement/announcement-card.fxml")
public class CourseAnnouncementCardController {
	@FXML
	private Label announcementTitleLabel;
	@FXML
	private Text postedOnText;

	@FXML
	private void initialize() {


	}

	public void setAnnouncementTitleLabel(String title) {
		announcementTitleLabel.setText(title);
	}

	public void setPostedOnText(Date postedOn) {
		postedOnText.setText(postedOn.toString());
	}
}
