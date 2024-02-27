package canvas.canvasapp.helpers;

import canvas.canvasapp.type.doc.DocType;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

@Service
public class DocTypeHelper {
	public boolean isOfficeDocument(String filename) {
		return isMsDoc(filename) || isMsPpt(filename);
	}

	public boolean isDoc(String filename) {
		String extension = FilenameUtils.getExtension(filename).toLowerCase();
		return extension.equals(DocType.DOC.toString());
	}

	public boolean isDocx(String filename) {
		String extension = FilenameUtils.getExtension(filename).toLowerCase();
		return extension.equals(DocType.DOCX.toString());
	}

	public boolean isPpt(String filename) {
		String extension = FilenameUtils.getExtension(filename).toLowerCase();
		return extension.equals(DocType.PPT.toString());
	}

	public boolean isPptx(String filename) {
		String extension = FilenameUtils.getExtension(filename).toLowerCase();
		return extension.equals(DocType.PPTX.toString());
	}

	public boolean isMsDoc(String fileName) {
		String extension = FilenameUtils.getExtension(fileName).toLowerCase();
		return isDoc(fileName) || isDocx(fileName);
	}

	public boolean isMsPpt(String filename) {
		String extension = FilenameUtils.getExtension(filename).toLowerCase();
		return isPpt(filename) || isPptx(filename);
	}
}
