package canvas.canvasapp.model.application.guide;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Guide {
	private String name;
	private String guideFolderName;
}
