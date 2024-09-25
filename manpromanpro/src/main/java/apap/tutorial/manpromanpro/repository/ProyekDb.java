package apap.tutorial.manpromanpro.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import apap.tutorial.manpromanpro.model.Proyek;

@Repository
public interface ProyekDb extends JpaRepository<Proyek, UUID> {
    List<Proyek> findBytanggalDihapusIsNull(Sort sort);
    List<Proyek> findByNamaContainingIgnoreCaseAndTanggalDihapusIsNull(String nama, Sort sort);
    List<Proyek> findByStatusIgnoreCaseAndTanggalDihapusIsNull(String status, Sort sort);
    List<Proyek> findByNamaContainingIgnoreCaseAndStatusIgnoreCaseAndTanggalDihapusIsNull(String nama, String status, Sort sort);
}
