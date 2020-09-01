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

	public static boolean isEmpty(@Nullable Object str) {
		return (str == null || EMPTY.equals(str));
	}
	
	public static boolean notEmpty(@Nullable Object str) {
		return (str != null && !EMPTY.equals(str));
	}
}
