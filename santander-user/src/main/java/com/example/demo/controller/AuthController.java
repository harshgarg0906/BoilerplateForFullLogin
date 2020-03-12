package com.example.demo.controller;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.GoogleServiceImpl;
import com.example.demo.model.User;
import com.example.demo.service.UserServiceImpl;
import com.example.demo.util.CookieUtil;
import com.example.demo.util.JwtUtil;

@Controller
@RequestMapping("/auth")
public class AuthController {

	    @Value("${client_dashboard_url}")
	    String clientDashboardUrl;


	    @Value("${Domain}")
	    String domain;

	    private static final String SYSTEM = "SYSTEM";

	    @Autowired
	    private GoogleServiceImpl googleService;

	    @Autowired
	   private UserServiceImpl userService;


	    private Logger logger = LoggerFactory.getLogger(this.getClass());

	    private static final String jwtTokenCookieName = "JWT-TOKEN";

	    // Redirect to login/consent form on Google's authentication page
	    @GetMapping(value = "/login")
	    public RedirectView googlelogin() {
	        RedirectView redirectview = new RedirectView();
	        String url = googleService.googlelogin();
	        redirectview.setUrl(url);
	        return redirectview;
	    }

	  
	 

	   
	    // Google calls back on user's successful authentication and consent
	    @GetMapping(value = "auth/login")
	    public RedirectView google(@RequestParam("code") String code, HttpServletResponse res) {
	    	System.out.println("in the auth login");
	        String accessToken = googleService.getGoogleAccessToken(code);
	        User user = googleService.getGoogleUserProfile(accessToken);
	        String jwtToken = JwtUtil.addToken(res, user);
	        CookieUtil.create(res, jwtTokenCookieName, jwtToken, false, -1, domain);
	        RedirectView redirectview = new RedirectView();
	        try {
	            userService.addUserData(user);
	        } catch (DuplicateKeyException exception) {
	            logger.error("In google method " + LocalDateTime.now() + " " + exception.getMessage());
	        } catch (Exception exception) {
	            logger.error("In google method " + LocalDateTime.now() + " " + exception.getMessage());
	        }
	        redirectview.setUrl(clientDashboardUrl);
	        return redirectview;
	    }



}
