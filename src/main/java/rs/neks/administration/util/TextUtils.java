/**
 * 
 */
package rs.neks.administration.util;

import org.springframework.lang.Nullable;

/**
 * @author nemanja
 *
 */
public abstract class TextUtils {
	
	public static final String EMPTY = "";
	public static final char SINGLE_SPACE = 0x20;
	public static final char SLASH = 0x2f;
		
	public static final String DECIMAL_NUM_COMMA_FORMAT = "^(\\d*\\,)?\\d+$";
	public static final String DECIMAL_NUM_DOT_FORMAT = "^(\\d*\\.)?\\d+$";
	
	public static final String STRONG_PASSWORD_STRENGTH_REGEX = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#!@%$*?&_]).{8,32}";

	public static boolean isEmpty(@Nullable Object str) {
		return (str == null || EMPTY.equals(str));
	}
	
	public static boolean notEmpty(@Nullable Object str) {
		return (str != null && !EMPTY.equals(str));
	}
	
	/**
	 * 
	 * */
	public static char toChar(int code) {
		return (char) code;
	}
	
}
