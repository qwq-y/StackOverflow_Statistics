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
  private long questionId;
  private String title;
  private String body;
  private long creationDate;
  private int score;
  private int viewCount;
  private int answerCount;
}
