package canvas.canvasapp.service.helper;

import com.spire.presentation.FileFormat;
import com.spire.presentation.Presentation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DocumentFormatConverterService {
	enum DocType{
		PPTX,DOCX
	}

	public void documentToPpt(String docPath, String destPath){
		Path fileName = Paths.get(docPath).getFileName();
		String baseName = FilenameUtils.getBaseName(fileName.toString());
		String extension = FilenameUtils.getExtension(fileName.toString());
		if(extension.equals(DocType.DOCX.toString())){
			docxToPdf(docPath, destPath);
		}else if(extension.equals(DocType.PPTX.toString())){
			pptxToPdf(docPath, destPath);
		}else {
			log.warn("File type not ");
		}
	}
	@Async
	public void docxToPdf(String docPath, String destPath) {




	}

	@Async
	public void pptxToPdf(String docPath, String destPath) {
		try {
			log.debug("Converting {} to pdf", docPath);
			Path saveFilePath = Paths.get(destPath);
			Path saveDirectory = saveFilePath.getParent();
			Path fileName = saveFilePath.getFileName();

			Presentation ppt = new Presentation();

			ppt.loadFromFile(docPath);
			int slideCount = ppt.getSlides().size();
			// spilt and save the ppt into multiple pdf because the library cannot convert more than 10 pages at once
			List<File> tempPdfFileList = new ArrayList<>();
			Path tempPdfDirectory = Files.createTempDirectory("canvas-app-temp-pdf");    // crete temp directory to save temp pdf
			for (int pageIndex = 0, fileSubfix = 0; pageIndex < slideCount; pageIndex += 10, fileSubfix++) {
				int endPageNum = pageIndex + 9;
				if (endPageNum > slideCount)
					endPageNum = slideCount - 1;
				File tempFile = File.createTempFile(FilenameUtils.removeExtension(fileName.getFileName().toString()), ".pdf", tempPdfDirectory.toFile());
				tempPdfFileList.add(tempFile);    // add to list
				ppt.saveToFile(pageIndex, endPageNum, tempFile.getPath(), FileFormat.PDF);
			}
			mergePdf(tempPdfFileList, destPath);
			FileSystemUtils.deleteRecursively(tempPdfDirectory.toFile());		// delete the temp directory
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void removeWaterMark(String fileName) {
		PDDocument pdDocument = new PDDocument();

	}

	private void mergePdf(List<File> pdfFileList, String destFileName) throws IOException {
		PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
		pdfMergerUtility.setDestinationFileName(destFileName);
		pdfFileList.stream().forEach(file -> {
			try {
				pdfMergerUtility.addSource(file);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		});
		File destFile = Paths.get(destFileName).toFile();
		if (destFile.exists()) {
			destFile.delete();
		}
		pdfMergerUtility.mergeDocuments(null);

	}
}