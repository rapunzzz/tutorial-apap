package apap.tutorial.manpromanpro.restcontroller;

import apap.tutorial.manpromanpro.restdto.request.AddPekerjaRequestRestDTO;
import apap.tutorial.manpromanpro.restdto.request.UpdatePekerjaRequestRestDTO;
import apap.tutorial.manpromanpro.restdto.response.BaseResponseDTO;
import apap.tutorial.manpromanpro.restdto.response.PekerjaResponseDTO;
import apap.tutorial.manpromanpro.restdto.response.ProyekResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import apap.tutorial.manpromanpro.restservice.PekerjaRestService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@Getter
@Setter
@RequestMapping("/api/pekerja")
public class PekerjaRestController {

    @Autowired
    PekerjaRestService pekerjaRestService;

    @GetMapping("/viewall")
    public ResponseEntity<?> listPekerja() {
        var baseResponseDTO = new BaseResponseDTO<List<PekerjaResponseDTO>>();
        List<PekerjaResponseDTO> listPekerja = pekerjaRestService.getAllPekerja();

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listPekerja);
        baseResponseDTO.setMessage(String.format("List pekerja berhasil ditemukan"));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detailPekerja(@PathVariable("id") Long id) {
        var baseResponseDTO = new BaseResponseDTO<PekerjaResponseDTO>();

        PekerjaResponseDTO pekerja = pekerjaRestService.getPekerjaById(id);
        if (pekerja == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data pekerja tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(pekerja);
        baseResponseDTO.setMessage(String.format("Pekerja dengan ID %s berhasil ditemukan", pekerja.getId()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPekerja(@Valid @RequestBody AddPekerjaRequestRestDTO pekerjaDTO,
                                           BindingResult bindingResult) {
        var baseResponseDTO = new BaseResponseDTO<PekerjaResponseDTO>();

        if (bindingResult.hasFieldErrors()) {
            String errorMessages = "";
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessages += error.getDefaultMessage() + "; ";
            }

            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(errorMessages);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }

        PekerjaResponseDTO pekerja = pekerjaRestService.addPekerja(pekerjaDTO);
        if (pekerja == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data pekerja tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.CREATED.value());
        baseResponseDTO.setData(pekerja);
        baseResponseDTO.setMessage(String.format("Pekerja dengan ID %s berhasil ditambahkan", pekerja.getId()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePekerja(@Valid @RequestBody UpdatePekerjaRequestRestDTO pekerjaDTO,
                                           BindingResult bindingResult) {
        var baseResponseDTO = new BaseResponseDTO<PekerjaResponseDTO>();

        if (bindingResult.hasFieldErrors()) {
            String errorMessages = "";
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessages += error.getDefaultMessage() + "; ";
            }

            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(errorMessages);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }

        PekerjaResponseDTO pekerja = pekerjaRestService.updatePekerjaRest(pekerjaDTO);
        if (pekerja == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data pekerja tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(pekerja);
        baseResponseDTO.setMessage(String.format("Pekerja dengan ID %s berhasil diubah", pekerja.getId()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePekerja(@RequestBody List<Long> listIdPekerja) {
        var baseResponseDTO = new BaseResponseDTO<Void>();

        // Menghapus duplikat ID menggunakan Set
        Set<Long> uniqueIds = new HashSet<>(listIdPekerja);
        List<Long> notFoundIds = new ArrayList<>();
        List<Long> hasProjectsIds = new ArrayList<>();

        List<Long> idsToDelete = new ArrayList<>();

        for (Long id : uniqueIds) {
            try {
                var pekerja = pekerjaRestService.getPekerjaById(id);
                
                if (pekerja == null) {
                    notFoundIds.add(id); 
                    continue;
                }
                
                List<ProyekResponseDTO> listProyek = pekerja.getListProyek();

                // Periksa apakah daftar proyek tidak null dan tidak kosong
                if (listProyek != null && !listProyek.isEmpty()) {
                    hasProjectsIds.add(id);
                } else {
                    idsToDelete.add(id);
                }
            } catch (EntityNotFoundException e) {
                notFoundIds.add(id); 
            }
        }

        if (!idsToDelete.isEmpty()) {
            pekerjaRestService.deletePekerjaRest(idsToDelete); 
        }

        // Menyiapkan response
        if (!notFoundIds.isEmpty()) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage("Pekerja dengan ID " + notFoundIds + " tidak ditemukan");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } 

        if (!hasProjectsIds.isEmpty()) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage("Pekerja dengan ID " + hasProjectsIds + " masih memiliki proyek");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }

        // Jika semua berhasil dihapus
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setMessage("List Pekerja berhasil dihapus");
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

}