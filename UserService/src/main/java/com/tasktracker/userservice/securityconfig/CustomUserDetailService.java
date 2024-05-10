package com.tasktracker.userservice.securityconfig;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tasktracker.userservice.Entity.AuthUser;
import com.tasktracker.userservice.Repository.AuthUserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private AuthUserRepository repository;
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
	//	Optional<AuthUser> optional =  repository.findByEmail(email);
		Optional<AuthUser> optional =  repository.findByUserName(userName);
		System.out.println(optional);
		return optional.get();
	}

}
