package com.example.demo.util;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.example.demo.model.User.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	static final long EXPIRATIONTIME = 3600000; // 1 hour in milliseconds
	private static final String jwtSigningKey = "santander-induction";

	public static String addToken(HttpServletResponse res, String psid, UserRole role) {

		Claims claims = Jwts.claims();
		claims.put("ps", psid); // username
		claims.put("roles", role); // user role in system
		String jwtToken = Jwts.builder().setClaims(claims)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, jwtSigningKey).compact();
		return jwtToken;
	}

}
