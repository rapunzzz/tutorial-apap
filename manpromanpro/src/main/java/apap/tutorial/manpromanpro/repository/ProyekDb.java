package apap.tutorial.manpromanpro.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import apap.tutorial.manpromanpro.model.Proyek;

@Repository
public interface ProyekDb extends JpaRepository<Proyek, UUID> {
    List<Proyek> findByDeletedAtIsNull(Sort sort);
    List<Proyek> findByNamaContainingIgnoreCaseAndDeletedAtIsNull(String nama, Sort sort);
    List<Proyek> findByStatusIgnoreCaseAndDeletedAtIsNull(String status, Sort sort);
    List<Proyek> findByNamaContainingIgnoreCaseAndStatusIgnoreCaseAndDeletedAtIsNull(String nama, String status, Sort sort);
}
