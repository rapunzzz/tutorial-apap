package apap.tutorial.manpromanpro.restcontroller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.tutorial.manpromanpro.restdto.request.CreateUserRequestDTO;
import apap.tutorial.manpromanpro.restdto.response.BaseResponseDTO;
import apap.tutorial.manpromanpro.restdto.response.CreateUserResponseDTO;
import apap.tutorial.manpromanpro.restservice.UserRestService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/user")
public class UserRestController {
    @Autowired
    UserRestService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody CreateUserRequestDTO userRequestDTO) {
        var baseResponseDTO = new BaseResponseDTO<CreateUserResponseDTO>();
        try {
            CreateUserResponseDTO userResponseDTO = userService.addUser(userRequestDTO);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(userResponseDTO);
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setMessage(String.format("User %s dengan id %d berhasil dibuat!", 
                                        userResponseDTO.getUsername(), 
                                        userResponseDTO.getId()));
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setMessage("Gagal membuat User!");
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
