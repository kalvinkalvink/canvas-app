package canvas.canvasapp.controller.view.guide;

import canvas.canvasapp.model.application.guide.Guide;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Controller
@FxmlView("/view/component/guide/guide.fxml")
public class UserGuideController {
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
	// guide list
	private String basePath = "/images/guide/";
	private List<Guide> guideList;
	private Guide selectedGuide;
	private int currentImageGuideIndex;
	private List<File> guideImageList;

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
		guideImageList = getGuideImageList(selectedGuide);
		currentImageGuideIndex = 0;
	}

	@FXML
	void initialize() throws FileNotFoundException {
		// add navigate button to guide list
		guideList.forEach(guide -> {
			Button guideButton = new Button(guide.getName());
			guideButton.setPrefWidth(Double.MAX_VALUE);
			guideButton.setOnAction(event -> {
				// set current selected guide
				selectedGuide = guide;
				try {
					// scan guide folder for guide
					guideImageList = getGuideImageList(guide);
					// set init guide image
					if (!guideImageList.isEmpty()) {        // display image if not empty
						displayImage();
					}
				} catch (IOException | URISyntaxException e) {
					log.error(String.format("Failed to load %s image %d", guide.getName(), currentImageGuideIndex), e);
				}
			});
			guideListVBox.getChildren().add(guideButton);
		});


		// set image counter to front
		imageCounterText.toFront();
		displayImage();
	}

	private List<File> getGuideImageList(Guide guide) throws URISyntaxException, IOException {
		String folderPath = basePath + guide.getGuideFolderName();
		URL uri = getClass().getResource(folderPath);
		if (Objects.isNull(uri)) return new ArrayList<>();
		Path guideFolderPath = Paths.get(uri.toURI());
		try (Stream<Path> entries = Files.walk(guideFolderPath, 1)) {
			return entries.filter(path -> path.toString().contains(".png"))
					.map(Path::toFile)
					.toList();
		}
	}

	private void displayImage() throws FileNotFoundException {
		if (guideImageList.isEmpty()) {
			log.warn("No guide image for guide {}", selectedGuide.getName());
			return;
		}
		File imageFile = guideImageList.get(currentImageGuideIndex);
		FileInputStream fileInputStream = new FileInputStream(imageFile);
		Image image = new Image(fileInputStream);
		imageView.setImage(image);

		// set iamge counter
		imageCounterText.setText(String.format("%d/%d", currentImageGuideIndex + 1, guideImageList.size()));
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
		if (currentImageGuideIndex >= guideImageList.size() - 1) {
			return;
		}
		// set image
		currentImageGuideIndex += 1;
		displayImage();
	}
}
