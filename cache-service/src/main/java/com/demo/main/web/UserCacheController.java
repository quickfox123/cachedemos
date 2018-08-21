package com.demo.main.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class UserCacheController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Caching(evict = { @CacheEvict(value = "users", key = "#id +'SALES'", allEntries = false),
			@CacheEvict(value = "users", key = "#id + 'OTHER'", allEntries = false),
			@CacheEvict(value = "users", key = "#id +'ALL'", allEntries = false) })
	@DeleteMapping("/users/{id}")
	public void deleteFromUserCacheByIdAndDivision(@PathVariable String id, @RequestParam(value = "mbu") String division) {
		LOG.info("deleting User from cache with key:: id: {} and division: {}", id, division);
	}

	@CacheEvict(value = "users", allEntries = true)
	@DeleteMapping("/users")
	public void deleteAllFromUserCache(String cacheName) {
		LOG.info("deleting All Users from cache  {}", cacheName);
	}

}
