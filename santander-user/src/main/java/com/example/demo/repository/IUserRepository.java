package com.example.demo.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.User;

public interface IUserRepository extends JpaRepository<User, String> {

    User findByEmail(String accountEmail);

    User findByUserName(String user);

}
