package com.example.controller;

import java.util.List;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.service.AccountService;
import com.example.service.MessageService;

import com.example.entity.Account;
import com.example.entity.Message;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
@RestController
public class SocialMediaController {
  private AccountService accountService;
  private MessageService messageService;

  @Autowired
  public SocialMediaController(AccountService accountService, MessageService messageService) {
    this.accountService = accountService;
    this.messageService = messageService;
  }

  @PostMapping("/register")
  public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
    return ResponseEntity.ok().body(accountService.addNewAccount(account));
  }

  @PostMapping("/login")
  public ResponseEntity<Account> login(@RequestBody Account account) {
    return ResponseEntity.ok().body(accountService.verifyLogin(account));
  }

  @PostMapping("/messages")
  public ResponseEntity<Message> newMessage(@RequestBody Message message) {
    return ResponseEntity.ok().body(messageService.addNewMessage(message));
  }

  @GetMapping("/messages")
  public ResponseEntity<List<Message>> getAllMessages() {
    return ResponseEntity.ok().body(messageService.getAllMessages());
  }

  @GetMapping("/messages/{messageId}")
  public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
    return ResponseEntity.ok().body(messageService.getMessageById(messageId));
  }

  @DeleteMapping("/messages/{messageId}")
  public ResponseEntity<String> deleteMessage(@PathVariable int messageId) {
    return ResponseEntity.ok().body(messageService.deleteMessageById(messageId));
  }

  @PatchMapping("/messages/{messageId}")
  public ResponseEntity<Integer> updateMessage(@PathVariable int messageId,
      @RequestBody Map<String, String> requestBody) {
    String messageText = requestBody.get("messageText");
    return ResponseEntity.ok().body(messageService.updateMessageById(messageId, messageText));
  }

  @GetMapping("/accounts/{accountId}/messages")
  public ResponseEntity<List<Message>> getMessagesByAccount(@PathVariable int accountId) {
    return ResponseEntity.ok().body(messageService.getAllMessagesByAccount(accountId));
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
    if (ex.getStatus() == HttpStatus.UNAUTHORIZED) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getReason());
    }
    if (ex.getStatus() == HttpStatus.CONFLICT) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getReason());
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getReason());
  }
}
