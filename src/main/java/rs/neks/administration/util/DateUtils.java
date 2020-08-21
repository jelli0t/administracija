/**
 * 
 */
package rs.neks.administration.util;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

/**
 * @author jelles
 *
 */
public class DateUtils {

	public static final DateTimeFormatter PICKER_DATE_FORMATTER = new DateTimeFormatterBuilder()
			.append(DateTimeFormatter.ISO_LOCAL_DATE)
		    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
		    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
		    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
		    .toFormatter();
	
	
	
	public static String formatDateTime(LocalDateTime date, DateTimeFormatter formatter) {
		if(date != null && formatter != null) {
			try {
				return date.format(formatter);
			} catch (DateTimeException dte) {
				// TODO: handle exception
			}
		}
		return null;
	}
	
	
	public static LocalDateTime parseDateTime(String date, DateTimeFormatter formatter) {
		if(TextUtils.notEmpty(date) && formatter != null) {
			try {
				return LocalDateTime.parse(date, formatter);
			} catch (DateTimeParseException e) {
				return null;
			}
		}
		return null;
	}
	
}