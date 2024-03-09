package canvas.canvasapp.controller.view.course.assignment;

import canvas.canvasapp.CanvasApp;
import canvas.canvasapp.model.application.HttpResponse;
import canvas.canvasapp.service.application.CanvasFileService;
import canvas.canvasapp.util.CanvasApi;
import canvas.canvasapp.util.DateFormatterUtil;
import edu.ksu.canvas.model.assignment.Assignment;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@FxmlView("/view/component/course/assignment/course-assignment-details.fxml")
@Controller
public class CourseAssignmentDetailsController {
	@FXML
	Text assignmentNameText;
	@FXML
	HBox hasSubmittedHBox;
	@FXML
	HBox dueHBox;
	@FXML
	HBox submissionTypesHBox;
	@FXML
	HBox allowableFileTypesHBox;
	@FXML
	WebView descriptionWebview;
	@FXML
	Button submitButton;
	@Setter
	private Assignment assignment;
	@Autowired
	private DateFormatterUtil dateFormatterUtil;
	@Autowired
	private CanvasApi canvasApi;
	@Autowired
	private CanvasFileService canvasFileService;


	public void initView() {
		log.info("Initializing course assignment {} details controller", assignment.getName());

		// init view
		assignmentNameText.setText(assignment.getName());
		// has submitted
		Boolean hasSubmittedSubmissions = assignment.getHasSubmittedSubmissions();
		hasSubmittedHBox.getChildren().add(new Text(hasSubmittedSubmissions.toString()));
		// due

		dueHBox.getChildren().add(new Text(dateFormatterUtil.format(assignment.getDueAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())));
		// submissionType
		List<String> submissionTypeList = assignment.getSubmissionTypes();
		submissionTypesHBox.getChildren().add(new Text(String.join(",", submissionTypeList)));
		// allowedFileType
		String[] allowedExtensionList = assignment.getAllowedExtensions();
		if (Objects.nonNull(allowedExtensionList))
			allowableFileTypesHBox.getChildren().add(new Text(String.join(",", allowedExtensionList)));
		else allowableFileTypesHBox.getChildren().add(new Text("N/A"));

		// webview
		WebEngine descriptionWebviewEngine = descriptionWebview.getEngine();
		descriptionWebviewEngine.loadContent(assignment.getDescription());
		descriptionWebviewEngine.locationProperty().addListener(new ChangeListener<String>() {        // handle download change
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String oldLoc, String newLoc) {
				CanvasApp.hostServices.showDocument(newLoc);
				descriptionWebviewEngine.getLoadWorker().cancel();
			}
		});
	}

	@FXML
	void submitButtonClicked() {
		// show choose file menu
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose Assignment File");
		File file = fileChooser.showOpenDialog(null);

		// upload file
		try {
			Future<HttpResponse> submitAssignmentFuture = canvasFileService.submitAssignment(file, assignment.getCourseId(), assignment.getId().toString());
			HttpResponse uploadAssignemntClosableHttpResponse = submitAssignmentFuture.get();
			int statusCode = uploadAssignemntClosableHttpResponse.getStatusCode();
			Alert uploadAssignmentAlert = new Alert(Alert.AlertType.INFORMATION);
			uploadAssignmentAlert.setTitle("upload Assignment");
			if (statusCode >= 200 && statusCode < 300) {
				uploadAssignmentAlert.setHeaderText("Upload assignment successfully");
				uploadAssignmentAlert.setContentText("Upload success");
			} else {
				uploadAssignmentAlert.setAlertType(Alert.AlertType.ERROR);
				uploadAssignmentAlert.setHeaderText("Upload assignment failed");
				String response = EntityUtils.toString(uploadAssignemntClosableHttpResponse.getHttpEntity());
				uploadAssignmentAlert.setContentText(response);
			}

			uploadAssignmentAlert.showAndWait();
		} catch (InterruptedException | ExecutionException | IOException e) {
			log.error(String.format("Error while trying to upload assignemnt %s file", assignment.getName()), e);
		}
	}
}
