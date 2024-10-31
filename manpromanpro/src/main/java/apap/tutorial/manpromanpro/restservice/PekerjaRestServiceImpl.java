package apap.tutorial.manpromanpro.restservice;

import apap.tutorial.manpromanpro.restdto.request.AddPekerjaRequestRestDTO;
import apap.tutorial.manpromanpro.restdto.request.UpdatePekerjaRequestRestDTO;
import apap.tutorial.manpromanpro.restdto.response.DeveloperResponseDTO;
import apap.tutorial.manpromanpro.restdto.response.PekerjaResponseDTO;
import apap.tutorial.manpromanpro.restdto.response.ProyekResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import apap.tutorial.manpromanpro.model.Pekerja;
import apap.tutorial.manpromanpro.model.Proyek;
import apap.tutorial.manpromanpro.repository.PekerjaDb;
import apap.tutorial.manpromanpro.repository.ProyekDb;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

@Service
@Transactional
public class PekerjaRestServiceImpl implements PekerjaRestService {
    @Autowired
    PekerjaDb pekerjaDb;

    @Autowired
    ProyekDb proyekDb;

    @Override
    public PekerjaResponseDTO addPekerja(AddPekerjaRequestRestDTO pekerjaDTO) {
        Pekerja pekerja = new Pekerja();
        pekerja.setNama(pekerjaDTO.getNama());
        pekerja.setUsia(pekerjaDTO.getUsia());
        pekerja.setPekerjaan(pekerjaDTO.getPekerjaan());

        if (pekerjaDTO.getBiografi() != null) {
            pekerja.setBiografi(pekerjaDTO.getBiografi());
        }

        Set<Proyek> proyekSet = new HashSet<>();
        if (pekerjaDTO.getListProyek() != null) {
            Set<UUID> uniqueProjectIds = new HashSet<>(pekerjaDTO.getListProyek());
           uniqueProjectIds.forEach(idProyek -> {
                Proyek proyek = proyekDb.findById(idProyek).orElse(null);
                if (proyek != null) {
                    // Add the Proyek to the Pekerja's listProyek
                    proyekSet.add(proyek);
                    // Add the Pekerja to the Proyek's listPekerja
                    proyek.getListPekerja().add(pekerja);
                }
            });
        }
        pekerja.setListProyek(new ArrayList<>(proyekSet));
        var newPekerja = pekerjaDb.save(pekerja);
        return pekerjaToPekerjaResponseDTO(newPekerja);
    }

    @Override
    public List<PekerjaResponseDTO> getAllPekerja() {

        var listPekerja = pekerjaDb.findAll();
        var listPekerjaResponseDTO = new ArrayList<PekerjaResponseDTO>();
        listPekerja.forEach(pekerja -> {
            var pekerjaResponseDTO =pekerjaToPekerjaResponseDTO(pekerja);
            listPekerjaResponseDTO.add(pekerjaResponseDTO);
        });

        return listPekerjaResponseDTO;
    }

    @Override
    public PekerjaResponseDTO getPekerjaById(Long idPekerja) {
        var pekerja = pekerjaDb.findById(idPekerja).orElse(null);

        if (pekerja == null) {
            return null;
        }

        return pekerjaToPekerjaResponseDTO(pekerja);
    }

    @Override
    public PekerjaResponseDTO updatePekerjaRest(UpdatePekerjaRequestRestDTO pekerjaDTO) {
        Pekerja pekerja = pekerjaDb.findById(pekerjaDTO.getId()).orElse(null);
        if (pekerja == null) {
            return null;
        }

        pekerja.setNama(pekerjaDTO.getNama());
        pekerja.setUsia(pekerjaDTO.getUsia());
        pekerja.setPekerjaan(pekerjaDTO.getPekerjaan());

        if (pekerjaDTO.getBiografi() != null) {
            pekerja.setBiografi(pekerjaDTO.getBiografi());
        }
        
        var listProyekFromDTO = pekerjaDTO.getListProyek();
        if (listProyekFromDTO != null) {
            Set<Proyek> proyekSet = new HashSet<>();

            var listProyekExisting = pekerja.getListProyek();

            // Pekerja sudah memiliki list proyek sebelumnya
            if (listProyekExisting != null && !listProyekExisting.isEmpty()) {
                // Menghapus proyek yang tidak ada dalam list proyek baru
                listProyekExisting.forEach(proyek -> {
                    if (!listProyekFromDTO.contains(proyek.getId())) {
                        proyek.getListPekerja().remove(pekerja);
                    }
                });

                // Menggunakan Set untuk mendapatkan ID proyek unik
                Set<UUID> uniqueProjectIds = new HashSet<>(listProyekFromDTO);
                uniqueProjectIds.forEach(idProyek -> {
                    Proyek proyek = proyekDb.findById(idProyek).orElse(null);
                    if (proyek != null) {
                        // Add the Proyek to the Pekerja's listProyek
                        proyekSet.add(proyek);

                        // Add the Pekerja to the Proyek's listPekerja
                        if (!proyek.getListPekerja().contains(pekerja)) {
                            proyek.getListPekerja().add(pekerja);
                        }
                    }
                });

                pekerja.setListProyek(new ArrayList<>(proyekSet));
            // Pekerja belum memiliki list proyek sebelumnya
            } else {
                // Menggunakan Set untuk mendapatkan ID proyek unik
                Set<UUID> uniqueProjectIds = new HashSet<>(listProyekFromDTO);
                uniqueProjectIds.forEach(idProyek -> {
                    Proyek proyek = proyekDb.findById(idProyek).orElse(null);
                    if (proyek != null) {
                        // Add the Proyek to the Pekerja's listProyek
                        proyekSet.add(proyek);
                        
                        // Add the Pekerja to the Proyek's listPekerja
                        proyek.getListPekerja().add(pekerja);
                    }
                });
                pekerja.setListProyek(new ArrayList<>(proyekSet));
            }
        }

        var updatePekerja = pekerjaDb.save(pekerja);
        return pekerjaToPekerjaResponseDTO(updatePekerja);
    }


    private PekerjaResponseDTO pekerjaToPekerjaResponseDTO(Pekerja pekerja) {
        var pekerjaResponseDTO = new PekerjaResponseDTO();
        pekerjaResponseDTO.setId(pekerja.getId());
        pekerjaResponseDTO.setNama(pekerja.getNama());
        pekerjaResponseDTO.setUsia(pekerja.getUsia());
        pekerjaResponseDTO.setPekerjaan(pekerja.getPekerjaan());
        pekerjaResponseDTO.setBiografi(pekerja.getBiografi());
        pekerjaResponseDTO.setCreatedAt(pekerja.getCreatedAt());
        pekerjaResponseDTO.setUpdatedAt(pekerja.getUpdatedAt());

        if (pekerja.getListProyek() != null) {
            var listProyekResponseDTO = new ArrayList<ProyekResponseDTO>();
            pekerja.getListProyek().forEach(proyek -> {
                var proyekResponseDTO = new ProyekResponseDTO();
                var developerResponseDTO = new DeveloperResponseDTO();

                proyekResponseDTO.setId(proyek.getId());
                proyekResponseDTO.setNama(proyek.getNama());
                proyekResponseDTO.setDeskripsi(proyek.getDeskripsi());
                proyekResponseDTO.setTanggalMulai(proyek.getTanggalMulai());
                proyekResponseDTO.setTanggalSelesai(proyek.getTanggalSelesai());
                proyekResponseDTO.setStatus(proyek.getStatus());
                proyekResponseDTO.setCreatedAt(proyek.getCreatedAt());
                proyekResponseDTO.setUpdatedAt(proyek.getUpdatedAt());

                developerResponseDTO.setId(proyek.getDeveloper().getId());
                developerResponseDTO.setNama(proyek.getDeveloper().getNama());
                developerResponseDTO.setAlamat(proyek.getDeveloper().getAlamat());
                developerResponseDTO.setTanggalBerdiri(proyek.getDeveloper().getTanggalBerdiri());
                developerResponseDTO.setEmail(proyek.getDeveloper().getEmail());
                developerResponseDTO.setCreatedAt(proyek.getDeveloper().getCreatedAt());
                developerResponseDTO.setUpdatedAt(proyek.getDeveloper().getUpdatedAt());
                proyekResponseDTO.setDeveloper(developerResponseDTO);

                listProyekResponseDTO.add(proyekResponseDTO);
            });

            pekerjaResponseDTO.setListProyek(listProyekResponseDTO);
        }
        return pekerjaResponseDTO;
    }

    @Override
    public void deletePekerjaRest(List<Long> listIdPekerja) throws EntityNotFoundException, ConstraintViolationException {
        var pekerjaToDelete = new ArrayList<Pekerja>();

        if (listIdPekerja != null && !listIdPekerja.isEmpty()) {
            for (Long id : listIdPekerja) {
                // Mengambil pekerja berdasarkan ID
                Pekerja pekerja = pekerjaDb.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Pekerja dengan ID " + id + " tidak ditemukan."));

                // Memeriksa apakah pekerja memiliki proyek
                if (pekerja.getListProyek() == null || pekerja.getListProyek().isEmpty()) {
                    pekerja.setDeletedAt(new Date());
                    pekerjaToDelete.add(pekerja);
                } else {
                    // Jika pekerja memiliki proyek, lemparkan exception
                    throw new ConstraintViolationException("Pekerja dengan ID " + id + " masih memiliki proyek.", null);
                }
            }
        }

        // Simpan semua pekerja yang akan dihapus
        pekerjaDb.saveAll(pekerjaToDelete);
    }

}