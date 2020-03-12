package com.example.demo.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ResponseModel;
import com.example.demo.model.User;
import com.example.demo.model.User.UserRole;
import com.example.demo.service.UserService;
import com.example.demo.util.CookieUtil;
import com.example.demo.util.JwtUtil;


@Controller
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private ResponseModel responseModel;

	private static final String jwtTokenCookieName = "JWT-TOKEN";

	@PostMapping("/login")
	public String checkLogin(@RequestBody User user, HttpServletResponse res, HttpServletRequest request) {
//		RedirectView redirectview = new RedirectView();
		System.out.println("In the login system");
		System.out.println(user.toString());
		String psid = user.getPsid();
		if (userService.searchByPsid(psid) != null) {
			User dbuser = userService.searchByPsid(psid);
			System.out.println("data coming from the  database");
			System.out.println(dbuser.toString());
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			System.out.println(user.getPassword());
			System.out.println(dbuser.getPassword());
			Boolean flag = passwordEncoder.matches(user.getPassword(), dbuser.getPassword());
			System.out.println("flag: " + flag);
			if (flag) {
				UserRole role = dbuser.getUserRole();
				String jwtToken = JwtUtil.addToken(res, psid, role);
				System.out.println("jwToken=====> " + jwtToken);
				Cookie cookie = CookieUtil.create(res, jwtTokenCookieName, jwtToken, false, -1, "localhost");
				System.out.println("Cookie=========> " + cookie);
//				redirectview.setUrl("http://localhost:8765/#/dashboard");
//				redirectview.setStatusCode(HttpStatus.OK);
//				return redirectview;
//				return "Login Successful";
//				request.setAttribute("OK", HttpStatus.OK);
//				String encodeURI = URLEncoder.encode("http://localhost:8765/#/dashboard", "UTF-8");
//				System.out.println("encoded uri======= "+encodeURI);
//				res.setHeader("uri", encodeURI);
				return "redirect:http://localhost:8765/user/auth/success";

			} else {
//				redirectview.setUrl("http://localhost:8765/#/");
//				redirectview.setStatusCode(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
//				return redirectview;
//				return "Wrong Password Entered";
//				request.setAttribute("Wrong Password Entered", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
				return "redirect:http://localhost:8765/user/auth/failure";
			}
		} else {
//			redirectview.setUrl("http://localhost:8765/#/");
//			redirectview.setStatusCode(HttpStatus.NOT_FOUND);
//			return redirectview;
//			return "PSID not found";
//			request.setAttribute("PSID not found", HttpStatus.NOT_FOUND);
			return "redirect:http://localhost:8765/user/auth/invalid";
		}

	}

	@GetMapping("/success")
	public ResponseEntity<ResponseModel> redirectViewOnSuccess(HttpServletRequest request, HttpServletResponse res) {
//		String req = URLEncoder.encode("http://localhost:8765/#/dashboard", "UTF-8");
//		System.out.println("encoded uri======= "+req);
//		request.setAttribute("OK", HttpStatus.OK);
//		return "redirect:" + req;
		responseModel.setStatusCode(200);
		responseModel.setMessage("Successful Login");
		return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
	}

	@GetMapping("/failure")
	public ResponseEntity<ResponseModel> redirectViewOnFailureOfPassword(HttpServletRequest request,
			HttpServletResponse res) {
//		request.setAttribute("Wrong Password Entered", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
//		return "redirect:http://localhost:8765/#/";
		responseModel.setStatusCode(203);
		responseModel.setMessage("Wrong Password");
		return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
	}

	@GetMapping("/invalid")
	public ResponseEntity<ResponseModel> redirectViewOnFailureOfPsid(HttpServletRequest request,
			HttpServletResponse res) {
//		request.setAttribute("PSID not found", HttpStatus.NOT_FOUND);
//		return "redirect:http://localhost:8765/#/";
		responseModel.setStatusCode(404);
		responseModel.setMessage("PSID not found");
		return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.NOT_FOUND);
	}
	
	
	@GetMapping("/data")
	@ResponseBody
	public User method()
	{
		System.out.println("in the method of data");
		return new User("10", "harsh@gmail.com", "$2y$12$fgX9SBXUK/oulTAQP3eieuRDU04Xee94YZ4GxxbO4GPNyMh8TH4NC", UserRole.USER, true);
	}



}

