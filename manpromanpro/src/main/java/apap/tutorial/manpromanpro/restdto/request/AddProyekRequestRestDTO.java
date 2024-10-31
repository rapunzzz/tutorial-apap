package apap.tutorial.manpromanpro.restdto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddProyekRequestRestDTO {
    @NotBlank(message = "Nama proyek tidak boleh kosong")
    private String nama;

    @NotBlank(message = "Deskripsi proyek tidak boleh kosong")
    private String deskripsi;

    @NotNull(message = "Tanggal mulai tidak boleh kosong")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tanggalMulai;

    @NotNull(message = "Tanggal selesai tidak boleh kosong")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tanggalSelesai;

    @NotBlank(message = "Status tidak boleh kosong")
    private String status;

    @NotNull(message = "Pekerja proyek tidak boleh kosong")
    private List<Long> listPekerja;

    @NotNull(message = "Developer tidak boleh kosong")
    private Long developer;
}