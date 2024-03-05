package com.roclh.mainmodule.controllers;

import com.roclh.mainmodule.entities.Account;
import com.roclh.mainmodule.exceptions.AccountNotFountException;
import com.roclh.mainmodule.messaging.AuthenticationResponse;
import com.roclh.mainmodule.messaging.LoginRequest;
import com.roclh.mainmodule.messaging.RegisterRequest;
import com.roclh.mainmodule.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AccountController {

    private final Logger log = LogManager.getLogger(AccountController.class);
    private final AccountService accountService;

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        log.info("Got request to list all accounts");
        return ResponseEntity.ok(accountService.findAll());
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerAccount(
            @RequestBody RegisterRequest request
    ) throws AccountNotFountException {
        return ResponseEntity.ok(accountService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginAccount(
            @RequestBody LoginRequest request
    ) throws AccountNotFountException {
        return ResponseEntity.ok(accountService.login(request));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(accountService.registerAdmin(request));
    }
}
