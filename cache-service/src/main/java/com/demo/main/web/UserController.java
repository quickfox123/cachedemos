package com.demo.main.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import com.demo.main.domain.User;
import com.demo.main.service.UserService;

@RestController
@RequestMapping
@RequestScope
public class UserController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	private final UserService userService;
	
/*
	@Autowired
	User user;*/
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/members/{id}")
	public User getMemberById(@PathVariable long id) {
		LOG.info("Getting Member with ID {}.", id);
		return userService.getMemberById(id);
	}

	@GetMapping("/users/{id}")
	public User getUserById(@PathVariable long id) {
		LOG.info("Getting User with ID {}.", id);
		return userService.getUserById(id);
	}
	
	@GetMapping("/plans/{id}")
	public User getPlanById(@PathVariable long id) {
		LOG.info("Getting Plan with ID {}.", id);
		return userService.getPlanById(id);
	}
	
	/*@GetMapping("/name")
	public String getMyName() {
		LOG.info("Getting Name");
		return user.getName();
	}*/

}
