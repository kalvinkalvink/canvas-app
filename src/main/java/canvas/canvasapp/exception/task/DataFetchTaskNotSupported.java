package canvas.canvasapp.exception.task;

import canvas.canvasapp.type.database.TableFetchType;
import lombok.Getter;

@Getter
public class DataFetchTaskNotSupported extends Exception {
	private final TableFetchType tableFetchType;

	public DataFetchTaskNotSupported(String message, TableFetchType tableFetchType) {
		super(message);
		this.tableFetchType = tableFetchType;
	}

}
