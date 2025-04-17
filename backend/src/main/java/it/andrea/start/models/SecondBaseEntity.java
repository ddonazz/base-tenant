package it.andrea.start.models;

import jakarta.persistence.Column;

public abstract class SecondBaseEntity extends FirstBaseEntity {

    @Column(nullable = false)
    private boolean deleted = Boolean.FALSE;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
