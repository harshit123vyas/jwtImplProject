package com.tasktracker.userservice.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tasktracker.userservice.Entity.AuthUser;
import com.tasktracker.userservice.ServiceImpl.AuthUserService;
import com.tasktracker.userservice.jwt.JwtHelper;
import com.tasktracker.userservice.jwtrequest.JwtRequest;
import com.tasktracker.userservice.jwtresponse.JwtResponse;
import com.tasktracker.userservice.securityconfig.CustomUserDetailService;

@RestController
public class JwtAuthenticationController {

	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private CustomUserDetailService userDetailsService;

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private JwtHelper helper;

	@PostMapping("/authenticate")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
		// authenticate
		this.doAuthenticate(request.getUsername(), request.getPassword());

		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		String token = this.helper.generateToken(userDetails);

		JwtResponse response = JwtResponse.builder().jwtToken(token).username1(userDetails.getUsername()).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void doAuthenticate(String username, String password) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
				password);
		try {
			manager.authenticate(authentication);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Credentials Invalid !!");
		}

	}

	@GetMapping("/register")
	private String register(@RequestBody AuthUser user) {
		AuthUser authuser = authUserService.saveUser(user);
		if (authuser != null) {
			return "register successfully";
		}

		return "user already exixt";
	}

}
