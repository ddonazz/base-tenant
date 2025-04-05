package it.andrea.start.mappers.user;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import it.andrea.start.constants.RoleType;
import it.andrea.start.dto.user.UserDTO;
import it.andrea.start.exception.MappingToDtoException;
import it.andrea.start.exception.MappingToEntityException;
import it.andrea.start.mappers.AbstractMapper;
import it.andrea.start.models.user.User;
import it.andrea.start.models.user.UserRole;
import it.andrea.start.repository.user.UserRoleRepository;
import jakarta.persistence.EntityManager;

@Service
public class UserMapper extends AbstractMapper<UserDTO, User> {
    
    private final UserRoleRepository userRoleRepository;

    public UserMapper(final EntityManager entityManager, final UserRoleRepository userRoleRepository) {
	super(entityManager);
	this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDTO toDto(User entity) throws MappingToDtoException {
	UserDTO dto = new UserDTO();
	dto.setId(entity.getId());
	dto.setUsername(entity.getUsername());
	dto.setName(entity.getName());
	dto.setEmail(entity.getEmail());
	dto.setUserStatus(entity.getUserStatus());
	dto.setRoles(Set.copyOf(entity.getRoles().stream().map(userRole -> userRole.getRole().name()).toList()));
	dto.setLanguageDefault(entity.getLanguageDefault());

	return dto;
    }

    @Override
    public void toEntity(UserDTO dto, User entity) throws MappingToEntityException {
	entity.setId(dto.getId());
	entity.setUsername(dto.getUsername().toUpperCase());
	entity.setName(dto.getName());
	entity.setEmail(dto.getEmail());
	entity.setUserStatus(dto.getUserStatus());
	entity.setLanguageDefault(dto.getLanguageDefault());

	Set<String> roles = dto.getRoles();
	Set<UserRole> userRoles = new HashSet<>();
	if (roles != null && !roles.isEmpty()) {
	    for (String role : roles) {
		Optional<UserRole> userRoleOpt = userRoleRepository.findByRole(RoleType.valueOf(role));
		if (userRoleOpt.isEmpty()) {
		    throw new MappingToEntityException();
		}
		userRoles.add(userRoleOpt.get());
	    }
	}
	entity.setRoles(userRoles);
    }

}
