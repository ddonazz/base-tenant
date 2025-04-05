package it.andrea.start.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import it.andrea.start.models.user.TokenUser;

public interface TokenUserRepository extends JpaRepository<TokenUser, Long>, JpaSpecificationExecutor<TokenUser> {

    @Override
    public Optional<TokenUser> findById(Long id);

}
