package apap.tutorial.manpromanpro.dto.request;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data
public class UpdateDeveloperRequestDTO extends AddDeveloperRequestDTO{
    private Long id;
}
