package canvas.canvasapp.util;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class DateFormatterUtil {
	private final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	private final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public String format(LocalDate date) {
		return format(date, DEFAULT_DATE_FORMAT);
	}

	public String format(LocalDateTime dateTime) {
		return format(dateTime, DEFAULT_DATE_TIME_FORMAT);
	}

	public String format(LocalDate date, String pattern) {
		if (date == null) {
			return "";
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return date.format(formatter);
	}

	public String format(LocalDateTime dateTime, String pattern) {
		if (dateTime == null) {
			return "";
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return dateTime.format(formatter);
	}

	public LocalDate parseDate(String dateString) {
		return parseDate(dateString, DEFAULT_DATE_FORMAT);
	}

	public LocalDateTime parseDateTime(String dateTimeString) {
		return parseDateTime(dateTimeString, DEFAULT_DATE_TIME_FORMAT);
	}

	public LocalDate parseDate(String dateString, String pattern) {
		if (dateString == null || dateString.isEmpty()) {
			return null;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDate.parse(dateString, formatter);
	}

	public LocalDateTime parseDateTime(String dateTimeString, String pattern) {
		if (dateTimeString == null || dateTimeString.isEmpty()) {
			return null;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDateTime.parse(dateTimeString, formatter);
	}
	public static LocalDate convertToLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
}