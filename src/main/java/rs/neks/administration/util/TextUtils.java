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
	
	public static final char EMPTY = 0x00;
	public static final String EMPTY_STRING = String.valueOf(EMPTY);
	public static final char SINGLE_SPACE = 0x20;

	public static boolean isEmpty(@Nullable Object str) {
		return (str == null || "".equals(str));
	}
}
