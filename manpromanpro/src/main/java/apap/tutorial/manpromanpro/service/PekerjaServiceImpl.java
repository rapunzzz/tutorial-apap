package apap.tutorial.manpromanpro.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import apap.tutorial.manpromanpro.model.Pekerja;
import apap.tutorial.manpromanpro.repository.PekerjaDb;

@Service
public class PekerjaServiceImpl implements PekerjaService{

    @Autowired
    PekerjaDb pekerjaDb;
    
    @Override
    public Pekerja addPekerja(Pekerja pekerja) {
        return pekerjaDb.save(pekerja);
    }

    @Override
    public List<Pekerja> getAllPekerja() {
        return pekerjaDb.findBytanggalDihapusIsNull(Sort.by(Sort.Order.asc("nama").ignoreCase()));
    }

    @Override 
    public void deleteListPekerja(List<Pekerja> listPekerja) {
        var pekerjaToDelete = new ArrayList<Pekerja>();
        
        if(listPekerja != null) {
            for(Pekerja pekerja : listPekerja) {
                if(pekerja.getListProyek() == null || pekerja.getListProyek().isEmpty()) {
                    pekerja.setTanggalDihapus(new Date());
                    pekerjaToDelete.add(pekerja);
                }
            }
        }

        pekerjaDb.saveAll(pekerjaToDelete);
    }
}
