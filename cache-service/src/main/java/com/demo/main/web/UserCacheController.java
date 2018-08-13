package com.demo.main.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.main.util.CacheUtils;

@RestController
@RequestMapping("/cache")
public class UserCacheController {

	@Autowired
	CacheUtils cacheUtils;

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@CacheEvict(value = "users", key = "#id", allEntries = false)
	@DeleteMapping("/users/{id}")
	public void deleteFromUserCacheById(@PathVariable Long id) {
		LOG.info("deleting User from cache with id {}", id);
	}

	@CacheEvict(value = "users", allEntries = true)
	@DeleteMapping("/users")
	public void deleteAllFromUserCache() {
		LOG.info("deleting All Users from cache with id {}");
	}

	@PutMapping("/users/{minutes}")
	public void updateCacheExpiry(@PathVariable("minutes") long minutes) {
		LOG.info("Updating Cache Expiry for : {}", minutes);
	}

	@PostMapping("/expiration")
	public void setCacheExpirationInSeconds() {
		LOG.info("Updating Expiration Time of App Caches...");
		cacheUtils.findAndSetExpirationTimeForCaches();
		LOG.info("Updation Successful.");
	}
}
