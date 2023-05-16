package com.example.service;

import com.example.resportory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  private UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
}
