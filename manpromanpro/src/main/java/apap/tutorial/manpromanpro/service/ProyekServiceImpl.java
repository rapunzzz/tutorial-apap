package apap.tutorial.manpromanpro.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import apap.tutorial.manpromanpro.repository.ProyekDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import apap.tutorial.manpromanpro.model.Proyek;

@Service
public class ProyekServiceImpl implements ProyekService {
    @Autowired
    ProyekDb proyekDb;

    @Override
    public Proyek addProyek(Proyek proyek) {
        return proyekDb.save(proyek);
    }

    @Override
    public List<Proyek> getAllProyek() {
        Sort.Order orderByNama = Sort.Order.by("nama").ignoreCase();
        Sort sort = Sort.by(orderByNama);
        return proyekDb.findByDeletedAtIsNull(sort);
    }

    @Override
    public List<Proyek> getAllProyek(String nama, String status) {
        Sort.Order orderByNama = Sort.Order.by("nama").ignoreCase();
        Sort sort = Sort.by(orderByNama);
        if (status.isEmpty()) {
            return proyekDb.findByNamaContainingIgnoreCaseAndDeletedAtIsNull(nama, sort);
        } else if (nama.isEmpty()) {
            return proyekDb.findByStatusIgnoreCaseAndDeletedAtIsNull(status, sort);
        } else {
            return proyekDb.findByNamaContainingIgnoreCaseAndStatusIgnoreCaseAndDeletedAtIsNull(nama, status, sort);
        }
    }

    @Override
    public Proyek getProyekById(UUID idProyek) {
        for (Proyek proyek : getAllProyek()) {
            if (proyek.getId().equals(idProyek)) {
                return proyek;
            }
        }
        return null;
    }

    @Override
    public Proyek updateProyek(Proyek proyek) {
        Proyek getProyek = getProyekById(proyek.getId());
        if (getProyek != null) {
            getProyek.setNama(proyek.getNama());
            getProyek.setDeskripsi(proyek.getDeskripsi());
            getProyek.setTanggalMulai(proyek.getTanggalMulai());
            getProyek.setTanggalSelesai(proyek.getTanggalSelesai());
            getProyek.setStatus(proyek.getStatus());
            getProyek.setDeveloper(proyek.getDeveloper());
            getProyek.setUpdatedAt(new Date());
            getProyek.setListPekerja(proyek.getListPekerja());
            proyekDb.save(getProyek);

            return getProyek;
        }

        return null;
    }

    @Override
    public void deleteProyek(Proyek proyek) {
        proyek.setDeletedAt(new Date());
        proyekDb.save(proyek);
        // proyekDb.delete(proyek);
    }

}