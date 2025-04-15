package it.andrea.start.mappers.user;

import org.springframework.stereotype.Component;

import it.andrea.start.dto.user.UserRoleDTO;
import it.andrea.start.error.exception.mapping.MappingToDtoException;
import it.andrea.start.error.exception.mapping.MappingToEntityException;
import it.andrea.start.mappers.AbstractMapper;
import it.andrea.start.models.user.UserRole;
import jakarta.persistence.EntityManager;

@Component
public class UserRoleMapper extends AbstractMapper<UserRoleDTO, UserRole> {

    public UserRoleMapper(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public UserRoleDTO toDto(UserRole entity) throws MappingToDtoException {
        UserRoleDTO dto = new UserRoleDTO();
        dto.setRole(entity.getRole().name());

        return dto;
    }

    @Override
    public void toEntity(UserRoleDTO dto, UserRole entity) throws MappingToEntityException {
        // Non serve implementare perchè non creiamo i ruoli a runtime
        throw new UnsupportedOperationException();
    }

}
