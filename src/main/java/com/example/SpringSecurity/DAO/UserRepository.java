package com.example.SpringSecurity.DAO;

import com.example.SpringSecurity.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByName(String name);
}
