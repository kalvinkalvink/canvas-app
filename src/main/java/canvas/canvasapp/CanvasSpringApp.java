package canvas.canvasapp;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class CanvasSpringApp {
	public static void main(String[]args){
		Application.launch(CanvasApp.class, args);
	}
}
