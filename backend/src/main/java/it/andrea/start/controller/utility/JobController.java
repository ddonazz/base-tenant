package it.andrea.start.controller.utility;

import java.text.ParseException;
import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.andrea.start.annotation.CustomApiOperation;
import it.andrea.start.dto.JobInfoDTO;
import it.andrea.start.exception.MappingToDtoException;
import it.andrea.start.service.JobInfoService;

@RestController
@RequestMapping("/api/job")
@PreAuthorize("hasRole('ADMIN')")
public class JobController {

    private JobInfoService jobInfoService;

    public JobController(JobInfoService jobInfoService) {
	super();
	this.jobInfoService = jobInfoService;
    }

    @CustomApiOperation(
    	    method = "GET",
    	    description = "Lista dei job definiti",
    	    summary = "Lista dei job definiti"
	    )
    @GetMapping("/list")
    public ResponseEntity<Collection<JobInfoDTO>> listJobs() throws MappingToDtoException  {
	return ResponseEntity.ok(jobInfoService.listJobs());
    }

    @CustomApiOperation(
	    method = "PUT",
	    description = "Pianifica un job non in esecuzione",
	    summary = "Pianifica un job non in esecuzione"
	    )
    @PutMapping("/schedule-new-job/{jobName}")
    public ResponseEntity<Void> scheduleNewJob(@PathVariable String jobName) throws Exception  {
	jobInfoService.scheduleNewJob(jobName);
	
	return ResponseEntity.ok().build();
    }

    @CustomApiOperation(
	    method = "PUT",
	    description = "Update di un job in esecuzione",
	    summary = "Update di un job in esecuzione"
	    )
    @PutMapping("/update-schedule-job/{jobName}")
    public ResponseEntity<Void> updateScheduleJob(@PathVariable String jobName) throws ParseException  {
	jobInfoService.updateScheduleJob(jobName);
	
	return ResponseEntity.ok().build();
    }

    @CustomApiOperation(
	    method = "PUT",
	    description = "Disattiva un job dal programma",
	    summary = "Disattiva un job dal programma"
	)
    @PutMapping("/unschedule-job/{jobName}")
    public ResponseEntity<Void> unScheduleJob(@PathVariable String jobName) {
	jobInfoService.unScheduleJob(jobName);
	
	return ResponseEntity.ok().build();
    }

    @CustomApiOperation(
	    method = "DELETE",
	    description = "Elimina un job dal programma",
	    summary = "Elimina un job dal programma"
	)
    @DeleteMapping("/delete-job/{jobName}")
    public ResponseEntity<Void> deleteJob(@PathVariable String jobName) throws Exception {
	jobInfoService.deleteJob(jobName);
	
	return ResponseEntity.ok().build();
    }

    @CustomApiOperation(
	    method = "PUT",
	    description = "Metti in pausa un job",
	    summary = "Metti in pausa un job"
	)
    @PutMapping("/pause-job/{jobName}")
    public ResponseEntity<Void> pauseJob(@PathVariable String jobName) {
	jobInfoService.pauseJob(jobName);
	
	return ResponseEntity.ok().build();
    }

    @CustomApiOperation(
	    method = "PUT",
	    description = "Riprendi un job messo in pausa",
	    summary = "Riprendi un job messo in pausa"
	)
    @PutMapping("/resumeJob/{jobName}")
    public ResponseEntity<Void> resumeJob(@PathVariable String jobName)  {
	jobInfoService.resumeJob(jobName);
	
	return ResponseEntity.ok().build();
    }

    @CustomApiOperation(
	    method = "PUT",
	    description = "Avvia un job",
	    summary = "Avvia un job"
	    )
    @PutMapping("/startJob/{jobName}")
    public ResponseEntity<Void> startJob(@PathVariable String jobName) {
	jobInfoService.startJobNow(jobName);
	
	return ResponseEntity.ok().build();
    }

}
