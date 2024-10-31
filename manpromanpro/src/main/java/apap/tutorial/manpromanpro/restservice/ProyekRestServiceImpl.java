package apap.tutorial.manpromanpro.restservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.tutorial.manpromanpro.model.Developer;
import apap.tutorial.manpromanpro.model.Pekerja;
import apap.tutorial.manpromanpro.model.Proyek;
import apap.tutorial.manpromanpro.repository.DeveloperDb;
import apap.tutorial.manpromanpro.repository.PekerjaDb;
import apap.tutorial.manpromanpro.repository.ProyekDb;
import apap.tutorial.manpromanpro.restdto.request.AddProyekRequestRestDTO;
import apap.tutorial.manpromanpro.restdto.request.UpdateProyekRequestRestDTO;
import apap.tutorial.manpromanpro.restdto.response.DeveloperResponseDTO;
import apap.tutorial.manpromanpro.restdto.response.PekerjaResponseDTO;
import apap.tutorial.manpromanpro.restdto.response.ProyekResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProyekRestServiceImpl implements ProyekRestService{
    @Autowired
    ProyekDb proyekDb;

    @Autowired
    PekerjaDb pekerjaDb;

    @Autowired
    DeveloperDb developerDb;

    @Override
    public ProyekResponseDTO addProyek(AddProyekRequestRestDTO proyekDTO){
        Developer developer = developerDb.findById(proyekDTO.getDeveloper())
        .orElseThrow(() -> new IllegalArgumentException("Developer tidak ditemukan dengan ID: " + proyekDTO.getDeveloper()));

        // Lakukan pengolahan lain dan set Developer pada Proyek
        Proyek proyek = new Proyek();
        proyek.setDeveloper(developer);
        proyek.setNama(proyekDTO.getNama());
        proyek.setDeskripsi(proyekDTO.getDeskripsi());
        proyek.setTanggalMulai(proyekDTO.getTanggalMulai());
        proyek.setTanggalSelesai(proyekDTO.getTanggalSelesai());
        proyek.setStatus(proyekDTO.getStatus());

         Set<Pekerja> pekerjaSet = new HashSet<>();
        for (Long idPekerja : proyekDTO.getListPekerja()) {
            Pekerja pekerja = pekerjaDb.findById(idPekerja)
                .orElseThrow(() -> new IllegalArgumentException("Pekerja tidak ditemukan dengan ID: " + idPekerja));
            pekerjaSet.add(pekerja);
        }
        proyek.setListPekerja(new ArrayList<>(pekerjaSet));
            var newProyek = proyekDb.save(proyek);
            return proyekToProyekResponseDTO(newProyek);
        }

    @Override
    public List<ProyekResponseDTO> getAllProyek() {
        var listProyek = proyekDb.findAll();
        var listProyekResponseDTO = new ArrayList<ProyekResponseDTO>();
        listProyek.forEach(proyek -> {
            var proyekResponseDTO = proyekToProyekResponseDTO(proyek);
            listProyekResponseDTO.add(proyekResponseDTO);
        });

        return listProyekResponseDTO;
    }

    @Override
    public ProyekResponseDTO getProyekById(UUID idProyek) {
        var proyek = proyekDb.findById(idProyek).orElse(null);

        if (proyek == null) {
            return null;
        }

        return proyekToProyekResponseDTO(proyek);
    }

    @Override
    public ProyekResponseDTO updateProyekRest(UpdateProyekRequestRestDTO proyekDTO) {
        // Temukan developer berdasarkan ID dari proyekDTO
        Developer developer = developerDb.findById(proyekDTO.getDeveloper())
                .orElseThrow(() -> new IllegalArgumentException("Developer tidak ditemukan dengan ID: " + proyekDTO.getDeveloper()));

        // Temukan proyek berdasarkan ID dari proyekDTO
        Proyek proyek = proyekDb.findById(proyekDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Proyek tidak ditemukan dengan ID: " + proyekDTO.getId()));

        // Update field-field proyek
        proyek.setDeveloper(developer);
        proyek.setNama(proyekDTO.getNama());
        proyek.setDeskripsi(proyekDTO.getDeskripsi());
        proyek.setTanggalMulai(proyekDTO.getTanggalMulai());
        proyek.setTanggalSelesai(proyekDTO.getTanggalSelesai());
        proyek.setStatus(proyekDTO.getStatus());

        // Perbarui list pekerja
        Set<Pekerja> pekerjaSet = new HashSet<>();

        // Ambil list pekerja saat ini dari proyek
        var listPekerjaExisting = proyek.getListPekerja();

        // Hapus pekerja yang sudah tidak ada dalam list baru
        if (listPekerjaExisting != null && !listPekerjaExisting.isEmpty()) {
            listPekerjaExisting.forEach(pekerja -> {
                if (!proyekDTO.getListPekerja().contains(pekerja.getId())) {
                    pekerja.getListProyek().remove(proyek);
                }
            });
        }

        // Menambahkan pekerja baru ke proyek
        if (proyekDTO.getListPekerja() != null && !proyekDTO.getListPekerja().isEmpty()) {
            Set<Long> uniquePekerjaIds = new HashSet<>(proyekDTO.getListPekerja());
            uniquePekerjaIds.forEach(idPekerja -> {
                Pekerja pekerja = pekerjaDb.findById(idPekerja)
                        .orElseThrow(() -> new IllegalArgumentException("Pekerja tidak ditemukan dengan ID: " + idPekerja));
                pekerjaSet.add(pekerja);

                // Pastikan proyek ditambahkan ke listProyek pekerja jika belum ada
                if (!pekerja.getListProyek().contains(proyek)) {
                    pekerja.getListProyek().add(proyek);
                }
            });
        }

        // Set pekerja yang diperbarui ke proyek
        proyek.setListPekerja(new ArrayList<>(pekerjaSet));

        // Simpan proyek yang diperbarui
        Proyek updatedProyek = proyekDb.save(proyek);

        // Kembalikan hasil update sebagai DTO
        return proyekToProyekResponseDTO(updatedProyek);
    }




    private ProyekResponseDTO proyekToProyekResponseDTO(Proyek proyek) {
        ProyekResponseDTO proyekResponseDTO = new ProyekResponseDTO();
        proyekResponseDTO.setId(proyek.getId());
        proyekResponseDTO.setNama(proyek.getNama());
        proyekResponseDTO.setDeskripsi(proyek.getDeskripsi());
        proyekResponseDTO.setTanggalMulai(proyek.getTanggalMulai());
        proyekResponseDTO.setTanggalSelesai(proyek.getTanggalSelesai());
        proyekResponseDTO.setStatus(proyek.getStatus());
        
        // Add Developer details to the response
        Developer developer = proyek.getDeveloper();

        DeveloperResponseDTO developerResponseDTO = new DeveloperResponseDTO();
        developerResponseDTO.setId(developer.getId());
        developerResponseDTO.setNama(developer.getNama());
        developerResponseDTO.setAlamat(developer.getAlamat());
        developerResponseDTO.setTanggalBerdiri(developer.getTanggalBerdiri());
        developerResponseDTO.setEmail(developer.getEmail());
        developerResponseDTO.setCreatedAt(developer.getCreatedAt());
        developerResponseDTO.setUpdatedAt(developer.getUpdatedAt());
        proyekResponseDTO.setDeveloper(developerResponseDTO);
        // Konversi list pekerja ke PekerjaResponseDTO
        List<PekerjaResponseDTO> listPekerjaResponseDTO = new ArrayList<>();
    for (Pekerja pekerja : proyek.getListPekerja()) {
        // Cari Pekerja berdasarkan ID
        PekerjaResponseDTO pekerjaResponseDTO = new PekerjaResponseDTO();
        pekerjaResponseDTO.setId(pekerja.getId());
        pekerjaResponseDTO.setNama(pekerja.getNama());
        pekerjaResponseDTO.setUsia(pekerja.getUsia());
        pekerjaResponseDTO.setPekerjaan(pekerja.getPekerjaan());
        pekerjaResponseDTO.setBiografi(pekerja.getBiografi());
        pekerjaResponseDTO.setCreatedAt(pekerja.getCreatedAt());
        pekerjaResponseDTO.setUpdatedAt(pekerja.getUpdatedAt());
        
        listPekerjaResponseDTO.add(pekerjaResponseDTO);
    }
    proyekResponseDTO.setListPekerja(listPekerjaResponseDTO);

        return proyekResponseDTO;
    }

    @Override
    public void deleteProyekRest(UUID idProyek){
        Proyek proyek = proyekDb.findById(idProyek)
            .orElseThrow(() -> new EntityNotFoundException("Proyek dengan ID " + idProyek + " tidak ditemukan."));
        proyek.setDeletedAt(new Date());
        proyekDb.save(proyek);
    }
    
}
