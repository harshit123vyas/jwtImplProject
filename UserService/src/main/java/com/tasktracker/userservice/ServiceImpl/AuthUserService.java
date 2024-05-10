package com.tasktracker.userservice.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tasktracker.userservice.Entity.AuthUser;
import com.tasktracker.userservice.Entity.Role;
import com.tasktracker.userservice.Repository.AuthUserRepository;
import com.tasktracker.userservice.Repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthUserService {

	@Autowired
	private AuthUserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	public AuthUser saveUser(AuthUser user) {
		Optional<AuthUser> authuser = repository.findAuthUserByUserNameAndEmail(user.getUsername(), user.getEmail());
		log.info("autheruser : {}", authuser);
		if (!authuser.isPresent()) {
			AuthUser newAuthUser = new AuthUser();
			String encoded = new BCryptPasswordEncoder().encode(user.getPassword());
			newAuthUser.setPassword(encoded);
			newAuthUser.setEmail(user.getEmail());
			newAuthUser.setUserName(user.getUsername());
			List<Role> roleList = new ArrayList<Role>();
			List<String> checkRole = new ArrayList<String>();
			for (Role role : roleRepository.findAll()) {
				checkRole.add(role.getRole());
			}
			for (Role role : user.getRole()) {
				if (checkRole.contains(role.getRole().toUpperCase())) {
					roleList.add(role);
				} else {
					roleList.add(role);
					roleRepository.save(role);
				}
			}
			newAuthUser.setRole(roleList);
			log.info("roleList : {}", roleList);
			return repository.save(newAuthUser);
		}
		return authuser.get();
	}


}
