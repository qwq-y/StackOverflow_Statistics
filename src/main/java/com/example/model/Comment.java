package com.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "comment")
@Entity
public class Comment {
  @Id
  @GeneratedValue
  private Long ownerAccountId;
  private Long replyToUser;
  private Boolean edited;
  private Long score;
  private Long creationDate;
  private Long postId;
  private Long commentId;
  private String contentLicense;
}
