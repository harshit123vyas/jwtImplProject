package com.tasktracker.userservice.Controller;

import java.sql.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tasktracker.userservice.Entity.User;
import com.tasktracker.userservice.Service.UserService;
import com.tasktracker.userservice.response.UserResponse;

@RestController
@RequestMapping("/admin")
public class AdminController {

	static final Logger logger = LogManager.getLogger(UserControllerImpl.class.getName());

	@Autowired
	private UserService userService;

	@Value("${project.image}")
	private String path;

	private static final String ERROR_MESSAGE = "Something went wrong";

	
	@GetMapping("/getUsers")
	public ResponseEntity<UserResponse<List<User>>> getAllUsers(
			@RequestParam(defaultValue = "5", required = false) Integer pageSize,
			@RequestParam(defaultValue = "0", required = false) Integer page) {
		UserResponse<List<User>> userResponse = new UserResponse<List<User>>();
		try {
			Pageable paging = PageRequest.of(page, pageSize);
			userResponse = userService.getAllUsers(paging);
			logger.info("userresponse : {}", userResponse);
			if (userResponse.isStatus()) {
				return new ResponseEntity<UserResponse<List<User>>>(userResponse, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<UserResponse<List<User>>>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			logger.error("exception : {}", e);
			return new ResponseEntity<UserResponse<List<User>>>(HttpStatus.NOT_FOUND);
		}
	}

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

	@GetMapping("/getUserByDate")
	public ResponseEntity<UserResponse<List<User>>> getUsersBetweenDates(@RequestParam Date startDate,
			@RequestParam Date endDate) {
		UserResponse<List<User>> userResponse = new UserResponse<List<User>>();
		try {
			userResponse = userService.getUsersBetweenDates(startDate, endDate);
			if (userResponse.isStatus()) {
				return new ResponseEntity<UserResponse<List<User>>>(userResponse, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<UserResponse<List<User>>>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			return new ResponseEntity<UserResponse<List<User>>>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/updateUser")
	public ResponseEntity<UserResponse<List<User>>> updateUser(@RequestParam Long id, @RequestBody User updateUser) {
		UserResponse<List<User>> userResponse = new UserResponse<List<User>>();
		try {
			userResponse = userService.updateUser(id, updateUser);
			if (userResponse.isStatus()) {
				return new ResponseEntity<UserResponse<List<User>>>(userResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<UserResponse<List<User>>>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			return new ResponseEntity<UserResponse<List<User>>>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/deleteUser")
	public ResponseEntity<String> deleteUser(@RequestParam Long id) {
		try {
			if (id != 0) {
				logger.info("Deleting User with id: {}", id);
				String existEmployee = userService.deleteUser(id);
				return ResponseEntity.status(HttpStatus.OK).body(existEmployee);
			} else {
				logger.warn("please enter the id");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return new ResponseEntity<String>("user not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/upload")
	public ResponseEntity<User> fileUpload(@RequestParam("image") MultipartFile image) {
		try {
			userService.uploadImage(path, image);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@GetMapping("/login")
	public ResponseEntity<String> sendEmail(@RequestParam("email") String email, @RequestParam("password") String password) {
		try {
			logger.info("usercontroller : sendEmail {}", email);
			userService.sendSimpleMessage(email, password);
			return new ResponseEntity<String>("mail send", HttpStatus.OK);
		} catch (Exception e) {
			logger.error("usercontroller : sendEmail {}", e);
		}
		return new ResponseEntity<String>("mail could not send", HttpStatus.NOT_FOUND);
	}

	@GetMapping("/verifyOtp")
	public ResponseEntity<String> verifyOtp(@RequestParam("email") String email, @RequestParam("otp") Long otp) {
		try {
			logger.info("usercontroller : verifyotp eamail : {} otp : {} ", email, otp);
			if (userService.checkOtp(email, otp)) {
				return new ResponseEntity<String>("otp matched ", HttpStatus.OK);
			}

		} catch (Exception e) {
			logger.info("usercontroller : verifyOyp {}", e);
		}

		return new ResponseEntity<String>("invalid otp ", HttpStatus.NOT_FOUND);
	}

}
