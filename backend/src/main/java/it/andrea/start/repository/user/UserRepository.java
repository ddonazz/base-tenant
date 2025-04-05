package it.andrea.start.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import it.andrea.start.models.user.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Override
    public Optional<User> findById(Long id);

    public Optional<User> findByUsername(String username);

    public Optional<User> findByEmail(String email);

    public List<User> findAll();

    @Query(value = "SELECT u FROM User u INNER JOIN u.roles r WHERE r.role <> 'ADMIN'")
    public List<User> findAllNotAdmin();

}
