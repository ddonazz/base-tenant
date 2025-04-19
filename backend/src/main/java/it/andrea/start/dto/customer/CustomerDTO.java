package it.andrea.start.dto.customer;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import it.andrea.start.constants.CustomerStatus;
import it.andrea.start.constants.DocumentType;
import it.andrea.start.constants.Gender;
import it.andrea.start.constants.TypeRegistry;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5337726298398860174L;

    private Long id;
    private String code;
    private CustomerStatus customerStatus;
    private TypeRegistry typeRegistry;
    private String name;
    private String surname;
    private String businessName;
    private Gender gender;
    private String email;
    private String telephone;
    private String pec;
    private String vatCode;
    private String fiscalCode;
    private String birthCity;
    private String birthState;
    private Date birthDate;
    private DocumentType documentType;
    private String identityCardNumber;
    private String companyRegistrationNumber;
    private String street;
    private String number;
    private String city;
    private String zipCode;
    private String country;
    private String stateOrProvince;
    private Boolean deleted;

}
