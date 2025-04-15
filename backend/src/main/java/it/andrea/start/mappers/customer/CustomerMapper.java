package it.andrea.start.mappers.customer;

import it.andrea.start.dto.customer.CustomerDTO;
import it.andrea.start.error.exception.mapping.MappingToDtoException;
import it.andrea.start.error.exception.mapping.MappingToEntityException;
import it.andrea.start.mappers.AbstractMapper;
import it.andrea.start.models.customer.Customer;
import jakarta.persistence.EntityManager;

public class CustomerMapper extends AbstractMapper<CustomerDTO, Customer> {

    protected CustomerMapper(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public CustomerDTO toDto(Customer entity) throws MappingToDtoException {
        return null;
    }

    @Override
    public void toEntity(CustomerDTO dto, Customer entity) throws MappingToEntityException {
    }

}
