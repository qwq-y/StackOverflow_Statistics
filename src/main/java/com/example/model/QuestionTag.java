package com.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "question_tag")
@Entity
public class QuestionTag {
  @Id
  @GeneratedValue
  private Long questionId;
  private String tag;
}
