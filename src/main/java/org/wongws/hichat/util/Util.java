package org.wongws.hichat.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Util {
	public static Map<String, Integer> UserDic = new ConcurrentHashMap<String, Integer>();
	static {
		UserDic.put("wws", 1);
		UserDic.put("lmm", 2);
		UserDic.put("zf", 3);
		UserDic.put("wvv", 4);
		UserDic.put("tzc", 5);
		UserDic.put("yz", 6);
	}
	public static String IMGURL="dist/images/";
}
