package apap.tutorial.manpromanpro.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import apap.tutorial.manpromanpro.model.UserModel;
import apap.tutorial.manpromanpro.repository.UserDb;
import apap.tutorial.manpromanpro.restdto.request.CreateUserRequestDTO;
import apap.tutorial.manpromanpro.restdto.response.CreateUserResponseDTO;

@Service
public class UserRestServiceImpl implements UserRestService{
    @Autowired
    private UserDb userDb;

    @Autowired 
    private RoleRestService roleService;

    @Override
    public CreateUserResponseDTO addUser(CreateUserRequestDTO requestDTO) {
        UserModel user = new UserModel();
        
        user.setName(requestDTO.getName());
        user.setUsername(requestDTO.getUsername());
        user.setRole(roleService.getRoleByRoleName(requestDTO.getRole()));
        user.setPassword(hashPassword(requestDTO.getPassword()));
        
        userDb.save(user);
        
        CreateUserResponseDTO responseDTO = new CreateUserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setName(user.getName());
        responseDTO.setRole(user.getRole().getRole());
        responseDTO.setUsername(user.getUsername());
        
        return responseDTO;
    }

    @Override
    public String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
