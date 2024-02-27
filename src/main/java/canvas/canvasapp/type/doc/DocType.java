package canvas.canvasapp.type.doc;

public enum DocType {
	PPTX, PPT, DOCX, DOC;


	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}
