package com.example.demo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.example.demo.model.User;



public interface IUserService {

	User createUser(User user);

	List<User> findAll();

	User searchByPsid(String psid);

	User loadByUsername(HttpServletRequest request);
//	boolean authenticateUser(String psid, String password, HttpServletResponse response) throws Exception;
}
