package canvas.canvasapp.controller.view;

import canvas.canvasapp.CanvasApp;
import canvas.canvasapp.helpers.DocTypeHelper;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import canvas.canvasapp.service.application.document.DocumentToPdfConverterService;
import canvas.canvasapp.type.application.AppSetting;
import com.dlsc.preferencesfx.view.FilterableTreeItem;
import com.sshtools.twoslices.Toast;
import com.sshtools.twoslices.ToastType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.Objects;

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
	@Autowired
	private DocumentToPdfConverterService documentToPdfConverterService;
	@Autowired
	private DocTypeHelper docTypeHelper;

	private FilterableTreeItem<File> fileTreeRoot;

	// context menu
	// folder
	private final ContextMenu folderContextMenu = new ContextMenu();
	private final ContextMenu fileContextMenu = new ContextMenu();
	private final ContextMenu officeDocumentFileContextMenu = new ContextMenu();

	@FXML
	private void initialize() {
		setupContextMenu();
		setupCellFactory();
	}

	private void setupContextMenu() {
		setupFolderContextmenu();
		setupFileContextMenu();
		setupOfficeDocumentContextMenu();
	}

	private void setupFolderContextmenu() {
		// reset all context menu
		folderContextMenu.getItems().clear();

		MenuItem openFolderMenuItem = new MenuItem("open folder");
		// set context menu function //
		openFolderMenuItem.setOnAction(event -> {
			File file = fileTreeView.getSelectionModel().getSelectedItem().getValue();
			CanvasApp.hostServices.showDocument(file.getPath());
		});
		folderContextMenu.getItems().addAll(openFolderMenuItem);
	}

	private void setupFileContextMenu() {
		// reset all context menu
		fileContextMenu.getItems().clear();
		MenuItem deleteFileMenuItem = new MenuItem("delete");
		deleteFileMenuItem.setOnAction(event -> {
			MultipleSelectionModel<TreeItem<File>> fileTreeViewSelectionModel = fileTreeView.getSelectionModel();
			File file = fileTreeViewSelectionModel.getSelectedItem().getValue();
			log.trace("Deleting file {}", file.getName());
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + file.getName() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {    // delete file
				boolean fileDeleted = file.delete();
				if (fileDeleted) {
					TreeItem<File> selectedItem = fileTreeViewSelectionModel.getSelectedItem();
					selectedItem.getParent().getChildren().remove(selectedItem);
					log.trace("File {} deleted", file.getName());
				} else {
					log.trace("Failed to delete file {}", file.getName());
				}
			}
		});
		fileContextMenu.getItems().addAll(deleteFileMenuItem);
	}

	private void setupOfficeDocumentContextMenu() {
		// reset all context menu
		officeDocumentFileContextMenu.getItems().clear();

		MenuItem deleteFileMenuItem = new MenuItem("delete");
		MenuItem convertToPdfMenuItem = new MenuItem("convert to pdf");

		deleteFileMenuItem.setOnAction(event -> {
			MultipleSelectionModel<TreeItem<File>> fileTreeViewSelectionModel = fileTreeView.getSelectionModel();
			File file = fileTreeViewSelectionModel.getSelectedItem().getValue();
			log.trace("Deleting file {}", file.getName());
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + file.getName() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {    // delete file
				boolean fileDeleted = file.delete();
				if (fileDeleted) {
					TreeItem<File> selectedItem = fileTreeViewSelectionModel.getSelectedItem();
					selectedItem.getParent().getChildren().remove(selectedItem);
					log.trace("File {} deleted", file.getName());
				} else {
					log.trace("Failed to delete file {}", file.getName());
				}
			}
		});
		convertToPdfMenuItem.setOnAction(event -> {
			File file = fileTreeView.getSelectionModel().getSelectedItem().getValue();
			log.trace("Converting {} to pdf", file.getName());
			// check if file extension is supported for conversion
			if (!documentToPdfConverterService.isExtensionSupprted(file.getName())) {
				log.error("File extension {} conversion not supported", file.getName());
				return;
			}
			Toast.toast(ToastType.INFO, "PDF Converter", String.format("Converting %s to PDF", file.getName()));
			try {
				// convert to pdf in place
				documentToPdfConverterService.convertDocumentToPdf(file.getPath());
				setupCellFactory();
			} catch (Exception e) {
				log.error(String.format("Failed to convert %s to pdf", file.getPath()), e);
			}

		});
		officeDocumentFileContextMenu.getItems().addAll(convertToPdfMenuItem, deleteFileMenuItem);
	}


	private void setupCellFactory() {
		fileTreeView.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
			@Override
			public TreeCell<File> call(TreeView<File> fileTreeView) {
				TreeCell<File> fileTreeCell = new TreeCell<>() {
					@Override
					protected void updateItem(File file, boolean empty) {
						super.updateItem(file, empty);
						setText((empty || file == null) ? "" : file.getName());
						if (Objects.nonNull(file) && file.isFile()) {        // leaf

							if (docTypeHelper.isOfficeDocument(file.getName())) {    // document file
								setContextMenu(officeDocumentFileContextMenu);
							} else {
								setContextMenu(fileContextMenu);
							}
							setOnMouseClicked(new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent mouseEvent) {
									if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
										File file = getTreeItem().getValue();
										if (file.exists() && file.isFile()) {
											CanvasApp.hostServices.showDocument(file.getPath());
										}
									}
								}
							});
						} else {        // folder node
							setContextMenu(folderContextMenu);
						}
					}
				};
				// setting on click action
				return fileTreeCell;
			}
		});
		String syncFolderBasePath = FilenameUtils.separatorsToSystem(canvasPreferenceService.get(AppSetting.COURSE_SYNC_FOLDER_PATH, ""));
		if (syncFolderBasePath.isEmpty()) {    // don't show root directory when no sync path is selected
			return;
		}
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
