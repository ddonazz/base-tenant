package it.andrea.start.service.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.andrea.start.dto.JobInfoDTO;
import it.andrea.start.error.exception.mapping.MappingToDtoException;
// import it.andrea.start.error.exception.job.JobNotFoundException; // Esempio eccezione custom
// import it.andrea.start.error.exception.job.JobSchedulingException; // Esempio eccezione custom
// import it.andrea.start.error.exception.job.JobControlException; // Esempio eccezione custom
import it.andrea.start.mappers.job.JobInfoMapper;
import it.andrea.start.models.JobInfo;
import it.andrea.start.repository.JobInfoRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobInfoServiceImpl implements JobInfoService {

    private static final Logger log = LoggerFactory.getLogger(JobInfoServiceImpl.class);

    @Lazy
    private final Scheduler scheduler;
    private final JobInfoRepository jobInfoRepository;
    private final JobInfoMapper jobInfoMapper;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public Collection<JobInfoDTO> listJobs() throws MappingToDtoException {
        List<JobInfo> jobs = jobInfoRepository.findAll();
        return jobInfoMapper.toDtos(jobs);
    }

    @PostConstruct
    @Transactional
    public void initializeScheduledJobs() {
        log.info("Avvio inizializzazione Job Quartz...");
        List<JobInfo> activeJobs = jobInfoRepository.findByIsActiveTrue();
        log.info("Trovati {} job attivi nel database da schedulare/verificare.", activeJobs.size());
        int scheduledCount = 0;
        int errorCount = 0;
        for (JobInfo jobInfo : activeJobs) {
            try {
                scheduleOrUpdateJobInternal(jobInfo);
                scheduledCount++;
            } catch (Exception e) {
                errorCount++;
                log.error("Errore durante la schedulazione iniziale del job {}/{}: {}",
                        jobInfo.getJobGroup(), jobInfo.getJobName(), e.getMessage(), e);
            }
        }
        log.info("Inizializzazione Job Quartz completata. Schedulati/Verificati: {}, Errori: {}", scheduledCount, errorCount);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void scheduleNewJob(String jobName, String jobGroup) {
        log.info("Tentativo di schedulare un nuovo job: {}/{}", jobGroup, jobName);
        JobInfo jobInfo = findJobInfoOrThrow(jobName, jobGroup);

        // Policy: Schedula solo se esplicitamente attivo nel DB
        if (!Boolean.TRUE.equals(jobInfo.getIsActive())) {
            log.warn("Il job {}/{} è marcato come inattivo nel DB. Non verrà schedulato. Aggiornare 'isActive' a true e riprovare se necessario.", jobGroup, jobName);
            // Potresti lanciare un'eccezione qui se preferisci un fallimento esplicito
            // throw new IllegalStateException("Il job " + jobGroup + "/" + jobName + " è inattivo e non può essere schedulato.");
            return; // Esce se non vuoi attivarlo automaticamente o lanciare errore
        }

        try {
            scheduleOrUpdateJobInternal(jobInfo);
            log.info("Job {}/{} schedulato con successo.", jobGroup, jobName);
        } catch (Exception e) {
            log.error("Errore durante la schedulazione del job {}/{}: {}", jobGroup, jobName, e.getMessage(), e);
            // Rilancia un'eccezione specifica per indicare il fallimento della schedulazione
            // throw new JobSchedulingException("Errore schedulazione job " + jobGroup + "/" + jobName, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void updateScheduleJob(String jobName, String jobGroup) {
        log.info("Tentativo di aggiornare la schedulazione per il job: {}/{}", jobGroup, jobName);
        JobInfo jobInfo = findJobInfoOrThrow(jobName, jobGroup);

        // Sincronizza lo stato dello scheduler con lo stato 'isActive' del DB
        if (Boolean.TRUE.equals(jobInfo.getIsActive())) {
            // Se attivo nel DB, assicurati che sia schedulato/aggiornato nello scheduler
            log.info("Job {}/{} è attivo nel DB. Schedulazione/aggiornamento nello scheduler...", jobGroup, jobName);
            try {
                scheduleOrUpdateJobInternal(jobInfo);
                log.info("Schedulazione job {}/{} aggiornata con successo.", jobGroup, jobName);
            } catch (Exception e) {
                log.error("Errore durante l'aggiornamento della schedulazione del job {}/{}: {}", jobGroup, jobName, e.getMessage(), e);
                // throw new JobSchedulingException("Errore aggiornamento schedulazione job " + jobGroup + "/" + jobName, e);
            }
        } else {
            // Se inattivo nel DB, assicurati che sia rimosso dallo scheduler
            log.info("Job {}/{} è inattivo nel DB. Rimozione dallo scheduler (se esistente)...", jobGroup, jobName);
            deleteJobInternal(jobInfo.getJobName(), jobInfo.getJobGroup());
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void unScheduleJob(String jobName, String jobGroup) {
        log.info("Tentativo di de-schedulare (rimuovere e marcare come inattivo) il job: {}/{}", jobGroup, jobName);

        // 1. Aggiorna lo stato nel DB a inattivo (se trovato)
        Optional<JobInfo> jobInfoOpt = jobInfoRepository.findByJobNameAndJobGroup(jobName, jobGroup);
        if (jobInfoOpt.isPresent()) {
            JobInfo jobInfo = jobInfoOpt.get();
            if (Boolean.TRUE.equals(jobInfo.getIsActive())) {
                jobInfo.setIsActive(false);
                jobInfoRepository.save(jobInfo);
                log.info("JobInfo {}/{} marcato come inattivo nel database.", jobGroup, jobName);
            } else {
                log.info("JobInfo {}/{} era già marcato come inattivo nel database.", jobGroup, jobName);
            }
        } else {
            log.warn("JobInfo non trovato per {}/{}. Impossibile aggiornare lo stato isActive nel DB.", jobGroup, jobName);
        }

        // 2. Rimuovi dallo scheduler (indipendentemente dallo stato trovato nel DB, per pulizia)
        deleteJobInternal(jobName, jobGroup);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void deleteJob(String jobName, String jobGroup) {
        log.info("Tentativo di cancellare completamente il job: {}/{}", jobGroup, jobName);

        // 1. Rimuovi dallo scheduler
        deleteJobInternal(jobName, jobGroup);

        // 2. Rimuovi dal database
        Optional<JobInfo> jobInfoOpt = jobInfoRepository.findByJobNameAndJobGroup(jobName, jobGroup);
        if (jobInfoOpt.isPresent()) {
            jobInfoRepository.delete(jobInfoOpt.get());
            log.info("JobInfo {}/{} cancellato dal database.", jobGroup, jobName);
        } else {
            log.warn("JobInfo {}/{} non trovato nel database per la cancellazione.", jobGroup, jobName);
        }
    }

    @Override
    // Potrebbe non necessitare di @Transactional se non modifica JobInfo
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void pauseJob(String jobName, String jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        log.info("Tentativo di mettere in pausa il job: {}", jobKey);
        try {
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseJob(jobKey);
                log.info("Job {} messo in pausa con successo.", jobKey);
                // Nota: Non modificare jobInfo.isActive. La pausa è uno stato di runtime dello scheduler.
            } else {
                log.warn("Tentativo di mettere in pausa un job non esistente nello scheduler: {}", jobKey);
            }
        } catch (SchedulerException e) {
            log.error("Errore durante la pausa del job {}: {}", jobKey, e.getMessage(), e);
            // throw new JobControlException("Errore pausa job " + jobKey, e);
        }
    }

    @Override
    // Potrebbe non necessitare di @Transactional se non modifica JobInfo, ma legge per verifica.
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void resumeJob(String jobName, String jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        log.info("Tentativo di riprendere il job: {}", jobKey);
        try {
            // Verifica cruciale: il job deve esistere nel DB ed essere considerato ATTIVO logicamente
            // per poter essere ripreso. Non riprendere job che dovrebbero essere inattivi.
            Optional<JobInfo> jobInfoOpt = jobInfoRepository.findByJobNameAndJobGroup(jobName, jobGroup);
            if (jobInfoOpt.isEmpty() || !Boolean.TRUE.equals(jobInfoOpt.get().getIsActive())) {
                log.warn("Impossibile riprendere il job {}: non trovato nel DB o marcato come inattivo.", jobKey);
                // throw new IllegalStateException("Cannot resume job " + jobKey + " as it is not found or inactive in the database.");
                return;
            }

            // Se è logicamente attivo, prova a riprenderlo nello scheduler
            if (scheduler.checkExists(jobKey)) {
                scheduler.resumeJob(jobKey);
                log.info("Job {} ripreso con successo.", jobKey);
                // Nota: Non modificare jobInfo.isActive.
            } else {
                log.warn("Tentativo di riprendere un job non esistente nello scheduler: {}. Potrebbe essere necessario rischedularlo se si desidera eseguirlo.", jobKey);
                // Considera se rischedularlo automaticamente qui chiamando scheduleOrUpdateJobInternal(jobInfoOpt.get());
            }
        } catch (SchedulerException e) {
            log.error("Errore Scheduler durante la ripresa del job {}: {}", jobKey, e.getMessage(), e);
            // throw new JobControlException("Errore ripresa job " + jobKey, e);
        } catch (Exception e) { // Catch per altre eccezioni (es. accesso DB)
            log.error("Errore generico durante la ripresa del job {}: {}", jobKey, e.getMessage(), e);
            // throw new RuntimeException("Errore generico ripresa job " + jobKey, e);
        }
    }

    @Override
    // Potrebbe non necessitare di @Transactional se non modifica JobInfo, ma legge per verifica.
    public void startJobNow(String jobName, String jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        log.info("Tentativo di avviare immediatamente (trigger) il job: {}", jobKey);
        try {
            // Verifica: Il job deve esistere nel DB ed essere ATTIVO per essere triggerato manualmente.
            Optional<JobInfo> jobInfoOpt = jobInfoRepository.findByJobNameAndJobGroup(jobName, jobGroup);
            if (jobInfoOpt.isEmpty() || !Boolean.TRUE.equals(jobInfoOpt.get().getIsActive())) {
                log.warn("Impossibile avviare il job {}: non trovato nel DB o marcato come inattivo.", jobKey);
                // throw new IllegalStateException("Cannot trigger job " + jobKey + " as it is not found or inactive in the database.");
                return;
            }

            // Verifica se esiste nello scheduler (potrebbe essere stato de-schedulato)
            if (scheduler.checkExists(jobKey)) {
                scheduler.triggerJob(jobKey);
                log.info("Job {} avviato manualmente (triggerato) con successo.", jobKey);
            } else {
                log.warn("Tentativo di avviare manualmente un job non esistente nello scheduler: {}. Schedularlo prima se necessario.", jobKey);
                // Non puoi triggerare un job che non è nello scheduler.
            }
        } catch (SchedulerException e) {
            log.error("Errore Scheduler durante l'avvio manuale del job {}: {}", jobKey, e.getMessage(), e);
            // throw new JobControlException("Errore avvio manuale job " + jobKey, e);
        } catch (Exception e) { // Catch per altre eccezioni (es. accesso DB)
            log.error("Errore generico durante l'avvio manuale del job {}: {}", jobKey, e.getMessage(), e);
            // throw new RuntimeException("Errore generico avvio manuale job " + jobKey, e);
        }
    }

    // --- Metodi Interni Helper ---

    /**
     * Trova JobInfo per nome e gruppo o lancia un'eccezione (esempio).
     *
     * @return Il JobInfo trovato.
     * @throws RuntimeException // o una tua eccezione custom tipo JobNotFoundException
     */
    private JobInfo findJobInfoOrThrow(String jobName, String jobGroup) {
        return jobInfoRepository.findByJobNameAndJobGroup(jobName, jobGroup)
                .orElseThrow(() -> {
                    log.error("JobInfo non trovato per {}/{}", jobGroup, jobName);
                    // Sostituisci con la tua eccezione custom se preferisci
                    return new RuntimeException("JobInfo non trovato per " + jobGroup + "/" + jobName);
                    // return new JobNotFoundException("JobInfo non trovato per " + jobGroup + "/" + jobName);
                });
    }


    /**
     * Logica interna per schedulare o aggiornare un job nello scheduler Quartz.
     * Usa la strategia delete-then-recreate per semplicità e per applicare tutte le modifiche.
     * Questa logica presume che JobInfo contenga la configurazione desiderata *attuale*.
     */
    private void scheduleOrUpdateJobInternal(JobInfo jobInfo) throws SchedulerException, ClassNotFoundException {
        JobKey jobKey = JobKey.jobKey(jobInfo.getJobName(), jobInfo.getJobGroup());
        log.debug("Preparazione schedulazione/aggiornamento per job {}", jobKey);

        JobDetail jobDetail = buildJobDetail(jobInfo, jobKey);
        Trigger trigger = buildTrigger(jobInfo, jobKey);

        boolean jobExists = scheduler.checkExists(jobKey);

        if (jobExists) {
            // Se il job esiste, lo rimuoviamo prima di rischedularlo.
            // Questo assicura che vengano applicate modifiche sia al JobDetail (es. jobDataMap)
            // sia al Trigger (es. cron expression, intervallo).
            log.info("Job {} esiste già nello scheduler. Verrà rimosso e ricreato per applicare la configurazione corrente.", jobKey);
            // La rimozione del job rimuove anche tutti i trigger associati.
            boolean deleted = scheduler.deleteJob(jobKey);
            if (!deleted) {
                // Questo non dovrebbe accadere se checkExists era true, ma logghiamo per sicurezza.
                log.warn("Non è stato possibile rimuovere il job {} esistente prima di aggiornarlo.", jobKey);
                // Potresti voler lanciare un errore qui, perché l'aggiornamento potrebbe fallire o essere incoerente.
                // throw new JobSchedulingException("Impossibile rimuovere job esistente " + jobKey);
            }
        } else {
            log.info("Schedulazione nuovo job: {}", jobKey);
        }

        // Schedula il job con il (nuovo) dettaglio e trigger.
        // Quartz sovrascriverà JobDetail se storeDurably(true) è stato usato e il job esiste già,
        // ma la strategia delete/recreate è più esplicita per l'aggiornamento del trigger.
        Date scheduledTime = scheduler.scheduleJob(jobDetail, trigger);
        log.info("Job {} schedulato/aggiornato nello scheduler. Prossima esecuzione (stimata): {}", jobKey, scheduledTime);
    }

    /**
     * Logica interna e sicura per rimuovere un job dallo scheduler Quartz.
     * Non lancia eccezioni SchedulerException ma le logga.
     */
    private void deleteJobInternal(String jobName, String jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            if (scheduler.checkExists(jobKey)) {
                boolean deleted = scheduler.deleteJob(jobKey);
                if (deleted) {
                    log.info("Job {} cancellato con successo dallo scheduler.", jobKey);
                } else {
                    // Se checkExists è true, deleteJob dovrebbe ritornare true. Log anomalia.
                    log.warn("Il job {} è stato trovato nello scheduler ma deleteJob() ha restituito false.", jobKey);
                }
            } else {
                // È normale tentare di cancellare un job che potrebbe non essere (più) nello scheduler.
                log.info("Tentativo di cancellare un job non esistente nello scheduler: {}. Nessuna azione necessaria.", jobKey);
            }
        } catch (SchedulerException e) {
            // Logga l'errore ma non interrompere il flusso (es. durante un unSchedule o delete)
            log.error("Errore durante la cancellazione del job {} dallo scheduler: {}", jobKey, e.getMessage(), e);
            // Considera se rilanciare un'eccezione personalizzata qui se la cancellazione è critica
            // throw new JobControlException("Errore cancellazione job " + jobKey, e);
        }
    }

    private JobDetail buildJobDetail(JobInfo jobInfo, JobKey jobKey) throws ClassNotFoundException {
        JobDataMap jobDataMap = new JobDataMap();

        if (jobInfo.getJobDataMapJson() != null && !jobInfo.getJobDataMapJson().isBlank()) {
            try {
                Map<String, Object> data = objectMapper.readValue(jobInfo.getJobDataMapJson(), new TypeReference<>() {
                });
                jobDataMap.putAll(data);
                log.debug("JobDataMap popolata per {} da JSON: {}", jobKey, data);
            } catch (Exception e) {
                log.error("ERRORE nel parsing del JSON JobDataMap per {}: {}. JobDataMap potrebbe essere incompleta o vuota. JSON: '{}'",
                        jobKey, e.getMessage(), jobInfo.getJobDataMapJson(), e);
            }
        } else {
            log.debug("Nessun JobDataMap JSON fornito per {}", jobKey);
        }

        jobDataMap.put("jobName", jobInfo.getJobName());
        jobDataMap.put("jobGroup", jobInfo.getJobGroup());

        Class<? extends Job> jobClass;
        try {
            @SuppressWarnings("unchecked")
            Class<? extends Job> loadedClass = (Class<? extends Job>) Class.forName(jobInfo.getJobClass());
            jobClass = loadedClass;
        } catch (ClassNotFoundException e) {
            log.error("Classe Job non trovata: '{}' per job {}", jobInfo.getJobClass(), jobKey);
            throw e;
        }

        log.debug("Costruzione JobDetail per {} con classe {}", jobKey, jobClass.getName());
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobKey)
                .withDescription(jobInfo.getDescription())
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }


    /**
     * Costruisce l'oggetto Trigger (CronTrigger o SimpleTrigger) basandosi su JobInfo.
     */
    private Trigger buildTrigger(JobInfo jobInfo, JobKey jobKey) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobInfo.getJobName() + "-trigger", jobInfo.getJobGroup()); // Convenzione nome trigger
        String triggerDescription = "Trigger per " + jobKey.toString();

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withDescription(triggerDescription)
                .forJob(jobKey);

        if (Boolean.TRUE.equals(jobInfo.getCronJob())) {
            if (jobInfo.getCronExpression() == null || jobInfo.getCronExpression().isBlank()) {
                log.error("Espressione CRON mancante per il job CRON {}", jobKey);
                throw new IllegalArgumentException("Espressione CRON mancante per il job " + jobKey);
            }
            if (!CronExpression.isValidExpression(jobInfo.getCronExpression())) {
                log.error("Espressione CRON non valida ('{}') per il job CRON {}", jobInfo.getCronExpression(), jobKey);
                throw new IllegalArgumentException("Espressione CRON non valida: " + jobInfo.getCronExpression());
            }

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression())
                    // Politica Misfire: Cosa fare se lo scheduler "perde" un'esecuzione (es. perché era spento)
                    // - MISFIRE_INSTRUCTION_FIRE_ONCE_NOW: Esegui immediatamente una volta e riprendi la schedulazione normale. (Comune)
                    // - MISFIRE_INSTRUCTION_DO_NOTHING: Ignora l'esecuzione persa e aspetta la prossima schedulata.
                    // - MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY: Esegui tutte le esecuzioni perse il prima possibile. (Rischioso)
                    .withMisfireHandlingInstructionFireAndProceed(); // Esegui immediatamente e riprendi normalmente (simile a FIRE_ONCE_NOW)
            // .withMisfireHandlingInstructionDoNothing();

            log.debug("Costruzione CronTrigger per {} con espressione '{}' e misfire policy '{}'",
                    jobKey, jobInfo.getCronExpression(), "FireAndProceed");

            triggerBuilder.withSchedule(scheduleBuilder);
            // Per CronTrigger, startAt definisce *da quando* l'espressione è valida, non un delay.
            // Di solito non serve se si vuole che parta alla prossima occorrenza valida.
            // triggerBuilder.startNow(); // Non strettamente necessario per Cron

        } else {
            // --- Costruzione SimpleTrigger ---
            if (jobInfo.getRepeatIntervalMillis() == null || jobInfo.getRepeatIntervalMillis() <= 0) {
                log.error("L'intervallo di ripetizione è nullo o non valido ({}) per il Simple job {}", jobInfo.getRepeatIntervalMillis(), jobKey);
                throw new IllegalArgumentException("Intervallo di ripetizione mancante o non valido per il job " + jobKey);
            }

            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMilliseconds(jobInfo.getRepeatIntervalMillis());

            // Gestione Repeat Count:
            // Quartz: repeat count è il numero di ripetizioni *dopo* la prima.
            // REPEAT_INDEFINITELY = -1. Esegue per sempre.
            // 0 = Esegue solo la prima volta, 0 ripetizioni aggiuntive. (Totale 1 esecuzione)
            // N = Esegue la prima volta + N ripetizioni aggiuntive. (Totale N+1 esecuzioni)
            Integer repeatCountConfig = jobInfo.getRepeatCount(); // Potrebbe essere null

            if (repeatCountConfig == null || repeatCountConfig < 0) { // Considera null come infinito
                scheduleBuilder.repeatForever();
                log.debug("SimpleTrigger per {} ripeterà all'infinito (intervallo {} ms).", jobKey, jobInfo.getRepeatIntervalMillis());
            } else if (repeatCountConfig == 0) {
                // Questo è ambiguo: l'utente intende 0 esecuzioni totali (impossibile) o 1 esecuzione totale?
                // Interpretiamo come 1 esecuzione totale (0 ripetizioni).
                scheduleBuilder.withRepeatCount(0);
                log.debug("SimpleTrigger per {} eseguirà una sola volta (repeat count config 0, Quartz repeat count 0).", jobKey);
            } else {
                // Se jobInfo.getRepeatCount() è il *numero totale* di esecuzioni desiderate (>= 1)
                scheduleBuilder.withRepeatCount(repeatCountConfig - 1);
                log.debug("SimpleTrigger per {} eseguirà un totale di {} volte (repeat count config {}, Quartz repeat count {}).",
                        jobKey, repeatCountConfig, repeatCountConfig, repeatCountConfig - 1);
            }

            // Politica Misfire per SimpleTrigger:
            // - MISFIRE_INSTRUCTION_FIRE_NOW: Esegui immediatamente. (Comune)
            // - MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT: Esegui ora, mantieni conteggio ripetizioni.
            // - MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT: Salta, aspetta prossima schedulata, mantieni conteggio rimanente.
            scheduleBuilder.withMisfireHandlingInstructionFireNow();
            // .withMisfireHandlingInstructionRescheduleNowWithExistingRepeatCount();
            log.debug("SimpleTrigger per {} userà misfire policy '{}'", jobKey, "FireNow");


            triggerBuilder.withSchedule(scheduleBuilder);

            // Gestione Ritardo Iniziale
            if (jobInfo.getInitialDelayMillis() != null && jobInfo.getInitialDelayMillis() > 0) {
                Date startTime = new Date(System.currentTimeMillis() + jobInfo.getInitialDelayMillis());
                triggerBuilder.startAt(startTime);
                log.debug("Trigger per {} avrà un ritardo iniziale, partenza schedulata alle {}", jobKey, startTime);
            } else {
                triggerBuilder.startNow();
                log.debug("Trigger per {} partirà immediatamente (nessun ritardo iniziale).", jobKey);
            }
        }

        return triggerBuilder.build();
    }
}