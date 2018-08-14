package com.demo.main.util;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.demo.main.config.AppConfig;
import com.demo.main.config.CacheConfig;

@Component
public class CacheUtils {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Value("${refresh.endpoint}")
	String refreshEndpoint;

	@Autowired
	RedisTemplate<String, ?> redisTemplate;

	@Autowired
	AppConfig appConfig;

	public void findAndSetExpirationTimeForCaches() {
		LOG.info("Entering..");

		LOG.info("Updating Spring RedisCacheManager to have the  values updated in Configuration for new Keys...");
		// Update RedisCacheManager (using /refresh Endpoint) so that New Keys Created
		// for the passed "cacheName" will have the Expiration Time Set to "seconds"
		invokeRefreshOnService();

		LOG.info("Updated Spring RedisCacheManager to have the  values updated in Configuration for new Keys");

		List<CacheConfig> cacheConfigList = appConfig.getCacheConfig();
		if (null != cacheConfigList && cacheConfigList.size() > 0) {
			LOG.info("CacheConfigList is:");
			Consumer<CacheConfig> action = new Consumer<CacheConfig>() {

				@Override
				public void accept(CacheConfig cacheConfig) {
					if (null != cacheConfig) {
						LOG.info("Next:{}", cacheConfig.toString());
						// Update Existing Keys for Updated Caches in Configuration
						updateExpiryExistingKeys(cacheConfig.getName(), cacheConfig.getExpirySeconds());
					}
				}
			};
			cacheConfigList.forEach(action);
		}
		LOG.info("Leaving.");
	}

	public void invokeRefreshOnService() {
		LOG.info("Entering...");
		LOG.info("Configured refreshEndpoint is: {}", refreshEndpoint);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<>(new String("test"));
		String response = restTemplate.postForObject(refreshEndpoint, request, String.class);
		LOG.info("Received Response:." + response);
		LOG.info("Leaving.");
	}

	public void updateExpiryExistingKeys(String cacheName, long seconds) {
		LOG.info("Entering...");
		LOG.info("Received cacheName:{} , seconds:{}", cacheName, seconds);

		LOG.info("Fetching all keys of cache:{}", cacheName);
		// Fetch All Keys with pattern cachename*
		RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
		String cacheNamePattern = cacheName + "*";
		Set<byte[]> keySet = redisConnection.keys(cacheNamePattern.getBytes());
		redisConnection.close();

		LOG.info("Updating Expiration Time of All Existing Keys of  cache:{}...", cacheName);
		// Update Expiration Time for Keys returned based on pattern cachename*
		redisTemplate.executePipelined(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				Consumer<byte[]> action = new Consumer<byte[]>() {
					@Override
					public void accept(byte[] t) {
						connection.expire(t, seconds);
					}
				};
				keySet.forEach(action);
				return null;
			}
		});
		LOG.info("Updated Expiration Time for Existing keys of cache:{}, To : new Time: {}", cacheName, seconds);
		LOG.info("Leaving.");
	}
}
