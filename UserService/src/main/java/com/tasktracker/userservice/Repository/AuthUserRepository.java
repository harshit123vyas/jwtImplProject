package com.tasktracker.userservice.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasktracker.userservice.Entity.AuthUser;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {
	
	Optional<AuthUser> findByUserName(String userName);

	Optional<AuthUser> findByEmail(String email);

	Optional<AuthUser> findAuthUserByUserNameAndEmail(String userName, String email);

}
