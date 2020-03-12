package com.example.demo.util;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	static final long EXPIRATIONTIME = 3600000;// 1 hour in milliseconds
	static String SIGNINGKEY;
	static final String PREFIX = "Bearer";

	@Value("${SIGNING_KEY}")
	public void setSigningkey(String signingkey) {
		SIGNINGKEY = signingkey;
	}

	// Add token to Authorization header
	public static String addToken(HttpServletResponse res, User user) {
		Claims claims = Jwts.claims();
		claims.put("un", user.getUserName()); // username
		claims.put("n", user.getName()); // given name or name of user
		claims.put("avt", user.getAvatarURL()); // avatar url
		claims.put("role", user.getRole()); // user role in system
		String jwtToken = Jwts.builder().setClaims(claims)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, SIGNINGKEY).compact();
		return jwtToken;
	}
}
