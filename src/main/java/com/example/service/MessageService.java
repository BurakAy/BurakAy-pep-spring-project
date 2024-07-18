package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
  private MessageRepository messageRepository;
  private AccountRepository accountRepository;

  @Autowired
  public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
    this.messageRepository = messageRepository;
    this.accountRepository = accountRepository;
  }

  public Message addNewMessage(Message message) {
    if (message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Message cannot be empty or longer than 255 characters.");
    }

    Optional<Account> optionalAccount = accountRepository.findById(message.getPostedBy());
    if (optionalAccount.isPresent()) {
      return messageRepository.save(message);
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found.");
  }

  public List<Message> getAllMessages() {
    return (List<Message>) messageRepository.findAll();
  }

  public Message getMessageById(int message_id) {
    Optional<Message> optionalMessage = messageRepository.findById(message_id);
    if (optionalMessage.isPresent()) {
      Message message = optionalMessage.get();
      return message;
    }
    return null;
  }

  public String deleteMessageById(int message_id) {
    Optional<Message> optionalMessage = messageRepository.findById(message_id);
    if (optionalMessage.isPresent()) {
      messageRepository.deleteById(message_id);
      return "1";
    }
    return "";
  }

  public int updateMessageById(int message_id, String message_text) {
    if (message_text.isEmpty() || message_text.length() > 255) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Message cannot be empty or longer than 255 characters.");
    }

    Optional<Message> optionalMessage = messageRepository.findById(message_id);
    if (optionalMessage.isPresent()) {
      Message message = optionalMessage.get();
      message.setMessageText(message_text);
      messageRepository.save(message);
      return 1;
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message not found.");
  }

  public List<Message> getAllMessagesByAccount(int account_id) {
    return messageRepository.findByPostedBy(account_id);
  }
}
