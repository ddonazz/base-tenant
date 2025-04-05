package it.andrea.start.quartz;

import java.text.ParseException;
import java.time.LocalDateTime;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import it.andrea.start.utils.HelperDate;

@Component
public class JobSchedulerCreator {

    public JobDetail createJob(Class<? extends QuartzJobBean> jobClass, boolean isDurable, ApplicationContext context, String jobName, String jobGroup) {
	JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
	factoryBean.setJobClass(jobClass);
	factoryBean.setDurability(isDurable);
	factoryBean.setApplicationContext(context);
	factoryBean.setName(jobName);
	factoryBean.setGroup(jobGroup);

	JobDataMap jobDataMap = new JobDataMap();
	jobDataMap.put(jobName + jobGroup, jobClass.getName());
	factoryBean.setJobDataMap(jobDataMap);

	factoryBean.afterPropertiesSet();

	return factoryBean.getObject();
    }

    public CronTrigger createCronTrigger(String triggerName, LocalDateTime startTime, String cronExpression, int misFireInstruction) throws ParseException {
	CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
	factoryBean.setName(triggerName);
	factoryBean.setStartTime(HelperDate.localDateTimeToDate(startTime));
	factoryBean.setCronExpression(cronExpression);
	factoryBean.setMisfireInstruction(misFireInstruction);
	factoryBean.afterPropertiesSet();
	return factoryBean.getObject();
    }

    public SimpleTrigger createSimpleTrigger(String triggerName, LocalDateTime startTime, Long delayTime, Long repeatTime, int repeatCount, int misFireInstruction) {
	if (delayTime != null) {
	    startTime = startTime.plusSeconds(delayTime);
	}
	SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
	factoryBean.setName(triggerName);
	factoryBean.setStartTime(HelperDate.localDateTimeToDate(startTime));
	factoryBean.setRepeatInterval(repeatTime != null ? repeatTime : 0L);
	factoryBean.setRepeatCount(repeatCount);
	factoryBean.setMisfireInstruction(misFireInstruction);
	factoryBean.afterPropertiesSet();
	return factoryBean.getObject();
    }

}
