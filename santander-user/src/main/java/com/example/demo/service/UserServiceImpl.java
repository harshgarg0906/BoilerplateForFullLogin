package com.example.demo.service;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.example.demo.exception.DatabaseEmptyException;
import com.example.demo.model.User;
import com.example.demo.repository.IUserRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
@Service
public class UserServiceImpl {

	@Autowired
	private IUserRepository userRepo;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	static String SIGNINGKEY;

	@Value("${SIGNING_KEY}")
	public void setSigningkey(String signingkey) {
		SIGNINGKEY = signingkey;
	}

	// For posting the user data into the user database
	public void addUserData(User user) throws DuplicateKeyException {
		if (userRepo.findByEmail(user.getEmail()) == null) {
			userRepo.save(user);
		} else {
			throw new DuplicateKeyException("User Already exists with Email " + user.getEmail());
		}
	}

	// For getting the user profile using username from the database
	public User loadByUsername(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("JWT-TOKEN")) {
					String user;
					try {
						user = Jwts.parser().setSigningKey(SIGNINGKEY).parseClaimsJws(cookie.getValue()).getBody()
								.get("un", String.class);
					} catch (ExpiredJwtException exception) {
						logger.error("In loadByUsername method" + LocalDateTime.now() + " " + exception.getMessage());
						return null;
					}
					if (user != null) {
						return this.userRepo.findByUserName(user);
					}
				}
			}
		}
		return null;
	}

	// For getting user by user email id
	public User findByUserEmail(String email) {
		return userRepo.findByEmail(email);
	}

	// For getting all the client profile data from the database
	public List<User> findAllUsers() throws DatabaseEmptyException {
		if (userRepo.findAll().size() == 0) {
			throw new DatabaseEmptyException("Database is Empty");
		} else {
			return userRepo.findAll();
		}
	}

	public User patchUserData(String email) {
		User user = userRepo.findByEmail(email);
		user.setRead(true);
		return userRepo.save(user);
	}

}
