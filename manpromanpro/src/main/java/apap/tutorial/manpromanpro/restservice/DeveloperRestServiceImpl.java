package apap.tutorial.manpromanpro.restservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.tutorial.manpromanpro.model.Developer;
import apap.tutorial.manpromanpro.repository.DeveloperDb;
import apap.tutorial.manpromanpro.restdto.response.DeveloperOptionResponseDTO;

@Service
public class DeveloperRestServiceImpl implements DeveloperRestService{
    @Autowired
    DeveloperDb developerDb;
    

    @Override
    public List<DeveloperOptionResponseDTO> getAllDevelopers() {
        List<Developer> developers = developerDb.findAll();

        List<DeveloperOptionResponseDTO> developerOptions = new ArrayList<>();
        developers.forEach(developer -> developerOptions.add(developerToDeveloperOptionResponseDTO(developer)));
        
        return developerOptions;
    }

    private DeveloperOptionResponseDTO developerToDeveloperOptionResponseDTO (Developer developer) {
        DeveloperOptionResponseDTO developerOptionResponseDTO = new DeveloperOptionResponseDTO();
        developerOptionResponseDTO.setId(developer.getId());
        developerOptionResponseDTO.setNama(developer.getNama());

        return developerOptionResponseDTO;
    }
}
