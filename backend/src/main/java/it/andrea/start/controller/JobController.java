package it.andrea.start.controller;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.andrea.start.dto.JobInfoDTO;
import it.andrea.start.service.job.JobInfoService;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Job Info API",
    description = "API for job scheduler management"
)
@RestController
@RequestMapping("/api/job")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class JobController {

    private final JobInfoService jobInfoService;

    @Operation(
        summary = "Lista tutte le definizioni dei job",
        description = "Recupera un elenco di tutte le configurazioni dei job memorizzate nel sistema."
    )
    @GetMapping("/list")
    public ResponseEntity<Collection<JobInfoDTO>> listJobs() {
        return ResponseEntity.ok(jobInfoService.listJobs());
    }

    @Operation(
        summary = "Pianifica un nuovo job",
        description = "Attiva e schedula un job definito nel database ma non ancora attivo nello scheduler Quartz."
    )
    @PutMapping("/schedule/{group}/{name}")
    public ResponseEntity<Void> scheduleNewJob(
            @PathVariable String group,
            @PathVariable String name) {

        jobInfoService.scheduleNewJob(name, group);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Aggiorna la schedulazione di un job",
        description = "Rilegge la configurazione del job dal database e aggiorna lo scheduler Quartz. Se il job è inattivo nel DB, viene rimosso dallo scheduler."
    )
    @PutMapping("/update/{group}/{name}")
    public ResponseEntity<Void> updateScheduleJob(
            @PathVariable String group,
            @PathVariable String name) {

        jobInfoService.updateScheduleJob(name, group);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Disattiva e de-schedula un job",
        description = "Rimuove il job dallo scheduler Quartz e lo marca come inattivo (isActive=false) nel database."
    )
    @PutMapping("/unschedule/{group}/{name}")
    public ResponseEntity<Void> unScheduleJob(
            @PathVariable String group,
            @PathVariable String name) {

        jobInfoService.unScheduleJob(name, group);
        return ResponseEntity.ok().build();

    }

    @Operation(
        summary = "Elimina un job",
        description = "Rimuove il job dallo scheduler Quartz e cancella la sua definizione dal database."
    )
    @DeleteMapping("/delete/{group}/{name}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable String group,
            @PathVariable String name) {

        jobInfoService.deleteJob(name, group);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Metti in pausa un job",
        description = "Sospende temporaneamente l'esecuzione del job nello scheduler Quartz."
    )
    @PutMapping("/pause/{group}/{name}")
    public ResponseEntity<Void> pauseJob(
            @PathVariable String group,
            @PathVariable String name) {

        jobInfoService.pauseJob(name, group);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Riprendi un job",
        description = "Riattiva l'esecuzione di un job precedentemente messo in pausa nello scheduler Quartz."
    )
    @PutMapping("/resume/{group}/{name}")
    public ResponseEntity<Void> resumeJob(
            @PathVariable String group,
            @PathVariable String name) {

        jobInfoService.resumeJob(name, group);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Avvia un job immediatamente",
        description = "Forza l'esecuzione una tantum di un job schedulato, indipendentemente dal suo trigger."
    )
    @PutMapping("/start/{group}/{name}")
    public ResponseEntity<Void> startJobNow(
            @PathVariable String group,
            @PathVariable String name) {
        jobInfoService.startJobNow(name, group);

        return ResponseEntity.ok().build();
    }
}