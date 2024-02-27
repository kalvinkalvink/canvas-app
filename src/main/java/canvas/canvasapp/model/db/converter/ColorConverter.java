package canvas.canvasapp.model.db.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import javafx.scene.paint.Color;
import java.util.Objects;
@Converter
public class ColorConverter implements AttributeConverter<Color, String> {
	@Override
	public String convertToDatabaseColumn(Color color) {
		if (Objects.isNull(color)) return null;
		return String.format("%f,%f,%f", color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public Color convertToEntityAttribute(String rgbString) {
		if (Objects.isNull(rgbString)) return null;
		String[] rgbArray = rgbString.split(",");
		double red = Double.parseDouble(rgbArray[0]);
		double green = Double.parseDouble(rgbArray[1]);
		double blue = Double.parseDouble(rgbArray[2]);
		return new Color(red, green, blue, 1);
	}
}
