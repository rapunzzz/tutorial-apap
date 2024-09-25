package apap.tutorial.manpromanpro.dto.request;

import java.util.UUID;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data
public class UpdateProyekRequestDTO extends AddProyekRequestDTO{
    @NotNull(message = "ID proyek tidak boleh kosong")
    private UUID id;
}
