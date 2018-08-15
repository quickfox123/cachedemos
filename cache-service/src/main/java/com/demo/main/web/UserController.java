package com.demo.main.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import com.demo.main.domain.User;
import com.demo.main.service.UserService;

@RestController
@RequestMapping("/users")
@RequestScope
public class UserController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{id}")
	public User getUserById(@PathVariable long id) {
		LOG.info("Getting User with ID {}.", id);
		return userService.getUserById(id);
	}

	@PostMapping
	public User createUser(@RequestBody User user) {
		LOG.info("Creating User  {}.", user);
		return userService.createUser(user);
	}
	
	@DeleteMapping("/id")
	public void deleteUserById(@PathVariable long id) {
		LOG.info("Deleting User with id {}.", id);
		userService.deleteUserById(id);
	}

}
