package it.andrea.start.mappers.user;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import it.andrea.start.constants.RoleType;
import it.andrea.start.dto.user.UserDTO;
import it.andrea.start.error.exception.mapping.MappingToDtoException;
import it.andrea.start.error.exception.mapping.MappingToEntityException;
import it.andrea.start.error.exception.user.UserRoleNotFoundException;
import it.andrea.start.mappers.AbstractMapper;
import it.andrea.start.models.user.User;
import it.andrea.start.models.user.UserRole;
import it.andrea.start.repository.user.UserRoleRepository;
import jakarta.persistence.EntityManager;

@Component
public class UserMapper extends AbstractMapper<UserDTO, User> {

    private final UserRoleRepository userRoleRepository;

    public UserMapper(final EntityManager entityManager, final UserRoleRepository userRoleRepository) {
        super(entityManager);
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDTO toDto(User entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setUserStatus(entity.getUserStatus());
        Set<UserRole> rolesEntity = entity.getRoles();
        if (CollectionUtils.isEmpty(rolesEntity)) {
            dto.setRoles(Collections.emptySet());
        } else {
            try {
                Set<String> rolesDto = rolesEntity.stream()
                        .filter(Objects::nonNull)
                        .map(userRole -> Objects.requireNonNull(userRole.getRole(), "UserRole contains null Role enum").name())
                        .collect(Collectors.toUnmodifiableSet());
                dto.setRoles(rolesDto);
            } catch (NullPointerException e) {
                throw new MappingToDtoException("Error mapping roles for user: " + entity.getId() + ". Null value encountered in roles collection.", e);
            }
        }
        dto.setLanguageDefault(entity.getLanguageDefault());

        return dto;
    }

    @Override
    public void toEntity(UserDTO dto, User entity) {
        Objects.requireNonNull(dto, "Input DTO cannot be null for mapping to Entity");
        Objects.requireNonNull(entity, "Input entity cannot be null for mapping to Entity");

        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername() != null ? dto.getUsername().toUpperCase() : null);
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setUserStatus(dto.getUserStatus());
        entity.setLanguageDefault(dto.getLanguageDefault());

        Set<String> rolesDtoNames = dto.getRoles();
        if (CollectionUtils.isEmpty(rolesDtoNames)) {
            entity.setRoles(Collections.emptySet());
        } else {
            Set<RoleType> roleTypesToFind = new HashSet<>();
            for (String roleName : rolesDtoNames) {
                try {
                    roleTypesToFind.add(RoleType.valueOf(roleName));
                } catch (IllegalArgumentException e) {
                    throw new UserRoleNotFoundException(roleName);
                }
            }

            List<UserRole> foundUserRoles = userRoleRepository.findByRoleIn(roleTypesToFind);

            if (foundUserRoles.size() != roleTypesToFind.size()) {
                Set<RoleType> foundTypes = foundUserRoles
                        .stream()
                        .map(UserRole::getRole)
                        .collect(Collectors.toSet());
                roleTypesToFind.removeAll(foundTypes);
                throw new MappingToEntityException("The following roles specified in the DTO were not found in the database: " + roleTypesToFind);
            }

            entity.setRoles(new HashSet<>(foundUserRoles));
        }
    }

}
