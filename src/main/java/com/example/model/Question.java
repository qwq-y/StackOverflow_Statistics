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
  private String tags;
  private long ownerAccountId;
  private boolean isAnswered;
  private long viewCount;
  private long protectedDate;
  private long acceptedAnswerId;
  private long answerCount;
  private long lastActivityDate;
  private long creationDate;
  private long lastEditDate;
  private long questionId;
  private String contentLicense;
  private String link;
  private String title;
}
