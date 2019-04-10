package org.wongws.hichat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 获取Spring信息的工具类
 * 
 * @author wongws
 *
 */
public class SpringUtil implements ApplicationContextAware {
	private final static Logger logger = LoggerFactory.getLogger(SpringUtil.class);
	private static ApplicationContext applicationContext = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringUtil.applicationContext == null) {
			SpringUtil.applicationContext = applicationContext;
			logger.debug("=====ApplicationContext配置成功,applicationContext=" + applicationContext + "=====");
		}
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	// 通过name获取bean
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	// 通过class获取bean
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	// 通过name，class获取bean
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}

}
