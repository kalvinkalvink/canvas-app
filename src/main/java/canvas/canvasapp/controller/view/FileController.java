package canvas.canvasapp.controller.view;

import canvas.canvasapp.CanvasApp;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import canvas.canvasapp.type.application.AppSetting;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;

@Slf4j
@Controller
@FxmlView("/view/component/tab/file.fxml")
public class FileController {
	@FXML
	TextField fileSearchTextField;
	@FXML
	TreeView<File> fileTreeView;

	@Autowired
	private CanvasPreferenceService canvasPreferenceService;
	@FXML
	private void initialize() {
		fileTreeView.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {

			@Override
			public TreeCell<File> call(TreeView<File> fileTreeView) {
				TreeCell<File> fileTreeCell = new TreeCell<>() {
					@Override
					protected void updateItem(File file, boolean empty) {
						super.updateItem(file, empty);
						setText((empty || file == null) ? "" : file.getName());
					}
				};
				// setting on click action
				fileTreeCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						if(mouseEvent.getClickCount() == 2){
							File file = fileTreeCell.getTreeItem().getValue();
							if(file.exists() && file.isFile()){
								CanvasApp.hostServices.showDocument(file.getPath());
							}
						}
					}
				});
				return fileTreeCell;
			}
		});
		String syncFolderBasePath = FilenameUtils.separatorsToSystem(canvasPreferenceService.get(AppSetting.COURSE_SYNC_FOLDER_PATH, ""));
		File rootFile = new File(syncFolderBasePath);
		TreeItem<File> rootNode = createNode(rootFile);
		fileTreeView.setRoot(rootNode);
		fileTreeView.setShowRoot(false);
	}
	private TreeItem<File> createNode(final File f) {
		return new TreeItem<File>(f) {
			private boolean isLeaf;
			private boolean isFirstTimeChildren = true;
			private boolean isFirstTimeLeaf = true;

			@Override
			public ObservableList<TreeItem<File>> getChildren() {
				if (isFirstTimeChildren) {
					isFirstTimeChildren = false;
					super.getChildren().setAll(buildChildren(this));
				}
				return super.getChildren();
			}

			@Override
			public boolean isLeaf() {
				if (isFirstTimeLeaf) {
					isFirstTimeLeaf = false;
					File f = (File) getValue();
					isLeaf = f.isFile();
				}
				return isLeaf;
			}

			private ObservableList<TreeItem<File>> buildChildren(
					TreeItem<File> TreeItem) {
				File f = TreeItem.getValue();
				if (f == null) {
					return FXCollections.emptyObservableList();
				}
				if (f.isFile()) {
					return FXCollections.emptyObservableList();
				}
				File[] files = f.listFiles();
				if (files != null) {
					ObservableList<TreeItem<File>> children = FXCollections
							.observableArrayList();
					for (File childFile : files) {
						children.add(createNode(childFile));
					}
					return children;
				}
				return FXCollections.emptyObservableList();
			}
		};
	}
}
