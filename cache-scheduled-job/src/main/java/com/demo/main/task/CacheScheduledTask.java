package com.demo.main.task;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import com.demo.caching.config.AppConfig;
import com.demo.caching.config.CacheConfig;

@Configuration
@ConditionalOnProperty(value="spring.cache.type", havingValue="redis")
@RefreshScope
@EnableScheduling
public class CacheScheduledTask  {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private AppConfig appConfig;

	@Autowired
    private ThreadPoolTaskScheduler taskScheduler;

	
	
	@PostConstruct
    public void scheduleRunnableWithCronTrigger() {
		LOG.info("***Scheduling CronTrigger....");
       
        Map<String, String> cacheExpiryTimeMap = getCacheExpiryTimeMap(appConfig);
		if (null != cacheExpiryTimeMap && cacheExpiryTimeMap.size() > 0) {
			cacheExpiryTimeMap.forEach((k, v) -> {
				LOG.debug("Added Cron Task for cache: {} with CRON: {}", k, v);
				CronTrigger cronTrigger = new CronTrigger(v);
				LOG.debug("***Adding CacheClearTask for Cache:{} with CronExpression:{}",k,cronTrigger.getExpression());
				taskScheduler.schedule(new CacheClearTask(cacheManager, k),cronTrigger);
			});
		}

    }
	
	private Map<String, String> getCacheExpiryTimeMap(AppConfig appConfig) {
		LOG.debug("Entering...");
		Map<String, String> cacheExpiryTimeMap = new HashMap<>();
		if (null != appConfig) {
			List<CacheConfig> cacheConfigList = appConfig.getCacheConfig();
			if (null != cacheConfigList && cacheConfigList.size() > 0) {
				cacheExpiryTimeMap = cacheConfigList.stream().filter(
						cacheConfig -> cacheConfig.getExpiryTime() != null && cacheConfig.getExpiryTime().length() > 0)
						.collect(Collectors.toMap(x -> x.getName(), x -> x.getExpiryTime()));
			}
		}
		LOG.debug("Leaving.");
		return cacheExpiryTimeMap;
	}
	
	@Bean(destroyMethod="shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(100);
    }
}
