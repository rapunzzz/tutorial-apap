package apap.tutorial.manpromanpro.restdto.response;

import java.util.UUID;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProyekResponseDTO {
    private UUID id;
    private String nama;
    private String deskripsi;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tanggalMulai;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tanggalSelesai;

    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DeveloperResponseDTO developer;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PekerjaResponseDTO> listPekerja;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date deletedAt;
}
