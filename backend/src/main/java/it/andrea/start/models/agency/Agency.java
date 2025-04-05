package it.andrea.start.models.agency;

import java.io.Serializable;
import java.util.Collection;

import it.andrea.start.models.BaseEntityLong;
import it.andrea.start.models.FirstBaseEntity;
import it.andrea.start.models.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "agency")
public class Agency extends FirstBaseEntity implements BaseEntityLong, Serializable {
    private static final long serialVersionUID = -5989858160720302345L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AGENCY_SEQ")
    @SequenceGenerator(sequenceName = "agency_seq", initialValue = 1, allocationSize = 1, name = "AGENCY_SEQ")
    private Long id;

    @Column
    private String name;

    @Column
    private String vatNumber;

    @Column
    private String taxCode;

    @Column
    private String phone;

    @Column
    private String email;

    @Column
    private String website;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String postalCode;

    @Column
    private String province;

    @Column
    private String country;

    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @OneToMany(mappedBy = "agency")
    private Collection<User> users;

    @Override
    public Long getId() {
	return this.id;
    }

}
