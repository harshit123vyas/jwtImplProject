package com.tasktracker.userservice.Controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tasktracker.userservice.Entity.User;
import com.tasktracker.userservice.Service.UserService;
import com.tasktracker.userservice.response.UserResponse;

@RestController
@RequestMapping("/manager")
public class ManagerController {
	
	
	static final Logger logger = LogManager.getLogger(UserControllerImpl.class.getName());

	@Autowired
	private UserService userService;
	@GetMapping("/getUsersById")
	public ResponseEntity<UserResponse<List<User>>> getUserById(@RequestParam Long id) {
		UserResponse<List<User>> userResponse = new UserResponse<List<User>>();
		try {
			userResponse = userService.getUserById(id);
			if (userResponse.isStatus()) {
				return new ResponseEntity<UserResponse<List<User>>>(userResponse, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<UserResponse<List<User>>>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			return new ResponseEntity<UserResponse<List<User>>>(HttpStatus.NOT_FOUND);
		}
	}

}
