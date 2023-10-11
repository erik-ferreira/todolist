package br.com.erikferreira.todolist.task;

import java.util.UUID;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(length = 50)
  private String title;
  private String description;
  private LocalDateTime startAt;
  private LocalDateTime endAt;
  private String priority;
  private UUID userId;
  
  @CreationTimestamp
  private LocalDateTime createdAt;
}
