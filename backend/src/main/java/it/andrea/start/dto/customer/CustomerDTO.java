package it.andrea.start.dto.customer;

import java.io.Serializable;
import java.util.Date;

import it.andrea.start.constants.CustomerStatus;
import it.andrea.start.constants.DocumentType;
import it.andrea.start.constants.Gender;
import it.andrea.start.constants.TypeRegistry;

public class CustomerDTO implements Serializable {
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

    public String getStreet() {
	return street;
    }

    public void setStreet(String street) {
	this.street = street;
    }

    public String getNumber() {
	return number;
    }

    public void setNumber(String number) {
	this.number = number;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getZipCode() {
	return zipCode;
    }

    public void setZipCode(String zipCode) {
	this.zipCode = zipCode;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public String getStateOrProvince() {
	return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
	this.stateOrProvince = stateOrProvince;
    }

    public Boolean getDeleted() {
	return deleted;
    }

    public void setDeleted(Boolean deleted) {
	this.deleted = deleted;
    }

}
