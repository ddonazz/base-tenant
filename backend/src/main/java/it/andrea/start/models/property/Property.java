package it.andrea.start.models.property;

import it.andrea.start.models.SecondBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "property")
public class Property extends SecondBaseEntity {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "PROPERTY_SEQ"
    )
    @SequenceGenerator(
        name = "PROPERTY_SEQ",
        sequenceName = "PROPERTY_SEQUENCE",
        allocationSize = 1
    )
    private Long id;

    @Column
    private String denomination;

    public void setId(Long id) {
        this.id = id;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

}
