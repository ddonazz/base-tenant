package it.andrea.start.models.customer;

import it.andrea.start.constants.CustomerStatus;
import it.andrea.start.models.Location;
import it.andrea.start.models.Registry;
import it.andrea.start.models.SecondBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;

public class Customer extends SecondBaseEntity {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "CUSTOMER_SEQ"
    )
    @SequenceGenerator(
        name = "CUSTOMER_SEQ",
        sequenceName = "CUSTOMER_SEQUENCE",
        allocationSize = 1
    )
    private Long id;

    @Column(
        nullable = false,
        unique = true
    )
    private String code;

    @Column(
        nullable = false,
        name = "customer_status"
    )
    @Enumerated(EnumType.STRING)
    private CustomerStatus customerStatus;

    @Embedded
    private Registry registry;

    @Embedded
    private Location location;

    @Lob
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CustomerStatus getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(CustomerStatus customerStatus) {
        this.customerStatus = customerStatus;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
