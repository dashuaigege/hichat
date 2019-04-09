package org.wongws.hichat;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.wongws.hichat.domain.RedisTemplate;
import org.wongws.hichat.util.RedisUtil;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;

/**
 * Redis缓存配置类
 * 
 * @author wongws
 *
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
	private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.password}")
	private String password;
	@Value("${spring.redis.port}")
	private int port;
	@Value("${spring.redis.timeout}")
	private int timeout;
	@Value("${spring.redis.jedis.pool.max-idle}")
	private int maxIdle;
	@Value("${spring.redis.jedis.pool.max-wait}")
	private long maxWaitMillis;

	/**
	 * 
	 * @return:org.springframework.data.redis.connection.jedis.JedisConnectionFactory
	 * @Description:Jedis配置
	 */
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);
		redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
		JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration
				.builder();
		jedisClientConfiguration.connectTimeout(Duration.ofMillis(timeout));
		JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration,
				jedisClientConfiguration.build());
		return factory;
	}

	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
		logger.info("RedisTemplate实例化成功！");
		RedisTemplate template = new RedisTemplate();
		template.setConnectionFactory(connectionFactory);
		initDomainRedisTemplate(template, connectionFactory);
		return template;
	}

	@Bean
	public KeyGenerator keyGenerator() {
		// 设置自动key的生成规则，配置spring boot的注解，进行方法级别的缓存
		// 使用：进行分割，可以很多显示出层级关系
		// 这里其实就是new了一个KeyGenerator对象，只是这是lambda表达式的写法，我感觉很好用，大家感兴趣可以去了解下
		return (target, method, params) -> {
			StringBuilder sb = new StringBuilder();
			sb.append(target.getClass().getName());
			sb.append(".");
			sb.append(method.getName());
			for (Object obj : params) {
				sb.append(":" + obj.toString());
			}
			String rsToUse = sb.toString();
			logger.info("自动生成Redis Key -> [{}]", rsToUse);
			return rsToUse;
		};
	}

	/**
	 * 设置数据存入 redis 的序列化方式,并开启事务
	 * 
	 * @param template
	 * @param connectionFactory
	 */
	@SuppressWarnings("unchecked")
	private void initDomainRedisTemplate(RedisTemplate template, RedisConnectionFactory connectionFactory) {
		// 如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to
		// String！
		FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<Object>(Object.class);
		// value值的序列化采用fastJsonRedisSerializer
		template.setValueSerializer(serializer);
		template.setHashValueSerializer(serializer);
		// key的序列化采用StringRedisSerializer
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		// 开启事务
		template.setEnableTransactionSupport(true);
		template.setConnectionFactory(connectionFactory);
	}

	/**
	 * 注入封装RedisTemplate
	 * 
	 * @param redisTemplate
	 * @return
	 */
	@Bean(name = "redisUtil")
	public RedisUtil redisUtil(RedisTemplate redisTemplate) {
		logger.info("RedisUtil注入成功！");
		RedisUtil redisUtil = new RedisUtil();
		redisUtil.setRedisTemplate(redisTemplate);
		return redisUtil;
	}

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory factory) {
		// 初始化缓存管理器，在这里我们可以缓存的整体过期时间什么的，我这里默认没有配置
		logger.info("初始化 -> [{}]", "CacheManager RedisCacheManager Start");
		RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(factory);
		return builder.build();
	}
}
