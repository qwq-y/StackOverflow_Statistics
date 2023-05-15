package com.example.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "question")
@Entity
public class Question {
  @Id
  @GeneratedValue
  private Long ownerAccountId;
  private Boolean isAnswered;
  private Long viewCount;
  private Long protectedDate;
  private Long acceptedAnswerId;
  private Long answerCount;
  private Long lastActivityDate;
  private Long creationDate;
  private Long lastEditDate;
  private Long questionId;
  private String contentLicense;
  private String link;
  private String title;
}
