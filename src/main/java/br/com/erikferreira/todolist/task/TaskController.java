package br.com.erikferreira.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.erikferreira.todolist.utils.Utils;
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

  @GetMapping("/")
  public List<TaskModel> list(HttpServletRequest request) {
    var userId = request.getAttribute("userId");
    var tasks = this.taskRepository.findByUserId((UUID) userId);

    return tasks;
  }

  @PutMapping("/{id}")
  public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
    var task = this.taskRepository.findById(id).orElse(null);
    var userId = request.getAttribute("userId");

    if(task == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Tarefa não encontrada.");
    }

    if(!task.getUserId().equals(userId)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Usuário não tem permissão para alterar essa tarefa.");
    }    

    Utils.copyNonNullProperties(taskModel, task);

    var taskSaved = this.taskRepository.save(task);

    return ResponseEntity.ok().body(taskSaved);
  }
}
