package it.andrea.start.models;

import java.util.Date;

import it.andrea.start.constants.DocumentType;
import it.andrea.start.constants.Gender;
import it.andrea.start.constants.TypeRegistry;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Embeddable
public class Registry {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeRegistry typeRegistry;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String surname;

    @Column(nullable = true)
    private String businessName;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String telephone;

    @Column(nullable = true)
    private String pec;

    @Column(nullable = true)
    private String vatCode;

    @Column(nullable = true)
    private String fiscalCode;

    @Column(nullable = true)
    private String birthCity;

    @Column(nullable = true)
    private String birthState;

    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private DocumentType documentType;

    @Column(nullable = true)
    private String identityCardNumber;

    @Column(nullable = true)
    private String companyRegistrationNumber;

    public TypeRegistry getTypeRegistry() {
        return typeRegistry;
    }

    public void setTypeRegistry(TypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPec() {
        return pec;
    }

    public void setPec(String pec) {
        this.pec = pec;
    }

    public String getVatCode() {
        return vatCode;
    }

    public void setVatCode(String vatCode) {
        this.vatCode = vatCode;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    public String getBirthState() {
        return birthState;
    }

    public void setBirthState(String birthState) {
        this.birthState = birthState;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public String getCompanyRegistrationNumber() {
        return companyRegistrationNumber;
    }

    public void setCompanyRegistrationNumber(String companyRegistrationNumber) {
        this.companyRegistrationNumber = companyRegistrationNumber;
    }

}
