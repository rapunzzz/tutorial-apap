package apap.tutorial.manpromanpro.restcontroller;

import apap.tutorial.manpromanpro.model.Proyek;
import apap.tutorial.manpromanpro.restdto.request.AddPekerjaRequestRestDTO;
import apap.tutorial.manpromanpro.restdto.request.AddProyekRequestRestDTO;
import apap.tutorial.manpromanpro.restdto.request.UpdatePekerjaRequestRestDTO;
import apap.tutorial.manpromanpro.restdto.request.UpdateProyekRequestRestDTO;
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
import apap.tutorial.manpromanpro.restservice.ProyekRestService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@Getter
@Setter
@RequestMapping("/api/proyek")
public class ProyekRestController {

    @Autowired
    ProyekRestService proyekRestService;


    @GetMapping("/{id}")
    public ResponseEntity<?> detailProyek(@PathVariable("id") UUID id) {
        var baseResponseDTO = new BaseResponseDTO<ProyekResponseDTO>();

        ProyekResponseDTO proyek = proyekRestService.getProyekById(id);
        if  (proyek == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data proyek tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(proyek);
        baseResponseDTO.setMessage(String.format("Proyek dengan ID %s berhasil ditemukan", proyek.getId()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProyek(@Valid @RequestBody AddProyekRequestRestDTO proyekDTO,
                                           BindingResult bindingResult) {
        var baseResponseDTO = new BaseResponseDTO<ProyekResponseDTO>();

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

        ProyekResponseDTO proyek = proyekRestService.addProyek(proyekDTO);
        if (proyek == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data proyek tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.CREATED.value());
        baseResponseDTO.setData(proyek);
        baseResponseDTO.setMessage(String.format("Proyek dengan ID %s berhasil ditambahkan", proyek.getId()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateProyek(@PathVariable UUID id, 
                                        @Valid @RequestBody UpdateProyekRequestRestDTO proyekDTO,
                                        BindingResult bindingResult) {
        var baseResponseDTO = new BaseResponseDTO<ProyekResponseDTO>();

        // Handle validation errors
        if (bindingResult.hasFieldErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                                                .map(FieldError::getDefaultMessage)
                                                .collect(Collectors.joining("; "));
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(errorMessages);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }

        // Set the ID from the path variable to the DTO (since it's not sent in the body)
        proyekDTO.setId(id);

        // Call the service to update the project
        ProyekResponseDTO proyek = proyekRestService.updateProyekRest(proyekDTO);
        if (proyek == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data proyek tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        // Return success response
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(proyek);
        baseResponseDTO.setMessage(String.format("Proyek dengan ID %s berhasil diubah", proyek.getId()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }


    @DeleteMapping("/{idProyek}/delete")
    public ResponseEntity<?> deleteProyekRest(@RequestBody @PathVariable("idProyek") UUID idProyek) {
        var baseResponseDTO = new BaseResponseDTO<Void>();

        try {
            // Mencari proyek berdasarkan ID
            var proyek = proyekRestService.getProyekById(idProyek);
            
            // Jika proyek tidak ditemukan, lemparkan exception
            if (proyek == null) {
                baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
                baseResponseDTO.setMessage("Data proyek tidak ditemukan");
                baseResponseDTO.setTimestamp(new Date());
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
            }

            proyekRestService.deleteProyekRest(idProyek);

            // Menyiapkan response sukses
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Proyek dengan ID " + idProyek + " berhasil dihapus");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
            
        } catch (EntityNotFoundException e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage("Data proyek tidak ditemukan");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan saat menghapus proyek");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
