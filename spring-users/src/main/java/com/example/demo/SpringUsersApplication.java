package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.model.User;
import com.example.demo.model.User.UserRole;
import com.example.demo.repo.UserRepo;

@SpringBootApplication
public class SpringUsersApplication {

	@Autowired
	UserRepo userRepo;
	public static void main(String[] args) {
		SpringApplication.run(SpringUsersApplication.class, args);
	}

	@PostConstruct
	public void initData()
	{
		System.out.println("in the init method");
		List<User> list=new ArrayList<>();
		list.add(new User("10", "harsh@gmail.com", "$2y$12$fgX9SBXUK/oulTAQP3eieuRDU04Xee94YZ4GxxbO4GPNyMh8TH4NC", UserRole.USER, true));
		list.add(new User("11", "rahul@gmail.com", "$2y$12$fgX9SBXUK/oulTAQP3eieuRDU04Xee94YZ4GxxbO4GPNyMh8TH4NC", UserRole.USER, true));
		userRepo.saveAll(list);
		
	}
}