package com.roclh.mainmodule.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.*;

import javax.sql.DataSource;
import java.util.Calendar;
import java.util.Properties;

import static org.roclh.common.constants.Constants.CRON_EVERY_FIVE_MINUTES;

@Configuration
@Slf4j
public class QuartzConfig {
    private ApplicationContext applicationContext;

    @Autowired
    private DataSource dataSource;


    public QuartzConfig(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }



    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutowiringSpringBeanFactory jobFactory = new AutowiringSpringBeanFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean scheduler(Trigger... triggers) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.instanceName", "Approve Job Instance");
        properties.setProperty("org.quartz.scheduler.instanceId", "ApproveIns1");
        properties.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setAutoStartup(true);
        schedulerFactory.setQuartzProperties(properties);
        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
        if (ArrayUtils.isNotEmpty(triggers)) {
            schedulerFactory.setTriggers(triggers);
        }
        return schedulerFactory;
    }

    static SimpleTriggerFactoryBean createTrigger(JobDetail jobDetail, long pollFrequencyMs, String triggerName) {
        log.debug("createTrigger(jobDetail={}, pollFrequencyMs={}, triggerName={})", jobDetail.toString(), pollFrequencyMs, triggerName);
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(0L);
        factoryBean.setRepeatInterval(pollFrequencyMs);
        factoryBean.setName(triggerName);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        return factoryBean;
    }

    static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String triggerName) {
        log.debug("createCronTrigger(jobDetail={}, cronExpression={}, triggerName={})", jobDetail.toString(), CRON_EVERY_FIVE_MINUTES, triggerName);
        // To fix an issue with time-based cron jobs
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression(CRON_EVERY_FIVE_MINUTES);
        factoryBean.setStartTime(calendar.getTime());
        factoryBean.setStartDelay(0L);
        factoryBean.setName(triggerName);
        factoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
        return factoryBean;
    }

    static JobDetailFactoryBean createJobDetail(Class jobClass, String jobName) {
        log.debug("createJobDetail(jobClass={}, jobName={})", jobClass.getName(), jobName);
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setName(jobName);
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(true);
        return factoryBean;
    }
}
