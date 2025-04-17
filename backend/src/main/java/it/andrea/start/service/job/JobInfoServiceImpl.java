package it.andrea.start.service.job;

import it.andrea.start.dto.JobInfoDTO;
import it.andrea.start.error.exception.mapping.MappingToDtoException;
import it.andrea.start.mappers.job.JobInfoMapper;
import it.andrea.start.models.JobInfo;
import it.andrea.start.quartz.JobSchedulerCreator;
import it.andrea.start.repository.JobInfoRepository;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class JobInfoServiceImpl implements JobInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(JobInfoServiceImpl.class);

    private static final long SECOND_STANDARD_DELAY_START_TRIGGER = 5L;

    private final SchedulerFactoryBean schedulerFactoryBean;
    private final JobSchedulerCreator schedulerCreator;
    private final ApplicationContext context;

    private final JobInfoRepository jobInfoRepository;

    private final JobInfoMapper jobInfoMapper;

    public JobInfoServiceImpl(SchedulerFactoryBean schedulerFactoryBean, JobInfoRepository jobInfoRepository, ApplicationContext context, JobSchedulerCreator schedulerCreator,
            JobInfoMapper jobInfoMapper) {
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

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void scheduleNewJob(String jobName) throws ParseException {
        Optional<JobInfo> jobInfoOpt = jobInfoRepository.findByJobName(jobName);
        if (jobInfoOpt.isEmpty()) {
            return;
        }
        JobInfo jobInfo = jobInfoOpt.get();

        String cronExpression = jobInfo.getCronExpression();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            Class<?> jobClass = Class.forName(jobInfo.getJobClass());
            if (QuartzJobBean.class.isAssignableFrom(jobClass)) {
                Class<? extends QuartzJobBean> quartzJobClass = jobClass.asSubclass(QuartzJobBean.class);
                JobDetail jobDetail = JobBuilder.newJob(quartzJobClass).withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
                if (!scheduler.checkExists(jobDetail.getKey())) {
                    jobDetail = schedulerCreator.createJob(quartzJobClass, false, context, jobInfo.getJobName(), jobInfo.getJobGroup());

                    Optional<Trigger> triggerOpt = createTrigger(jobInfo, cronExpression);
                    if (triggerOpt.isPresent()) {
                        scheduler.scheduleJob(jobDetail, triggerOpt.get());
                        jobInfo.setIsActive(true);
                        jobInfoRepository.save(jobInfo);
                        schedulerFactoryBean.afterPropertiesSet();
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            LOG.error("Impossibile trovare la classe per il job {}. Classe non trovata: {}", jobName, jobInfo.getJobClass());
        } catch (SchedulerException e) {
            LOG.error("Errore durante la programmazione del job {}: {}", jobName, e.getMessage());
        } catch (Exception e) {
            LOG.error("Errore generico nella schedulazione del job {}: {}", jobName, e.getMessage());
        }
    }

    @Override
    public void updateScheduleJob(String jobName) throws ParseException {
        Optional<JobInfo> jobInfoOpt = jobInfoRepository.findByJobName(jobName);
        if (jobInfoOpt.isEmpty()) {
            return;
        }
        JobInfo jobInfo = jobInfoOpt.get();

        String cronExpression = jobInfo.getCronExpression();
        Trigger newTrigger;
        if (Boolean.TRUE.equals(jobInfo.getCronJob())) {
            newTrigger = schedulerCreator.createCronTrigger(jobInfo.getJobName(),
                    LocalDateTime.now(),
                    cronExpression,
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        } else {
            newTrigger = schedulerCreator.createSimpleTrigger(jobInfo.getJobName(),
                    LocalDateTime.now(),
                    SECOND_STANDARD_DELAY_START_TRIGGER,
                    jobInfo.getRepeatTime(),
                    jobInfo.getRepeatCount(),
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        }

        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
            jobInfo.setIsActive(true);
            jobInfoRepository.save(jobInfo);
        } catch (SchedulerException e) {
            LOG.error("Errore durante l'aggiornamento del job {} nel programma: {}", jobName, e.getMessage());
        }
    }

    @Override
    public boolean unScheduleJob(String jobName) {
        Optional<JobInfo> jobInfoOpt = jobInfoRepository.findByJobName(jobName);
        if (jobInfoOpt.isEmpty()) {
            return false;
        }
        JobInfo jobInfo = jobInfoOpt.get();
        try {
            jobInfo.setIsActive(false);
            jobInfoRepository.save(jobInfo);
            return schedulerFactoryBean.getScheduler().unscheduleJob(new TriggerKey(jobName));
        } catch (SchedulerException e) {
            return false;
        }
    }

    @Override
    public boolean deleteJob(String jobName) {
        Optional<JobInfo> jobInfoOpt = jobInfoRepository.findByJobName(jobName);
        if (jobInfoOpt.isEmpty()) {
            return false;
        }
        JobInfo jobInfo = jobInfoOpt.get();
        try {
            jobInfo.setIsActive(false);
            jobInfoRepository.save(jobInfo);
            return schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
        } catch (SchedulerException e) {
            return false;
        }
    }

    @Override
    public boolean pauseJob(String jobName) {
        Optional<JobInfo> jobInfoOpt = jobInfoRepository.findByJobName(jobName);
        if (jobInfoOpt.isEmpty()) {
            return false;
        }
        JobInfo jobInfo = jobInfoOpt.get();
        try {
            jobInfo.setIsActive(false);
            jobInfoRepository.save(jobInfo);
            schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            return false;
        }
    }

    @Override
    public boolean resumeJob(String jobName) {
        Optional<JobInfo> jobInfoOpt = jobInfoRepository.findByJobName(jobName);
        if (jobInfoOpt.isEmpty()) {
            return false;
        }
        JobInfo jobInfo = jobInfoOpt.get();
        try {
            jobInfo.setIsActive(true);
            jobInfoRepository.save(jobInfo);
            schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            return false;
        }
    }

    @Override
    public boolean startJobNow(String jobName) {
        Optional<JobInfo> jobInfoOpt = jobInfoRepository.findByJobName(jobName);
        if (jobInfoOpt.isEmpty()) {
            return false;
        }
        JobInfo jobInfo = jobInfoOpt.get();
        try {
            schedulerFactoryBean.getScheduler().triggerJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            return false;
        }
    }

    private void startJob(Scheduler scheduler, JobInfo jobInfo) throws ParseException {
        try {
            String cronExpression = jobInfo.getCronExpression();
            Class<?> jobClass = Class.forName(jobInfo.getJobClass());
            if (QuartzJobBean.class.isAssignableFrom(jobClass)) {
                Class<? extends QuartzJobBean> quartzJobClass = jobClass.asSubclass(QuartzJobBean.class);
                JobDetail jobDetail = JobBuilder.newJob(quartzJobClass).withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
                if (!scheduler.checkExists(jobDetail.getKey())) {
                    jobDetail = schedulerCreator.createJob(quartzJobClass, false, context, jobInfo.getJobName(), jobInfo.getJobGroup());
                    Optional<Trigger> triggerOpt = createTrigger(jobInfo, cronExpression);
                    if (triggerOpt.isPresent()) {
                        scheduler.scheduleJob(jobDetail, triggerOpt.get());
                        schedulerFactoryBean.afterPropertiesSet();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Optional<Trigger> createTrigger(JobInfo jobInfo, String cronExpression) throws ParseException {
        Optional<Trigger> trigger = Optional.empty();
        if (Boolean.TRUE.equals(jobInfo.getCronJob()) && CronExpression.isValidExpression(cronExpression)) {
            trigger = Optional.of(schedulerCreator.createCronTrigger(
                    jobInfo.getJobName(),
                    LocalDateTime.now(),
                    cronExpression,
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW
            ));
        }
        return trigger;
    }

}
