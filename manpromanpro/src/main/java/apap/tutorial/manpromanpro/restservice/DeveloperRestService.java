package apap.tutorial.manpromanpro.restservice;

import java.util.List;

import apap.tutorial.manpromanpro.restdto.response.DeveloperOptionResponseDTO;

public interface DeveloperRestService {
    List<DeveloperOptionResponseDTO> getAllDevelopers();
}
