package it.andrea.start.models.property;

import it.andrea.start.models.BaseEntityLong;
import it.andrea.start.models.SecondBaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "property")
public class Property extends SecondBaseEntity implements BaseEntityLong {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROPERTY_SEQ")
    @SequenceGenerator(name = "PROPERTY_SEQ", sequenceName = "PROPERTY_SEQUENCE", allocationSize = 1)
    private Long id;

    @Column
    private String denomination;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

}
