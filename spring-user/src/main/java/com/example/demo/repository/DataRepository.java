package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

public interface DataRepository extends JpaRepository<User, String>{

	 User findByUsername(String username);
}


