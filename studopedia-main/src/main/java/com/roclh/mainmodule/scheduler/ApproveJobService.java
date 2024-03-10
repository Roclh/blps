package com.roclh.mainmodule.scheduler;

import com.roclh.mainmodule.entities.Account;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.roclh.common.constants.Constants.MESSAGE_SEND_FROM;

@Service
@AllArgsConstructor
@Slf4j
public class ApproveJobService {

    private JavaMailSender emailSender;
    public void executeApproveJob(List<Account> admins) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MESSAGE_SEND_FROM);
        message.setSubject("Approve new articles");
        message.setText("There are numerous new articles! Approve them!");
        for (Account admin : admins){
            log.info("Mail is being sent to :" + admin.getUsername());
            message.setTo(admin.getUsername());
            emailSender.send(message);
        }

    }
}
