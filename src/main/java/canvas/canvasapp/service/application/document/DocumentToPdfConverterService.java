package canvas.canvasapp.service.application.document;

import canvas.canvasapp.helpers.DocTypeHelper;
import canvas.canvasapp.lib.document.DocToPDFConverter;
import canvas.canvasapp.lib.document.DocxToPDFConverter;
import canvas.canvasapp.lib.document.PptToPDFConverter;
import canvas.canvasapp.lib.document.PptxToPDFConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Slf4j
@Service
public class DocumentToPdfConverterService {
	@Autowired
	private DocTypeHelper docTypeHelper;

	@Async
	public void convertDocumentToPdf(String sourceFilePath) throws Exception {
		// inplace conversion
		convertDocumentToPdf(sourceFilePath, FilenameUtils.removeExtension(sourceFilePath) + ".pdf");
	}

	@Async
	public void convertDocumentToPdf(String sourceFilePath, String destFilePath) throws Exception {
		File sourceFile = new File(sourceFilePath);
		File destFile = new File(destFilePath);
		// skip if desk pdf exist
		if (!destFile.isDirectory() && destFile.exists()) {
			log.debug("Skipping pdf conversion because '{}' already exist", destFilePath);
			return;
		}
		// skip if not document file
		if (!isExtensionSupprted(sourceFilePath)) {
			log.error("Document to pdf convert not supported for {}", sourceFilePath);
			return;
		}
		// create file input stream and output stream
		FileInputStream fileInputStream = new FileInputStream(sourceFile);
		FileOutputStream fileOutputStream = new FileOutputStream(destFile);

		// perform conversion base on file type
		if (docTypeHelper.isDoc(sourceFilePath)) {
			new DocToPDFConverter(fileInputStream, fileOutputStream, true, true).convert();
		} else if (docTypeHelper.isDocx(sourceFilePath)) {
			new DocxToPDFConverter(fileInputStream, fileOutputStream, true, true).convert();
		} else if (docTypeHelper.isPpt(sourceFilePath)) {
			new PptToPDFConverter(fileInputStream, fileOutputStream, true, true).convert();
		} else if (docTypeHelper.isPptx(sourceFilePath)) {
			new PptxToPDFConverter(fileInputStream, fileOutputStream, true, true).convert();
		}
	}

	public boolean isExtensionSupprted(String fileName) {
		return docTypeHelper.isOfficeDocument(fileName);
	}
}
