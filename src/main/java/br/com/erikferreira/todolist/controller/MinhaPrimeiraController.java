package br.com.erikferreira.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/primeira-rota")
public class MinhaPrimeiraController {

  @GetMapping("/")
  public String primeiraMensagem() {
    return "Minha primeira mensagem";
  }
}
