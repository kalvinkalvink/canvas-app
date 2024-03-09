package canvas.canvasapp.model.application;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.http.HttpEntity;

@Data
@Accessors(chain = true)
public class HttpResponse {
	private int statusCode;
	private HttpEntity httpEntity;
}
