package br.com.erikferreira.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;


  @PostMapping("/")
  public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    var userId = request.getAttribute("userId");
    taskModel.setUserId((UUID) userId);
    
    var currentDate = LocalDateTime.now();
    var startAt = taskModel.getStartAt();
    var endAt = taskModel.getEndAt();

    System.out.println("currentDate" + currentDate);
    System.out.println("startAt" + startAt);

    if(currentDate.isAfter(startAt) || currentDate.isAfter(endAt)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("A data de início/término deve ser maior que a data atual.");
    }

    if(startAt.isAfter(endAt)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("A data de início deve ser menor que a data de término.");
    }

    var task = this.taskRepository.save(taskModel);
    return ResponseEntity.status(HttpStatus.OK).body(task);
  }
}
