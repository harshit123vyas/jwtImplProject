package com.tasktracker.userservice.Controller;

import java.sql.Date;
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
@RequestMapping("/hr")
public class HrController {


	static final Logger logger = LogManager.getLogger(UserControllerImpl.class.getName());

	@Autowired
	private UserService userService;
	
	@GetMapping("/getUserByDate")
	public ResponseEntity<UserResponse<List<User>>> getUsersBetweenDates(@RequestParam Date startDate,
			@RequestParam Date endDate) {
		UserResponse<List<User>> userResponse = new UserResponse<List<User>>();
		try {
			userResponse = userService.getUsersBetweenDates(startDate, endDate);
			if (userResponse.isStatus()) {
				return new ResponseEntity<UserResponse<List<User>>>(userResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<UserResponse<List<User>>>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			return new ResponseEntity<UserResponse<List<User>>>(HttpStatus.NOT_FOUND);
		}
	}

}
