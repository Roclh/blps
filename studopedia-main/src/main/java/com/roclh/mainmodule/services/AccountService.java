package com.roclh.mainmodule.services;

import org.roclh.common.exceptions.AccountNotFountException;
import com.roclh.mainmodule.messaging.AuthenticationResponse;
import com.roclh.mainmodule.messaging.LoginRequest;
import com.roclh.mainmodule.messaging.RegisterRequest;
import com.roclh.mainmodule.database.AccountDatabase;
import com.roclh.mainmodule.entities.Account;
import com.roclh.mainmodule.entities.Role;
import com.roclh.mainmodule.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountDatabase database;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public List<Account> findAll() {
        return database.findAll();
    }

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) throws AccountNotFountException {
        Optional<Account> optional = database.findByUsername(request.getUsername());
        if(optional.isPresent()) throw new AccountNotFountException();
        Account user = Account.builder()
                .firstName(request.getFistName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        database.add(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Transactional
    public AuthenticationResponse registerAdmin(RegisterRequest request) {
        Account admin = Account.builder()
                .firstName(request.getFistName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
        database.add(admin);
        var jwtToken = jwtService.generateToken(admin);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse login(LoginRequest request) throws AccountNotFountException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        Optional<Account> user = database.findByUsername(request.getUsername());
        if (user.isEmpty()) throw new AccountNotFountException();
        Account account = user.get();
        var jwtToken = jwtService.generateToken(account);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public List<Account> getAdmins(){
        return database.getAdmins();
    }
}
