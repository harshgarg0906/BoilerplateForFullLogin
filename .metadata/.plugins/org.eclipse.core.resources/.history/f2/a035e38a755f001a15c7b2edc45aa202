package com.example.demo.service;

import java.util.Collections;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@Service
public class AuthenticationService {

	private static final String jwtSigningKey = "santander-induction";
//    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

	public static Authentication getAuthentication(HttpServletRequest request) {

		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("JWT-TOKEN")) {
					String user;
					try {
						user = Jwts.parser().setSigningKey(jwtSigningKey).parseClaimsJws(cookie.getValue()).getBody()
								.get("ps", String.class);

					} catch (ExpiredJwtException exception) {
						return null;
					}
					if (user != null) {
						return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
					}
				}
			}
		}
		return null;
	}

}
