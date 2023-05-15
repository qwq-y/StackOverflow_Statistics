package com.example.service;

import com.example.resportory.UserResportory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserResportory userResportory;

  @Autowired
  private UserService(UserResportory userResportory) {
    this.userResportory = userResportory;
  }
}
