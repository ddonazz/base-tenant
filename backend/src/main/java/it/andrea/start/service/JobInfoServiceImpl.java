package it.andrea.start.service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.quartz.CronExpression;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.andrea.start.constants.ApplicationConstants;
import it.andrea.start.dto.JobInfoDTO;
import it.andrea.start.exception.MappingToDtoException;
import it.andrea.start.mappers.job.JobInfoMapper;
import it.andrea.start.models.JobInfo;
import it.andrea.start.quartz.JobSchedulerCreator;
import it.andrea.start.repository.JobInfoRepository;

@Service
public class JobInfoServiceImpl implements JobInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(JobInfoServiceImpl.class);

    private final SchedulerFactoryBean schedulerFactoryBean;
    private final JobSchedulerCreator schedulerCreator;
    private final ApplicationContext context;

    private final JobInfoRepository jobInfoRepository;

    private final JobInfoMapper jobInfoMapper;

    public JobInfoServiceImpl(SchedulerFactoryBean schedulerFactoryBean, JobInfoRepository jobInfoRepository, ApplicationContext context, JobSchedulerCreator schedulerCreator, JobInfoMapper jobInfoMapper) {
	this.schedulerFactoryBean = schedulerFactoryBean;
	this.jobInfoRepository = jobInfoRepository;
	this.context = context;
	this.schedulerCreator = schedulerCreator;
	this.jobInfoMapper = jobInfoMapper;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public Collection<JobInfoDTO> listJobs() throws MappingToDtoException {
	List<JobInfo> jobs = jobInfoRepository.findAll();
	return jobInfoMapper.toDtos(jobs);
    }

    @Override
    public void startAllSchedulers() {
	List<JobInfo> jobInfoList = jobInfoRepository.findAll();
	Scheduler scheduler = schedulerFactoryBean.getScheduler();
	jobInfoList.forEach(jobInfo -> {
	    if (Boolean.TRUE.equals(jobInfo.getIsActive())) {
		try {
		    startJob(scheduler, jobInfo);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
	    }
	});

    }

    private void startJob(Scheduler scheduler, JobInfo jobInfo) throws ParseException {
	try {
	    String cronExpression = jobInfo.getCronExpression();
	    if (ApplicationConstants.INIZIALIZE_JOB.equals(jobInfo.getJobName())) {
		cronExpression = generateCronExpressionForInitializeJob();
	    }
	    Class<?> jobClass = Class.forName(jobInfo.getJobClass());
	    if (QuartzJobBean.class.isAssignableFrom(jobClass)) {
		Class<? extends QuartzJobBean> quartzJobClass = jobClass.asSubclass(QuartzJobBean.class);
		JobDetail jobDetail = JobBuilder.newJob(quartzJobClass).withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
		if (!scheduler.checkExists(jobDetail.getKey())) {
		    jobDetail = schedulerCreator.createJob(quartzJobClass, false, context, jobInfo.getJobName(), jobInfo.getJobGroup());
		    Trigger trigger = createTrigger(jobInfo, cronExpression);
		    if (trigger != null) {
			scheduler.scheduleJob(jobDetail, trigger);
			try {
			    schedulerFactoryBean.afterPropertiesSet();
			} catch (Exception e) {
			    e.printStackTrace();
			}
		    }
		}
	    }
	} catch (ClassNotFoundException | SchedulerException e) {
	    e.printStackTrace();
	}
    }

    private Trigger createTrigger(JobInfo jobInfo, String cronExpression) throws ParseException {
	Trigger trigger = null;
	if (Boolean.TRUE.equals(jobInfo.getCronJob()) && CronExpression.isValidExpression(cronExpression)) {
	    trigger = schedulerCreator.createCronTrigger(jobInfo.getJobName(), LocalDateTime.now(), cronExpression, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
	}
	return trigger;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void scheduleNewJob(String jobName) throws ParseException {
	JobInfo jobInfo = jobInfoRepository.findByJobName(jobName);
	if (jobInfo == null) {
	    return;
	}

	String cronExpression = jobInfo.getCronExpression();
	if (ApplicationConstants.INIZIALIZE_JOB.equals(jobInfo.getJobName())) {
	    cronExpression = generateCronExpressionForInitializeJob();
	}

	try {
	    Scheduler scheduler = schedulerFactoryBean.getScheduler();

	    Class<?> jobClass = Class.forName(jobInfo.getJobClass());
	    if (QuartzJobBean.class.isAssignableFrom(jobClass)) {
		Class<? extends QuartzJobBean> quartzJobClass = jobClass.asSubclass(QuartzJobBean.class);
		JobDetail jobDetail = JobBuilder.newJob(quartzJobClass).withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
		if (!scheduler.checkExists(jobDetail.getKey())) {
		    jobDetail = schedulerCreator.createJob(quartzJobClass, false, context, jobInfo.getJobName(), jobInfo.getJobGroup());

		    Trigger trigger = createTrigger(jobInfo, cronExpression);

		    if (trigger != null) {
			scheduler.scheduleJob(jobDetail, trigger);
			jobInfo.setIsActive(true);
			jobInfoRepository.save(jobInfo);
			try {
			    schedulerFactoryBean.afterPropertiesSet();
			} catch (Exception e) {
			    e.printStackTrace();
			}

			LOG.info("Nuovo job {} programmato con successo.", jobName);
		    }
		} else {
		    LOG.warn("Il job {} è già stato programmato.", jobName);
		}
	    }
	} catch (ClassNotFoundException e) {
	    LOG.error("Impossibile trovare la classe per il job {}. Classe non trovata: {}", jobName, jobInfo.getJobClass());
	} catch (SchedulerException e) {
	    LOG.error("Errore durante la programmazione del job {}: {}", jobName, e.getMessage());
	}
    }

    @Override
    public void updateScheduleJob(String jobName) throws ParseException {
	JobInfo jobInfo = jobInfoRepository.findByJobName(jobName);
	if (jobInfo == null) {
	    LOG.error("Il job con il nome {} non è stato trovato nel repository.", jobName);
	    return;
	}

	boolean isInitializeJob = ApplicationConstants.INIZIALIZE_JOB.equals(jobInfo.getJobName());
	String cronExpression = jobInfo.getCronExpression();
	if (isInitializeJob) {
	    cronExpression = generateCronExpressionForInitializeJob();
	}

	Trigger newTrigger;
	if (Boolean.TRUE.equals(jobInfo.getCronJob())) {
	    newTrigger = schedulerCreator.createCronTrigger(jobInfo.getJobName(), LocalDateTime.now(), cronExpression, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
	} else {
	    newTrigger = schedulerCreator.createSimpleTrigger(jobInfo.getJobName(), LocalDateTime.now(), ApplicationConstants.SECOND_STANDARD_DELAY_START_TRIGGER, jobInfo.getRepeatTime(), jobInfo.getRepeatCount(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
	}

	try {
	    Scheduler scheduler = schedulerFactoryBean.getScheduler();
	    scheduler.rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
	    jobInfo.setIsActive(true);
	    jobInfoRepository.save(jobInfo);
	    LOG.info("job {} aggiornato con successo nel programma.", jobName);
	} catch (SchedulerException e) {
	    LOG.error("Errore durante l'aggiornamento del job {} nel programma: {}", jobName, e.getMessage());
	}
    }

    @Override
    public boolean unScheduleJob(String jobName) {
	try {
	    JobInfo jobInfo = jobInfoRepository.findByJobName(jobName);
	    if (jobInfo == null) {
		LOG.error("Il job con il nome {} non è stato trovato nel repository.", jobName);
		return false;
	    }
	    jobInfo.setIsActive(false);
	    jobInfoRepository.save(jobInfo);
	    boolean isUnscheduled = schedulerFactoryBean.getScheduler().unscheduleJob(new TriggerKey(jobName));
	    if (isUnscheduled) {
		LOG.info("Il job {} è stato rimosso dal programma con successo.", jobName);
	    } else {
		LOG.error("Impossibile rimuovere il job {} dal programma.", jobName);
	    }
	    return isUnscheduled;
	} catch (SchedulerException e) {
	    LOG.error("Errore durante la rimozione del job {} dal programma: {}", jobName, e.getMessage());
	    return false;
	}
    }

    @Override
    public boolean deleteJob(String jobName) {
	try {
	    JobInfo jobInfo = jobInfoRepository.findByJobName(jobName);
	    if (jobInfo == null) {
		LOG.error("Il job con il nome {} non è stato trovato nel repository.", jobName);
		return false;
	    }
	    jobInfo.setIsActive(false);
	    jobInfoRepository.save(jobInfo);
	    boolean isDeleted = schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
	    if (isDeleted) {
		LOG.info("Il job {} è stato eliminato dal programma con successo.", jobName);
	    } else {
		LOG.error("Impossibile eliminare il job {} dal programma.", jobName);
	    }
	    return isDeleted;
	} catch (SchedulerException e) {
	    LOG.error("Errore durante l'eliminazione del job {} dal programma: {}", jobName, e.getMessage());
	    return false;
	}
    }

    @Override
    public boolean pauseJob(String jobName) {
	try {
	    JobInfo jobInfo = jobInfoRepository.findByJobName(jobName);
	    if (jobInfo == null) {
		LOG.error("Il job con il nome {} non è stato trovato nel repository.", jobName);
		return false;
	    }
	    jobInfo.setIsActive(false);
	    jobInfoRepository.save(jobInfo);
	    schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
	    LOG.info("Il job {} è stato messo in pausa con successo.", jobName);
	    return true;
	} catch (SchedulerException e) {
	    LOG.error("Errore durante la messa in pausa del job {} nel programma: {}", jobName, e.getMessage());
	    return false;
	}
    }

    @Override
    public boolean resumeJob(String jobName) {
	try {
	    JobInfo jobInfo = jobInfoRepository.findByJobName(jobName);
	    if (jobInfo == null) {
		LOG.error("Il job con il nome {} non è stato trovato nel repository.", jobName);
		return false;
	    }
	    jobInfo.setIsActive(true);
	    jobInfoRepository.save(jobInfo);
	    schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
	    LOG.info("Il job {} è stato ripreso con successo.", jobName);
	    return true;
	} catch (SchedulerException e) {
	    LOG.error("Errore durante il ripristino del job {} nel programma: {}", jobName, e.getMessage());
	    return false;
	}
    }

    @Override
    public boolean startJobNow(String jobName) {
	try {
	    JobInfo jobInfo = jobInfoRepository.findByJobName(jobName);
	    if (jobInfo == null) {
		LOG.error("Il job con il nome {} non è stato trovato nel repository.", jobName);
		return false;
	    }
	    schedulerFactoryBean.getScheduler().triggerJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
	    LOG.info("Il job {} è stato avviato immediatamente con successo.", jobName);
	    return true;
	} catch (SchedulerException e) {
	    LOG.error("Errore durante l'avvio immediato del job {} nel programma: {}", jobName, e.getMessage());
	    return false;
	}
    }

    private String generateCronExpressionForInitializeJob() {
	LocalDateTime currentTime = LocalDateTime.now().plusSeconds(20);

	int second = currentTime.getSecond();
	int minute = currentTime.getMinute();
	int hour = currentTime.getHour();
	int dayOfMonth = currentTime.getDayOfMonth();
	int month = currentTime.getMonthValue();
	int year = currentTime.getYear();

	String expression = String.format("%d %d %d %d %d ? %d", second, minute, hour, dayOfMonth, month, year);

	if (!CronExpression.isValidExpression(expression)) {
	    LOG.error("Espressione cron non valida: {}", expression);
	    return null;
	}

	LOG.info("Espressione cron generata con successo: {}", expression);
	return expression;
    }

}
