package com.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "answer")
@Entity
public class Answer {
  @Id
  @GeneratedValue
  private Long ownerAccountId;
  private Boolean isAccepted;
  private Long score;
  private Long lastActivityDate;
  private Long lastEditDate;
  private Long creationDate;
  private Long answerId;
  private Long questionId;
  private String contentLicense;
}
