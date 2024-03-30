package canvas.canvasapp.util;

import canvas.canvasapp.type.file.EXTENSION;
import org.apache.commons.io.FilenameUtils;

public class FileTypeUtils {
	public static boolean isDoc(String path) {
		return isWord(path) || isPpt(path);
	}

	public static boolean isWord(String path) {
		String extesion = FilenameUtils.getExtension(path);
		return extesion.equals(EXTENSION.DOCX.toString()) || extesion.equals(EXTENSION.DOC.toString());
	}

	public static boolean isPpt(String path) {
		String extesion = FilenameUtils.getExtension(path);
		return extesion.equals(EXTENSION.PPTX.toString()) || extesion.equals(EXTENSION.PPT.toString());
	}
}
