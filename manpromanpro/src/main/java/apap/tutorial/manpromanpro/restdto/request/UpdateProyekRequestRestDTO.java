package apap.tutorial.manpromanpro.restdto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data
public class UpdateProyekRequestRestDTO extends AddProyekRequestRestDTO {
    @NotNull(message = "ID proyek tidak boleh kosong")
    private UUID id;
}
