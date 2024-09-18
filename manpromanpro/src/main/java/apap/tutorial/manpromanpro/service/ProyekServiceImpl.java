package apap.tutorial.manpromanpro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import apap.tutorial.manpromanpro.model.Proyek;

@Service
public class ProyekServiceImpl implements ProyekService{
    private List<Proyek> listProyek = new ArrayList<Proyek>();

    @Override
    public void createProyek(Proyek proyek) {
        listProyek.add(proyek);
    }

    @Override
    public List<Proyek> getAllProyek() {
        return listProyek;
    }

    @Override
    public Proyek getProyekById(UUID id) {
        for (Proyek proyek : listProyek) {
            if (proyek.getId().equals(id)) {
                return proyek;
            }
        }
        return null; 
    }

    @Override
    public void updateProyek(Proyek proyek) {
        // Cari proyek yang sudah ada berdasarkan id
        Proyek proyekToUpdate = null;
        for (Proyek a : listProyek) {
            if (a.getId().equals(proyek.getId())) {
                proyekToUpdate = a;
                break;
            }
        }

        if (proyekToUpdate != null) {
            
            // Update nilai-nilai proyek
            proyekToUpdate.setNama(proyek.getNama());
            proyekToUpdate.setDeveloper(proyek.getDeveloper());
            proyekToUpdate.setTanggalMulai(proyek.getTanggalMulai());
            proyekToUpdate.setTanggalSelesai(proyek.getTanggalSelesai());
            proyekToUpdate.setStatus(proyek.getStatus());

        } else {
            throw new NoSuchElementException("Proyek tidak ditemukan");
        }
    }

    @Override
    public void deleteProyek(Proyek proyek) {
        listProyek.remove(proyek);
    }
}
