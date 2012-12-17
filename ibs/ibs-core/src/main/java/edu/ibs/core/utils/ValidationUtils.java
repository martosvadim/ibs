package edu.ibs.core.utils;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 6:25
 */
public class ValidationUtils {
	public static boolean isEmpty(final String str) {
		if (str != null && !(str.length() <= 0) && !str.isEmpty() && !("".equals(str.trim()))) {
			return false;
		}
		return true;
	}
}
