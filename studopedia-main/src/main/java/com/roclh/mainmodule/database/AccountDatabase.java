package com.roclh.mainmodule.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.roclh.mainmodule.entities.Account;
import com.roclh.mainmodule.entities.Role;
import com.roclh.mainmodule.utils.AccountIdGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Component("accountDatabase")
public class AccountDatabase {
    @JsonIgnore
    private final String FILE = "accounts.xml";
    @JsonIgnore
    File file = new File(FILE);

    @JacksonXmlElementWrapper(localName = "accounts")
    private LinkedList<Account> accountDatabase;

    @SneakyThrows
    public AccountDatabase() {
        accountDatabase = new LinkedList<>();
    }

    @PostConstruct
    public void init() throws IOException {
        if (file.length() != 0) readXML();
    }

    private void readXML() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        accountDatabase = xmlMapper.readValue(file, AccountDatabase.class).getAccountDatabase();
        AccountIdGenerator.setId(accountDatabase.get(accountDatabase.size() - 1).getId() + 1);
    }

    private void writeXML() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        if (!file.exists()) throw new IOException();
        xmlMapper.writeValue(file, this);
    }

    public LinkedList<Account> findAll() {
        return accountDatabase;
    }

    public void add(Account account) {
        account.setId(AccountIdGenerator.getId());
        accountDatabase.add(account);
        try {
            writeXML();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Account> get(String username, String password) {
        return accountDatabase.stream()
                .filter(account -> account.getUsername().equals(username) && account.getPassword().equals(password))
                .findAny();
    }

    public Optional<Account> findByIdEquals(Long id) {
        return accountDatabase.stream()
                .filter(account -> account.getId().equals(id))
                .findAny();
    }

    public Optional<Account> findByUsername(String username) {
        return accountDatabase.stream()
                .filter(account -> account.getUsername().equals(username))
                .findAny();
    }

    public List<Account> getAdmins(){
        return accountDatabase.stream()
                .filter(account -> account.getRole().equals(Role.ADMIN))
                .toList();
    }
}
