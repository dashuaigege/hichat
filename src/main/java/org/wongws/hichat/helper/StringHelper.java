package org.wongws.hichat.helper;

public class StringHelper {
	/**
	 * 从当前String对象移除指定的一组字符的所有尾部匹配项
	 * 
	 * @param oriainalStr
	 * @param trimChars
	 * @return
	 */
	public static String trimEnd(String oriainalStr, String trimChars) {
		if (!oriainalStr.substring(oriainalStr.length() - trimChars.length()).equals(trimChars))
			return oriainalStr;
		else
			return oriainalStr.substring(0, oriainalStr.length() - trimChars.length());
	}

	/**
	 * 报告指定字符串在此实例中的最后一个匹配项的从零开始的索引位置； 指定在搜索字符位置的数目的字符串开始时，开始指定字符和其后面的位置
	 * 
	 * @param str源字符串
	 * @param lastIndexCount要检查的字符位置数
	 * @param hitStr要搜寻的字符串
	 * @return
	 */
	public static String getLastSubString(String str, int lastIndexCount, String hitStr) {
		for (int i = 0; i < lastIndexCount; i++) {
			int index = str.lastIndexOf(hitStr);
			str = str.substring(0, index);
		}
		return str;
	}

	public static boolean isNullOrEmpty(String string) {
		return string == null || string.length() == 0;
	}
}
