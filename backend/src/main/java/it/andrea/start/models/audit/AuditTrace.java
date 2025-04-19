package it.andrea.start.models.audit;

import java.time.LocalDateTime;

import it.andrea.start.constants.AuditActivity;
import it.andrea.start.constants.AuditTypeOperation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(
    name = "audit_trace",
    indexes = {
            @Index(
                name = "IDX_AUDIT_USER_NAME",
                columnList = "username"
            ),
            @Index(
                name = "IDX_AUDIT_ACTIVITY",
                columnList = "activity"
            ),
            @Index(
                name = "IDX_AUDIT_TYPE",
                columnList = "auditType"
            ),
            @Index(
                name = "IDX_AUDIT_DATE_EVENT",
                columnList = "dateEvent"
            ),
    }
)
public class AuditTrace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditActivity activity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditTypeOperation auditType;

    @Column(nullable = false)
    private LocalDateTime dateEvent;

    @Column()
    private String username;

    @Column()
    private String className;

    @Column()
    private String methodName;

    @Column()
    private String controllerMethod;

    @Column()
    private String httpMethod;

    @Column()
    private String requestUri;

    @Column()
    private String clientIpAddress;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String requestParams;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String requestBody;

    @Column()
    private String userAgent;

    @Column(nullable = false)
    private Boolean success;

    @Column()
    private Long durationMs;

    @Column()
    private String exceptionType;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String exceptionMessage;

    public AuditTrace() {
        this.dateEvent = LocalDateTime.now();
    }

}
