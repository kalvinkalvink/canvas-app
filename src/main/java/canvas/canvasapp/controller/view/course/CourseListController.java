package canvas.canvasapp.controller.view.course;

import canvas.canvasapp.controller.view.MainController;
import canvas.canvasapp.model.Course;
import canvas.canvasapp.service.CourseService;
import canvas.canvasapp.service.view.course.CourseListService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@FxmlView("/view/component/tab/course-list.fxml")
public class CourseListController {
    @FXML
    VBox courseListVBox;
    @Autowired
    CourseService courseService;
    @Autowired
    CourseListService courseListService;
    @Autowired
    MainController mainController;

    @FXML
    private void initialize() {
        log.info("Initializing Course List Menu");
        List<Course> selectedCourseList = courseService.findAllSelected();
        Button[] courseButtonArray = selectedCourseList.stream()
                .map(course -> {
                    Button courseButton = new Button(course.getName());
                    courseButton.setMaxWidth(Double.MAX_VALUE);
                    courseButton.setOnAction((actionEvent) -> {
                        courseListService.publishCourseItemClickedEvent(this, course);
                        Stage window = (Stage) courseListVBox.getScene().getWindow();
                        window.close();
                    });
                    return courseButton;
                })
                .toArray(Button[]::new);
        System.out.println(courseButtonArray);
        courseListVBox.getChildren().addAll(courseButtonArray);
    }
}
