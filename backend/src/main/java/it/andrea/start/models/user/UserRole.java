package it.andrea.start.models.user;

import java.io.Serial;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;

import it.andrea.start.constants.RoleType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(
    name = "user_role",
    indexes = { @Index(
        name = "IDX_ROLE",
        columnList = "role"
    ) }
)
public class UserRole implements GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 5841586043417823821L;

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "USER_ROLE_SEQ"
    )
    @SequenceGenerator(
        name = "USER_ROLE_SEQ",
        sequenceName = "USER_ROLE_SEQUENCE",
        allocationSize = 1
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    public UserRole() {
        super();
    }

    public UserRole(RoleType role) {
        super();
        this.role = role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.role.name();
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserRole other = (UserRole) obj;
        return role == other.role;
    }

}