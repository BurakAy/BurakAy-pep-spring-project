package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
  private AccountRepository accountRepository;

  @Autowired
  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public Account addNewAccount(Account account) {
    Optional<Account> optionalAccount = accountRepository.findByUsername(account.getUsername());
    if (optionalAccount.isEmpty()) {
      if (!account.getUsername().isEmpty() && account.getPassword().length() > 4) {
        return accountRepository.save(account);
      }
    }
    if (optionalAccount.isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong.");
  }

  public Account verifyLogin(Account account) {
    Optional<Account> optionalAccount = accountRepository.findByUsernameAndPassword(account.getUsername(),
        account.getPassword());
    if (optionalAccount.isPresent()) {
      Account existingAccount = optionalAccount.get();
      if (existingAccount.getPassword().equals(account.getPassword())
          && existingAccount.getUsername().equals(account.getUsername())) {
        return existingAccount;
      }
    }
    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password.");
  }
}
