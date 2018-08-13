package com.demo.main.config;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ConditionalOnProperty(value = "spring.cache.type", havingValue = "redis")
@EnableCaching
@RefreshScope
public class RedisCacheConfig /* implements CachingConfigurer */ {
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Bean
	@RefreshScope
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
		jedisConFactory.setHostName("127.0.0.1");
		jedisConFactory.setPort(6380);
		return jedisConFactory;
	}

	@Bean
	@RefreshScope
	public RedisTemplate<?, ?> redisTemplate() {
		RedisTemplate<?, ?> template = new RedisTemplate();
		template.setConnectionFactory(jedisConnectionFactory());
		return template;
	}

	@Autowired
	AppConfig appConfig;

	@Primary
	@Bean
	@RefreshScope
	public RedisCacheManager cacheManager() throws UnknownHostException {
		LOG.info("Entering...");

		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
		cacheManager.setUsePrefix(true);
		List<CacheConfig> cacheConfigList = appConfig.getCacheConfig();
		long defaultCacheExpiryTime = appConfig.getDefaultCacheExpiryTime();
		if (defaultCacheExpiryTime <= 0) {
			defaultCacheExpiryTime = 60 * 5;
		}
		LOG.info("Setting DefaultCacheExpiryTime : {}", defaultCacheExpiryTime);
		cacheManager.setDefaultExpiration(defaultCacheExpiryTime);
		if (null != cacheConfigList && cacheConfigList.size() > 0) {
			LOG.info("Setting Individual Cache Configuration");
			cacheConfigList.forEach(x -> LOG.info("CacheConfig:{}", x));
			cacheManager.setExpires(getCacheExpirySecondsMap(cacheConfigList));
		}
		LOG.info("Leaving.");
		return cacheManager;
	}

	public Map<String, Long> getCacheExpirySecondsMap(List<CacheConfig> cacheConfigList) {
		LOG.info("Entering...");
		Map<String, Long> cacheConfigMap = cacheConfigList.stream()
				.collect(Collectors.toMap(x -> x.getName(), x -> x.getExpirySeconds()));

		cacheConfigMap.forEach((k, v) -> LOG.info((k + ":" + v)));

		LOG.info("Leaving.");

		return cacheConfigMap;
	}

}
