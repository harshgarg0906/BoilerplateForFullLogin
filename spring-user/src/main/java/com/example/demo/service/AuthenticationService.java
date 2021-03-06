package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.model.User;
import com.example.demo.repository.DataRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;


@Service
public class AuthenticationService  {

	 @Value("${google.base.url}")
	    private String baseUrl;

	    @Value("${spring.security.oauth2.client.registration.google.client-id}")
	    String clientId;

	    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
	    String clientSecret;

	    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
	    String redirectUrl;

	    @Autowired
	    DataRepository userRepository;

	    @Autowired
	    RestTemplate restTemplate;

	    public String getAccessToken(String code) {
	        String url = "https://www.googleapis.com/oauth2/v3/token";
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Accept", "application/json");
	        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
	                .queryParam("client_id", this.clientId)
	                .queryParam("client_secret", this.clientSecret)
	                .queryParam("code", code)
	                .queryParam("grant_type", "authorization_code")
	                .queryParam("redirect_uri", this.redirectUrl);
	        HttpEntity<String> request = new HttpEntity<>(headers);
	        ResponseEntity<JSONObject> result = this.restTemplate.postForEntity(builder.toUriString(), request, JSONObject.class);
	        System.out.println("Access Token: " + result.getBody().get("access_token"));
	        return (String)result.getBody().get("access_token");

	    }

	    public User getUserProfile(String accessToken) {
	        String url = "https://www.googleapis.com/userinfo/v2/me";
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Accept", "application/json");
	        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
	                .queryParam("access_token", accessToken);
	        HttpEntity<String> request = new HttpEntity<>(headers);
	        ResponseEntity<JSONObject> result = this.restTemplate.getForEntity(builder.toUriString(), JSONObject.class);
	        System.out.println("User Profile: " + result);
	        JSONObject userDetails = result.getBody();
	        User user = new User();
	        user.setAvatar((String)userDetails.get("picture"));
	        user.setEmail((String)userDetails.get("email"));
	        user.setUsername((String)userDetails.get("email"));
	        user.setFirstName((String)userDetails.get("given_name"));
	        user.setLastName((String)userDetails.get("family_name"));
	        this.saveUser(user);
	        return user;
	    }

	    public User saveUser(User user) {
	        return this.userRepository.save(user);
	    }

	    public String generateToken(HttpServletResponse response, User user) {
	    	System.out.println("in the generate token");
	        Claims claims = Jwts.claims();
	        claims.put("email", user.getEmail());
	        claims.put("username", user.getUsername());

	        String jwtToken = Jwts.builder().setClaims(claims)
	                            .setExpiration(new Date(System.currentTimeMillis() + 864_00_000)).signWith(SignatureAlgorithm.HS512, "abcd")
	                            .compact();
	        response.addHeader("Authorization", "Bearer "+jwtToken);
	        response.addHeader("Access-Control-Expose-Headers", "Authorization");
	        return jwtToken;
	    }

	    public User loadByUsername(HttpServletRequest request) {
	        String token = request.getHeader("Authorization");
	        if (token != null) {
	            String user = Jwts.parser().setSigningKey("abcd").parseClaimsJws(token.replace("Bearer ", "")).getBody()
	                    .get("username", String.class);
	            if (user != null) {
	                System.out.println("user not null");
	                return this.userRepository.findByUsername(user);
	            }

	            return null;
	        }
	        return null;
	    }



}
