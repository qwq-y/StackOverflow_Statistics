package com.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "user_")
@Entity
public class User {
  @Id
  @GeneratedValue
  private Long accountId;
  private Long reputation;
  private Long userId;
  private String userType;
  private Integer acceptRate;
  private String profileImage;
  private String displayName;
  private String link;
}
