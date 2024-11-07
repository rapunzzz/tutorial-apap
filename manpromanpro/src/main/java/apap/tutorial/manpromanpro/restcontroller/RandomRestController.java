package apap.tutorial.manpromanpro.restcontroller;

import apap.tutorial.manpromanpro.restdto.response.BaseResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Random;


@RestController
@Getter
@Setter
@RequestMapping("/api")
public class RandomRestController {

    @GetMapping("/random")
    public ResponseEntity<?> random() {
        Random random = new Random();
        var theBool = random.nextBoolean();

        // Inisialisasi BaseResponseDTO dengan tipe `Void`
        var baseResponseDTO = new BaseResponseDTO<Void>();
        baseResponseDTO.setTimestamp(new Date());

        if (theBool) {
            // Jika hasil random adalah true, set response sebagai OK
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Random menghasilkan true");
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } else {
            // Jika hasil random adalah false, set response sebagai BAD_REQUEST
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage("Random menghasilkan false");
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

}
