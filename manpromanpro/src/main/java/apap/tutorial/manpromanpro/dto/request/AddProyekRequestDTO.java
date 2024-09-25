package apap.tutorial.manpromanpro.dto.request;
import apap.tutorial.manpromanpro.model.Developer;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddProyekRequestDTO {
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

    private Developer developer;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tanggalDibentuk;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tanggalDiubah;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tanggalDihapus;
}
