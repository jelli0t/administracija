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

	public static boolean isEmpty(@Nullable Object str) {
		return (str == null || "".equals(str));
	}
}
