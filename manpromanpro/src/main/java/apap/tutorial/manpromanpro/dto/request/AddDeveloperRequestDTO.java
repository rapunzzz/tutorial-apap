package apap.tutorial.manpromanpro.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddDeveloperRequestDTO {
    private String nama;
    private String alamat;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tanggalBerdiri;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tanggalDibentuk;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tanggalDiubah;
}
