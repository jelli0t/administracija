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
	
	public static final String EMPTY_STRING = "";
	public static final char SINGLE_SPACE = 0x20;

	public static boolean isEmpty(@Nullable Object str) {
		return (str == null || "".equals(str));
	}
}
