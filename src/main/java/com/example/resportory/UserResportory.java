package com.example.resportory;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserResportory extends JpaRepository<User, Long> {

}
