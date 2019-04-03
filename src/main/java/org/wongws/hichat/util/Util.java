package org.wongws.hichat.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.wongws.hichat.domain.SimpleUser;
import org.wongws.hichat.entity.HcUser;
import org.wongws.hichat.helper.StringHelper;

public class Util {
	public static Map<String, SimpleUser> User_OnOff_Dic = new ConcurrentHashMap<String, SimpleUser>();

	public static String USERIMGURL = "static/dist/images";

	/**
	 * 获取访问者IP   在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。  
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)， 如果还不存在则调用Request
	 * .getRemoteAddr()。  
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) throws Exception {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String getCurrentProjectPath() {
		String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
		if (System.getProperty("os.name").toLowerCase().startsWith("win"))
			path = path.substring(path.indexOf("/") + 1);
		return path;
	}

}
