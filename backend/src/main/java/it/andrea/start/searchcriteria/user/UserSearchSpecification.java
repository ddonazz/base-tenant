package it.andrea.start.searchcriteria.user;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import it.andrea.start.models.user.User;
import it.andrea.start.models.user.UserRole;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class UserSearchSpecification implements Specification<User> {

    @Serial
    private static final long serialVersionUID = -1987604702637357646L;

    private final UserSearchCriteria criteria;

    public UserSearchSpecification(UserSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<User> root, CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getId() != null) {
            predicates.add(cb.equal(root.get("id"), criteria.getId()));
        }

        if (StringUtils.isNotBlank(criteria.getUsername())) {
            predicates.add(cb.equal(root.get("username"), criteria.getUsername()));
        }

        if (StringUtils.isNotBlank(criteria.getTextSearch())) {
            String pattern = "%" + criteria.getTextSearch().toUpperCase() + "%";
            Predicate usernamePredicate = cb.like(cb.upper(root.get("username")), pattern);
            Predicate emailPredicate = cb.like(cb.upper(root.get("email")), pattern);
            Predicate namePredicate = cb.like(cb.upper(root.get("name")), pattern);
            predicates.add(cb.or(usernamePredicate, emailPredicate, namePredicate));
        }

        if (criteria.getUserStatus() != null) {
            predicates.add(cb.equal(root.get("userStatus"), criteria.getUserStatus()));
        }

        boolean hasRoleFilter = criteria.getRoles() != null && !criteria.getRoles().isEmpty();
        boolean hasRoleNotValidFilter = criteria.getRolesNotValid() != null && !criteria.getRolesNotValid().isEmpty();
        if (hasRoleFilter || hasRoleNotValidFilter) {
            Join<User, UserRole> roleJoin = root.join("roles");
            query.distinct(true);

            if (hasRoleFilter) {
                predicates.add(roleJoin.get("role").in(criteria.getRoles()));
            }
            if (hasRoleNotValidFilter) {
                predicates.add(cb.not(roleJoin.get("role").in(criteria.getRolesNotValid())));
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
