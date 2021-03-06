package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.model.User;
import com.example.demo.service.AuthenticationService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
//@RestController
@RequestMapping("/auth")
public class DataController {

	@Value("${google.base.url}")
    private String baseUrl;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    String redirectUrl;

    @Autowired
    AuthenticationService userService;
    
    @GetMapping("/login")
    public RedirectView login() {
        System.out.println("in auth controller");
        String url = baseUrl + "?client_id="+this.clientId+"&redirect_uri="+this.redirectUrl+"&response_type=code&scope=profile+email";
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);

        return redirectView;
    }
    
//    @GetMapping("/login")
//    public String login1() {
//        System.out.println("in auth controller");
//        String url = baseUrl + "?client_id="+this.clientId+"&redirect_uri="+this.redirectUrl+"&response_type=code&scope=profile+email";
//        RedirectView redirectView = new RedirectView();
//        redirectView.setUrl(url);
//
//        return "hello wor;g";
//    }
    


    @GetMapping("auth/login")
    public RedirectView getUserDetails(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        System.out.println("in auth/login");
        String accessToken = userService.getAccessToken(code);
        System.out.println("in the acess token"+accessToken);
        User user = userService.getUserProfile(accessToken);
        System.out.println("printing the user etails"+user);
        String jwtToken = userService.generateToken(response,user);
        Cookie cookie = new Cookie("jwt-token", jwtToken);
        System.out.println("in auth/login with jwt token: " + jwtToken);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new RedirectView("http://localhost:8765/");
    }

    
//    @GetMapping("/auth/login")
//    public String getUserDetails1(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
//        System.out.println("in auth/login");
////        String accessToken = userService.getAccessToken(code);
////        User user = userService.getUserProfile(accessToken);
////        String jwtToken = userService.generateToken(response,user);
////        Cookie cookie = new Cookie("jwt-token", jwtToken);
////        System.out.println("in auth/login with jwt token: " + jwtToken);
////        cookie.setPath("/");
////        response.addCookie(cookie);
//        //return new RedirectView("http://localhost:8765/");
//        return "hello from the auth secongd logion";
//    }
}
