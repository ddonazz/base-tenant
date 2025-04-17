package it.andrea.start.repository.user;

import it.andrea.start.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @NonNull
    @Override
    Optional<User> findById(@NonNull Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @NonNull
    List<User> findAll();

}
