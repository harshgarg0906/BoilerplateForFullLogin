package com.example.demo.controller;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.demo.model.UserCredentials;
//import com.example.demo.util.JwtUtil;
//
//@RestController
//public class WelcomeController {
//
//	@Autowired
//	JwtUtil jwtUtil;
//	
//	@Autowired
//	AuthenticationManager auth;
//	
//	@PostMapping("/authenticate")
//	public String authenticate(@RequestBody UserCredentials userData) throws Exception
//	{
//		try {
//			
//			auth.authenticate(
//					new UsernamePasswordAuthenticationToken(userData.getUserName(), userData.getPassword()));
//		}catch(Exception e)
//		{
//			throw new Exception("User not availaible");
//		}
//		return jwtUtil.generateToken(userData.getUserName());
//	}
//	
//	@GetMapping("/hello")
//	public String mrthod()
//	{
//		return "hell0";
//	}
//}

