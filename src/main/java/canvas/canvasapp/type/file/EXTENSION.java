package canvas.canvasapp.type.file;

public enum EXTENSION {
	PPTX, PPT, DOCX, DOC,
	PDF;


	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}
