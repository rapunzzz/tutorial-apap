package apap.tutorial.manpromanpro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apap.tutorial.manpromanpro.model.Role;

@Repository
public interface RoleDb extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(String role);
}
