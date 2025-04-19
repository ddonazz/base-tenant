package it.andrea.start.repository.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import it.andrea.start.constants.RoleType;
import it.andrea.start.models.user.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole> {

    Optional<UserRole> findByRole(RoleType roleType);

    boolean existsByRole(RoleType roleType);

    List<UserRole> findByRoleIn(Set<RoleType> roleTypes);

}
