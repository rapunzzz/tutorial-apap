package apap.tutorial.manpromanpro.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginJwtRequestDTO {
    private String username;
    private String password;
}
