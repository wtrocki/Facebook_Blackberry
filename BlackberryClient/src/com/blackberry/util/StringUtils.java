/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
package com.blackberry.util;

import java.util.Vector;

public class StringUtils {

	public static String[] split(String str, char sep, int maxNum) {
		if ((str == null) || (str.length() == 0)) {
			return new String[0];
		}

		Vector results = maxNum == 0 ? new Vector() : new Vector(maxNum);

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			if (c == sep) {
				if (maxNum != 0) {
					if ((results.size() + 1) == maxNum) {
						for (; i < str.length(); i++) {
							buf.append(str.charAt(i));
						}
						break;
					}
				}

				results.addElement(buf.toString());
				buf.setLength(0);
			} else {
				buf.append(c);
			}
		}

		if (buf.length() > 0) {
			results.addElement(buf.toString());
		}

		return toStringArray(results);
	}

	public static String[] toStringArray(Vector strings) {
		String[] result = new String[strings.size()];
		for (int i = 0; i < strings.size(); i++) {
			result[i] = strings.elementAt(i).toString();
		}
		return result;
	}

	public static String chomp(String inStr) {
		if ((inStr == null) || "".equals(inStr)) {
			return inStr;
		}

		char lastChar = inStr.charAt(inStr.length() - 1);
		if (lastChar == 13) {
			return inStr.substring(0, inStr.length() - 1);
		} else if (lastChar == 10) {
			String tmp = inStr.substring(0, inStr.length() - 1);
			if ("".equals(tmp)) {
				return tmp;
			}
			lastChar = tmp.charAt(tmp.length() - 1);
			if (lastChar == 13) {
				return tmp.substring(0, tmp.length() - 1);
			} else {
				return tmp;
			}
		} else {
			return inStr;
		}
	}

	public static String parentOf(String inStr) {
		String result = null;

		if ((inStr != null) && !inStr.trim().equals("")) {
			inStr = inStr.trim();
			int index = inStr.lastIndexOf('.');
			if (index != -1) {
				result = inStr.substring(0, index);
			}
		}

		return result;
	}

	public static boolean isBlank(String string) {
		return (string == null) || "".equals(string.trim());
	}

	public static String fixHttpsUrlPrefix(String url) {
		String result = "";
		if ((url == null) || url.trim().equals("")) {
			result = url;
		} else {
			if (url.startsWith("http://") && (url.indexOf(":443") != -1)) {
				// fix it
				result = "https://" + url.substring(7);
			} else {
				// return the original url for all other cases
				result = url;
			}
		}
		return result;
	}

}