package it.andrea.start.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import it.andrea.start.dto.JobInfoDTO;
import it.andrea.start.service.job.JobInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/job")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor // Lombok per constructor injection
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
            @Parameter(description = "Gruppo del job", required = true) @PathVariable("group") String group,
            @Parameter(description = "Nome del job", required = true) @PathVariable("name") String name) {

        jobInfoService.scheduleNewJob(name, group);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Aggiorna la schedulazione di un job",
            description = "Rilegge la configurazione del job dal database e aggiorna lo scheduler Quartz. Se il job è inattivo nel DB, viene rimosso dallo scheduler."
    )
    @PutMapping("/update/{group}/{name}")
    public ResponseEntity<Void> updateScheduleJob(
            @Parameter(description = "Gruppo del job", required = true) @PathVariable("group") String group,
            @Parameter(description = "Nome del job", required = true) @PathVariable("name") String name) {

        jobInfoService.updateScheduleJob(name, group);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Disattiva e de-schedula un job",
            description = "Rimuove il job dallo scheduler Quartz e lo marca come inattivo (isActive=false) nel database."
    )
    @PutMapping("/unschedule/{group}/{name}")
    public ResponseEntity<Void> unScheduleJob(
            @Parameter(description = "Gruppo del job", required = true) @PathVariable("group") String group,
            @Parameter(description = "Nome del job", required = true) @PathVariable("name") String name) {

        jobInfoService.unScheduleJob(name, group);
        return ResponseEntity.ok().build();

    }

    @Operation(
            summary = "Elimina un job",
            description = "Rimuove il job dallo scheduler Quartz e cancella la sua definizione dal database."
    )
    @DeleteMapping("/delete/{group}/{name}")
    public ResponseEntity<Void> deleteJob(
            @Parameter(description = "Gruppo del job", required = true) @PathVariable("group") String group,
            @Parameter(description = "Nome del job", required = true) @PathVariable("name") String name) {

        jobInfoService.deleteJob(name, group);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Metti in pausa un job",
            description = "Sospende temporaneamente l'esecuzione del job nello scheduler Quartz."
    )
    @PutMapping("/pause/{group}/{name}")
    public ResponseEntity<Void> pauseJob(
            @Parameter(description = "Gruppo del job", required = true) @PathVariable("group") String group,
            @Parameter(description = "Nome del job", required = true) @PathVariable("name") String name) {

        jobInfoService.pauseJob(name, group);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Riprendi un job",
            description = "Riattiva l'esecuzione di un job precedentemente messo in pausa nello scheduler Quartz."
    )
    @PutMapping("/resume/{group}/{name}")
    public ResponseEntity<Void> resumeJob(
            @Parameter(description = "Gruppo del job", required = true) @PathVariable("group") String group,
            @Parameter(description = "Nome del job", required = true) @PathVariable("name") String name) {

        jobInfoService.resumeJob(name, group);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Avvia un job immediatamente",
            description = "Forza l'esecuzione una tantum di un job schedulato, indipendentemente dal suo trigger."
    )
    @PutMapping("/start/{group}/{name}")
    public ResponseEntity<Void> startJobNow(
            @Parameter(description = "Gruppo del job", required = true) @PathVariable("group") String group,
            @Parameter(description = "Nome del job", required = true) @PathVariable("name") String name) {
        jobInfoService.startJobNow(name, group);
        return ResponseEntity.ok().build();

    }
}