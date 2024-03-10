package com.roclh.mainmodule.scheduler;

import com.roclh.mainmodule.services.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.roclh.mainmodule.entities.Account;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class ApproveJob implements Job {
    private ApproveJobService jobService;
    private AccountService accountService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("Job of approving emails is starting");
        List<Account> admins = accountService.getAdmins();
        jobService.executeApproveJob(admins);
    }
}
