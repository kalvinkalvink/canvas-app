package canvas.canvasapp.controller.view.course.file;

import canvas.canvasapp.CanvasApp;
import canvas.canvasapp.helpers.CourseFileHelper;
import canvas.canvasapp.model.db.Course;
import canvas.canvasapp.model.db.File;
import canvas.canvasapp.model.db.Folder;
import canvas.canvasapp.model.view.course.file.CourseTreeViewItem;
import canvas.canvasapp.service.application.CanvasPreferenceService;
import canvas.canvasapp.service.database.FileService;
import canvas.canvasapp.service.database.FolderService;
import canvas.canvasapp.service.view.course.CourseViewService;
import canvas.canvasapp.type.application.AppSetting;
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

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Controller
@FxmlView("/view/component/course/file.fxml")
public class CourseFilesController {
	@FXML
	TreeView<CourseTreeViewItem> filesTreeView;
	@Autowired
	CourseViewService courseViewService;
	@Autowired
	private CanvasPreferenceService canvasPreferenceService;
	@Autowired
	private FileService fileService;
	@Autowired
	private FolderService folderService;
	@Autowired
	private CourseFileHelper courseFileHelper;

	private Course selectedCourse;


	@FXML
	private void initialize() {
		this.selectedCourse = courseViewService.getCourse();
		String syncFolderBasePath = FilenameUtils.separatorsToSystem(canvasPreferenceService.get(AppSetting.COURSE_SYNC_FOLDER_PATH, ""));

		setupCellFactory();
	}


	private void setupCellFactory() {
		// setting treeview cell factory
		filesTreeView.setCellFactory(new Callback<TreeView<CourseTreeViewItem>, TreeCell<CourseTreeViewItem>>() {
			@Override
			public TreeCell<CourseTreeViewItem> call(TreeView<CourseTreeViewItem> courseTreeViewItemTreeView) {
				TreeCell<CourseTreeViewItem> fileTreeCell = new TreeCell<>() {
					@Override
					protected void updateItem(CourseTreeViewItem courseTreeViewItem, boolean empty) {
						super.updateItem(courseTreeViewItem, empty);
						setText((empty || courseTreeViewItem == null) ? "" : courseTreeViewItem.getName());
						if (Objects.nonNull(courseTreeViewItem) && Objects.nonNull(courseTreeViewItem.getCanvasFile())) {
							setOnMouseClicked(new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent mouseEvent) {
									if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
										File canvasFile = courseTreeViewItem.getCanvasFile();

										Path systemFilePath = courseFileHelper.courseFileToSystemFile(selectedCourse, canvasFile);
										java.io.File systemFile = systemFilePath.toFile();
										if (systemFile.exists() && systemFile.isFile()) {
											CanvasApp.hostServices.showDocument(systemFile.getPath());
										}
									}
								}
							});
						}

					}
				};
				return fileTreeCell;
			}
		});

		TreeItem<CourseTreeViewItem> treeRoot = createTreeRoot();
		filesTreeView.setRoot(treeRoot);
		filesTreeView.setShowRoot(false);
	}


	private void setTreeViewCellContedtmenu(TreeCell<CourseTreeViewItem> fileTreeCell) {
		ContextMenu fileTreeViewcontextMenu = new ContextMenu();

		// setup menu item
		MenuItem openFileMenuItem = new MenuItem("Open File");
		MenuItem openFolderMenuItem = new MenuItem("Open Folder");
		MenuItem convertToPdfMenuItem = new MenuItem("Convert to PDF");

		// setup menu item function
		openFileMenuItem.setOnAction(e -> {
			Optional<java.io.File> optionalTreeViewSelectedItemFile = getTreeViewSelectedItemFile();
			optionalTreeViewSelectedItemFile.ifPresent(systemFile -> {
				if (systemFile.exists() && systemFile.isFile()) {
					CanvasApp.hostServices.showDocument(systemFile.getPath());
				}
			});
		});
		openFolderMenuItem.setOnAction(e -> {
			Optional<java.io.File> optionalTreeViewSelectedItemFile = getTreeViewSelectedItemFile();
			optionalTreeViewSelectedItemFile.ifPresent(systemFile -> {
				// get file parent
				java.io.File folder = systemFile.toPath().getParent().toFile();
				if (folder.exists() && !folder.isFile()) {
					CanvasApp.hostServices.showDocument(systemFile.getPath());
				}
			});
		});
		convertToPdfMenuItem.setOnAction(e -> {

		});
		fileTreeViewcontextMenu.getItems().addAll(openFileMenuItem, openFolderMenuItem, convertToPdfMenuItem);
		fileTreeCell.setContextMenu(fileTreeViewcontextMenu);
	}

	private Optional<java.io.File> getTreeViewSelectedItemFile() {
		TreeItem<CourseTreeViewItem> selectedItem = filesTreeView.getSelectionModel().getSelectedItem();
		if (Objects.isNull(selectedItem)) return Optional.empty();
		File selectedCanvasFile = selectedItem.getValue().getCanvasFile();
		System.out.println(selectedCanvasFile.getFolder());
		Path systemFilePath = courseFileHelper.courseFileToSystemFile(selectedCourse, selectedCanvasFile);

		return Optional.of(systemFilePath.toFile());
	}

	private TreeItem<CourseTreeViewItem> createTreeRoot() {
		Optional<Folder> optionalRootFolder = folderService.findRootFolder(courseViewService.getCourse());
		TreeItem<CourseTreeViewItem> root = new TreeItem<>();
		optionalRootFolder.ifPresent(folder -> populateFolderChildren(root, folder));
		return root;
	}

	private void populateFolderChildren(TreeItem<CourseTreeViewItem> currentItem, Folder currentFolder) {
		ObservableList<TreeItem<CourseTreeViewItem>> children = currentItem.getChildren();
		if (currentFolder.getFoldersCount() > 0) {
			// get folders in current folder
			List<Folder> childFolderList = folderService.findAllChildFolder(currentFolder);
			childFolderList.forEach(folder -> {
				// create tree view item
				TreeItem<CourseTreeViewItem> courseTreeViewItemTreeItem = new TreeItem<>();
				courseTreeViewItemTreeItem.setValue(new CourseTreeViewItem()
						.setName(folder.getName())
						.setCanvasFile(null));
				// add tree item to node
				children.add(courseTreeViewItemTreeItem);
				// recursive populate the child
				populateFolderChildren(courseTreeViewItemTreeItem, folder);
			});
		}
		if (currentFolder.getFilesCount() > 0) {    // get files in current folder
			// get files in the current folder
			List<File> fileList = fileService.findAllByFolder(currentFolder);
			fileList.forEach(file -> {
				TreeItem<CourseTreeViewItem> courseTreeViewItemTreeItem = new TreeItem<>();

				courseTreeViewItemTreeItem.setValue(new CourseTreeViewItem()
						.setName(file.getFilename())
						.setCanvasFile(file));

				children.add(courseTreeViewItemTreeItem);
			});
		}
	}
}
