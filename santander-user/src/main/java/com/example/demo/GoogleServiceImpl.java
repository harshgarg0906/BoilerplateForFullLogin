package com.example.demo;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.plus.Person;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.model.User.userRole;


@Service
public class GoogleServiceImpl {

	@Value("${spring.social.google.app-id}")
	private String googleId;
	
	@Value("${spring.social.google.app-secret}")
	private String googleSecret;
	
	@Value("${redirect_url}")
	private String redirectUrl;

	private static final String System = "SYSTEM";

	// Creates Google OAuth Connection
	private GoogleConnectionFactory createGoogleConnection() {
		return new GoogleConnectionFactory(googleId, googleSecret);
	}

	// Opens the Google Consent Form
	public String googlelogin() {
		OAuth2Parameters parameters = new OAuth2Parameters();
		parameters.setRedirectUri(redirectUrl);
		parameters.setScope("profile email");
		return createGoogleConnection().getOAuthOperations().buildAuthenticateUrl(parameters);
	}

	// For getting Google oauth access token
	public String getGoogleAccessToken(String code) {
		return createGoogleConnection().getOAuthOperations().exchangeForAccess(code, redirectUrl, null)
				.getAccessToken();
	}

	// For getting google profile of particular user
	public User getGoogleUserProfile(String accessToken) {
		Google google = new GoogleTemplate(accessToken);
		Person person = google.plusOperations().getGoogleProfile();
		User user = new User();
		user.setUserName(person.getAccountEmail());
		user.setRole(userRole.CLIENT);
		user.setFirstName(person.getGivenName());
		user.setName(person.getDisplayName());
		user.setEmail(person.getAccountEmail());
		user.setAvatarURL(person.getImageUrl());
		user.setCreatedAt(LocalDateTime.now());
		user.setCreatedBy(System);
		user.setUpdatedAt(LocalDateTime.now());
		user.setUpdatedBy(System);
		user.setRead(false);
		return user;

	}

}
