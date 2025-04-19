package it.andrea.start.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;

@MappedSuperclass
public class FirstBaseEntity {

    @CreatedBy
    @Column(
        nullable = false,
        updatable = false
    )
    private String creator;

    @LastModifiedBy
    @Column(nullable = false)
    private String lastModifiedBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime lastModification;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(
        nullable = false,
        updatable = false
    )
    private LocalDateTime creationDate;

    @Version
    private long version;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastModification() {
        return lastModification;
    }

    public void setLastModification(LocalDateTime lastModification) {
        this.lastModification = lastModification;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @PrePersist
    void createdAt() {
        this.creationDate = LocalDateTime.now();
        this.lastModification = LocalDateTime.now();
    }

    @PreUpdate
    void updatedAt() {
        if (this.creationDate == null)
            this.creationDate = LocalDateTime.now();
        this.lastModification = LocalDateTime.now();
    }

}
