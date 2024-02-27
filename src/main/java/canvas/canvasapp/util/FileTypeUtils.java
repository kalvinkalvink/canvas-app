package canvas.canvasapp.util;

import canvas.canvasapp.type.doc.DocType;
import org.apache.commons.io.FilenameUtils;

public class FileTypeUtils {
	public static boolean isDoc(String path) {
		return isWord(path) || isPpt(path);
	}

	public static boolean isWord(String path) {
		String extesion = FilenameUtils.getExtension(path);
		return extesion.equals(DocType.DOCX.toString()) || extesion.equals(DocType.DOC.toString());
	}

	public static boolean isPpt(String path) {
		String extesion = FilenameUtils.getExtension(path);
		return extesion.equals(DocType.PPTX.toString()) || extesion.equals(DocType.PPT.toString());
	}
}
