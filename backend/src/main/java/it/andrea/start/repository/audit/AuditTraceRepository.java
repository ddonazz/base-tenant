package it.andrea.start.repository.audit;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import it.andrea.start.models.audit.AuditTrace;

public interface AuditTraceRepository extends JpaRepository<AuditTrace, Long>, JpaSpecificationExecutor<AuditTrace> {

    @NonNull
    @Override
    public Optional<AuditTrace> findById(@NonNull Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM AuditTrace at WHERE at.dateEvent < :date")
    public int deleteRows(@Param("date") LocalDateTime date);

}
