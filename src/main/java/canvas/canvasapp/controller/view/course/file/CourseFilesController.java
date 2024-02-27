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

import java.nio.file.Path;
import java.util.List;
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

	@FXML
	private void initialize() {
		String syncFolderBasePath = FilenameUtils.separatorsToSystem(canvasPreferenceService.get(AppSetting.COURSE_SYNC_FOLDER_PATH, ""));
		Course selectedCourse = courseViewService.getCourse();

		// setting treeview cell factory
		filesTreeView.setCellFactory(new Callback<TreeView<CourseTreeViewItem>, TreeCell<CourseTreeViewItem>>() {
			@Override
			public TreeCell<CourseTreeViewItem> call(TreeView<CourseTreeViewItem> courseTreeViewItemTreeView) {
				TreeCell<CourseTreeViewItem> fileTreeCell = new TreeCell<>(){
					@Override
					protected void updateItem(CourseTreeViewItem courseTreeViewItem, boolean empty) {
						super.updateItem(courseTreeViewItem, empty);
						setText((empty|| courseTreeViewItem==null)?"":courseTreeViewItem.getName());
					}
				};
				fileTreeCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						if(mouseEvent.getClickCount()==2){
							File canvasFile = fileTreeCell.getTreeItem().getValue().getCanvasFile();

							Path systemFilePath = courseFileHelper.courseFileToSystemFile(selectedCourse, canvasFile);
							java.io.File systemFile = systemFilePath.toFile();
							if(systemFile.exists() && systemFile.isFile()){
								CanvasApp.hostServices.showDocument(systemFile.getPath());
							}
						}
					}
				});
				return fileTreeCell;
			}
		});

		TreeItem<CourseTreeViewItem> treeRoot = createTreeRoot();
		filesTreeView.setRoot(treeRoot);
		filesTreeView.setShowRoot(false);
	}

	private TreeItem<CourseTreeViewItem> createTreeRoot() {
//		List<Pair<File, List<String>>> sortedFileList = new ArrayList<>();
//		for (File file : fileList){
//			String folderPath = file.getFolder().getFullName();
//			List<String> pathComponent = Arrays.asList(folderPath.split(Pattern.quote(java.io.File.separator)));
//			sortedFileList.add(new Pair<File, List<String>>(file, pathComponent));
//		}
//
//		sortedFileList.sort(Comparator.comparing(fileListPair -> Integer.valueOf(fileListPair.getValue().size())));
//		for (Pair<File, List<String>> filePair:
//			 sortedFileList) {
//			System.out.println(filePair.getKey().getFolder().getFullName());
//		}

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
