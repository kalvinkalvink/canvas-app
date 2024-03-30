package canvas.canvasapp.controller.view.guide;

import canvas.canvasapp.model.application.guide.Guide;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import canvas.canvasapp.type.application.AppSetting;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@FxmlView("/view/component/guide/guide.fxml")
public class UserGuideController {
	@FXML
	CheckBox noShowUserGuideCheckBox;
	@FXML
	Text imageCounterText;
	@FXML
	VBox guideListVBox;
	@FXML
	ImageView imageView;
	@FXML
	Button previousImageButton;
	@FXML
	Button nextImageButton;


	@Autowired
	private CanvasPreferenceService canvasPreferenceService;
	@Autowired
	private ResourceLoader resourceLoader;
	// guide list
	private String basePath = "images/guide/";
	private List<Guide> guideList;
	private Guide selectedGuide;
	private int currentImageGuideIndex;
	private int currentImageListLen;

	public UserGuideController() throws URISyntaxException, IOException {
		// setup guide
		Guide setupCanvasTokenGuide = new Guide()
				.setName("Setup Canvas Token")
				.setGuideFolderName("canvas-token");
		Guide setupCourseSyncGuide = new Guide()
				.setName("Setup Course Sync")
				.setGuideFolderName("course-sync");
		Guide setupFileSyncGuide = new Guide()
				.setName("Setup File Sync")
				.setGuideFolderName("file-sync");

		guideList = Arrays.asList(
				setupCanvasTokenGuide,
				setupCourseSyncGuide,
				setupFileSyncGuide
		);

		// scan guide folder for guide

		selectedGuide = guideList.getFirst();
//		guideImageList = getGuideImageList(selectedGuide);
		currentImageGuideIndex = 0;
	}

	@FXML
	void initialize() throws FileNotFoundException {
		// setup no show checkbox
		noShowUserGuideCheckBox.setSelected(canvasPreferenceService.get(AppSetting.NO_SHOW_START_UP_GUIDE, false));

		// add navigate button to guide list
		guideList.forEach(guide -> {
			Button guideButton = new Button(guide.getName());
			guideButton.setPrefWidth(Double.MAX_VALUE);
			guideButton.setOnAction(event -> {
				// set current selected guide
				selectedGuide = guide;
				currentImageGuideIndex = 0;
				displayImage();
			});
			guideListVBox.getChildren().add(guideButton);
			setImageZoom();
		});


		// set image counter to front
		imageCounterText.toFront();
		displayImage();        // setup scroll to zoom
	}

	private void setImageZoom() {
		imageView.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent scrollEvent) {
				double zoomFactor = 1.05;
				double deltaY = scrollEvent.getDeltaY();
				if (deltaY < 0) {
					zoomFactor = 0.95;
				}
				imageView.setFitWidth(imageView.getFitWidth() * zoomFactor);
				imageView.setFitHeight(imageView.getFitHeight() * zoomFactor);
			}
		});

	}


	private void displayImage() {
//		if (guideImageList.isEmpty()) {
//			log.warn("No guide image for guide {}", selectedGuide.getName());
//			return;
//		}
		log.trace("Displaying guide image {}", currentImageGuideIndex);

		ResourcePatternResolver resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		try {
			Resource[] resources = resourcePatternResolver.getResources("classpath:" + basePath + selectedGuide.getGuideFolderName() + "/*");
			InputStream imageInputStream = resources[currentImageGuideIndex].getInputStream();
			Image image = new Image(imageInputStream);
			imageView.setImage(image);
			// set iamge counter
			currentImageListLen = resources.length;
			imageCounterText.setText(String.format("%d/%d", currentImageGuideIndex + 1, resources.length));
		} catch (IOException e) {
			log.error(String.format("Failed to display image index %d", currentImageGuideIndex));
		}


	}

	public void previousImageButtonClicked(ActionEvent event) throws FileNotFoundException {
		// check if reached beginning
		if (currentImageGuideIndex <= 0) {
			return;
		}
		// set image
		currentImageGuideIndex -= 1;
		displayImage();
	}

	public void nextImageButtonClicked(ActionEvent event) throws FileNotFoundException {
		// check if reached end
		if (currentImageGuideIndex >= currentImageListLen - 1) {
			return;
		}
		// set image
		currentImageGuideIndex += 1;
		displayImage();
	}

	public void noShowUserGuideCheckBoxClicked(ActionEvent event) {
		log.debug("show startup guide checkbox pressed: {}", noShowUserGuideCheckBox.isSelected());
		canvasPreferenceService.store(AppSetting.NO_SHOW_START_UP_GUIDE, noShowUserGuideCheckBox.isSelected());
	}
}
