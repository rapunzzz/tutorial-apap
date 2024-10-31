package apap.tutorial.manpromanpro.controller;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import apap.tutorial.manpromanpro.dto.request.AddProyekRequestDTO;
import apap.tutorial.manpromanpro.dto.request.UpdateProyekRequestDTO;
import apap.tutorial.manpromanpro.model.Pekerja;
import apap.tutorial.manpromanpro.model.Proyek;
import apap.tutorial.manpromanpro.restdto.request.UpdatePekerjaRequestRestDTO;
import apap.tutorial.manpromanpro.restdto.response.ProyekResponseDTO;
import apap.tutorial.manpromanpro.service.DeveloperService;
import apap.tutorial.manpromanpro.service.PekerjaService;
import apap.tutorial.manpromanpro.service.ProyekService;
import jakarta.validation.Valid;

@Controller
public class ProyekController {
    
    @Autowired
    private ProyekService proyekService;

    @Autowired
    private PekerjaService pekerjaService;

    @Autowired
    private DeveloperService developerService;

    enum StatusLevel {
        STARTED,
        ONGOING,
        FINISHED,
    }

    @GetMapping("/")
    private String home(Model model) {
        model.addAttribute("activeTab","home");
        return "home";
    }

    @GetMapping("/proyek/add")
    public String addProyekForm(Model model) {

        var proyekDTO = new AddProyekRequestDTO();

        model.addAttribute("proyekDTO", proyekDTO);
        model.addAttribute("listDeveloper", developerService.getAllDeveloper());
        model.addAttribute("statusLevel", StatusLevel.values());
        model.addAttribute("listPekerjaExisting", pekerjaService.getAllPekerja());
        model.addAttribute("activeTab","proyek");
        return "form-add-proyek";
    }

    @PostMapping("/proyek/add")
    public String addProyek(@Valid @ModelAttribute AddProyekRequestDTO proyekDTO, BindingResult result, Model model) {
        if(result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
            model.addAttribute("responseMessage", "Error: " + errorMessage);
            return "response-proyek";
        }

        var proyek = new Proyek();
        proyek.setNama(proyekDTO.getNama());
        proyek.setDeskripsi(proyekDTO.getDeskripsi());
        proyek.setTanggalMulai(proyekDTO.getTanggalMulai());
        proyek.setTanggalSelesai(proyekDTO.getTanggalSelesai());
        proyek.setStatus(proyekDTO.getStatus());
        proyek.setListPekerja(proyekDTO.getListPekerja());
        proyek.setDeveloper(proyekDTO.getDeveloper());
        proyek.setCreatedAt(new Date());
        proyek.setUpdatedAt(new Date());
        proyek.setDeletedAt(null);

        proyekService.addProyek(proyek);
        
        model.addAttribute("responseMessage",
                String.format("Proyek %s dengan ID %s berhasil ditambahkan.", proyek.getNama(), proyek.getId()));
        model.addAttribute("activeTab","proyek");
        return "response-proyek";
    }

    @PostMapping(value = "/proyek/add", params = {"addRow"})
    public String addRowDeveloperProyek(@ModelAttribute AddProyekRequestDTO addProyekRequestDTO, Model model) {
        if (addProyekRequestDTO.getListPekerja() == null || addProyekRequestDTO.getListPekerja().isEmpty()) {
            addProyekRequestDTO.setListPekerja(new ArrayList<>());
        }

        addProyekRequestDTO.getListPekerja().add(new Pekerja());

        model.addAttribute("listPekerjaExisting", pekerjaService.getAllPekerja());
        model.addAttribute("listDeveloper", developerService.getAllDeveloper());
        model.addAttribute("proyekDTO", addProyekRequestDTO);
        model.addAttribute("statusLevel", StatusLevel.values());
        model.addAttribute("activeTab","proyek");
        return "form-add-proyek";
    }

    @PostMapping(value = "/proyek/add", params = {"deleteRow"})
    public String deleteRowDeveloperProyek(@ModelAttribute AddProyekRequestDTO addProyekRequestDTO,
                                        @RequestParam("deleteRow") int row, Model model) {
        addProyekRequestDTO.getListPekerja().remove(row);

        model.addAttribute("listPekerjaExisting", pekerjaService.getAllPekerja());
        model.addAttribute("listDeveloper", developerService.getAllDeveloper());
        model.addAttribute("proyekDTO", addProyekRequestDTO);
        model.addAttribute("statusLevel", StatusLevel.values());
        model.addAttribute("activeTab","proyek");
        return "form-add-proyek";
    }

    @GetMapping("/proyek/viewall")
    public String listProyek(@RequestParam(value = "nama", required = false, defaultValue = "") String nama, 
                            @RequestParam(value = "status", required = false, defaultValue = "") String status, 
                            Model model) {
        List<Proyek> listProyek;

        if (nama.isEmpty() && status.isEmpty()) {
            listProyek = proyekService.getAllProyek();
        } else {
            listProyek = proyekService.getAllProyek(nama, status);
        }

        model.addAttribute("listProyek", listProyek);
        model.addAttribute("nama", nama);
        model.addAttribute("status", status);
        model.addAttribute("activeTab","proyek");
        return "viewall-proyek";
    }

    @GetMapping("/proyek/{id}")
    public String detailProyek(@PathVariable("id") UUID id, Model model) {
        var proyek = proyekService.getProyekById(id);

        model.addAttribute("proyek", proyek);
        model.addAttribute("activeTab","proyek");
        return "view-proyek";
    }

    @GetMapping("/proyek/{id}/update")
    public String updateProyek(@PathVariable("id") UUID id, Model model) {
        var proyek = proyekService.getProyekById(id);

        var proyekDTO = new UpdateProyekRequestDTO();
        proyekDTO.setId(proyek.getId());
        proyekDTO.setNama(proyek.getNama());
        proyekDTO.setDeskripsi(proyek.getDeskripsi());
        proyekDTO.setTanggalMulai(proyek.getTanggalMulai());
        proyekDTO.setTanggalSelesai(proyek.getTanggalSelesai());
        proyekDTO.setStatus(proyek.getStatus());
        proyekDTO.setListPekerja(proyek.getListPekerja());
        proyekDTO.setDeveloper(proyek.getDeveloper());
        proyekDTO.setUpdatedAt(proyek.getUpdatedAt());

        model.addAttribute("proyekDTO", proyekDTO);
        model.addAttribute("listDeveloper", developerService.getAllDeveloper());
        model.addAttribute("statusLevel", StatusLevel.values());
        model.addAttribute("listPekerjaExisting", pekerjaService.getAllPekerja());
        model.addAttribute("activeTab","proyek");
        return "form-update-proyek";
    }

    @PostMapping("/proyek/update")
    public String updateProyek(@Valid @ModelAttribute UpdateProyekRequestDTO proyekDTO, BindingResult result, Model model) {
        if(result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
            model.addAttribute("responseMessage", "Error: " + errorMessage);
            return "response-proyek";
        }
        
        var proyekFromDTO = new Proyek();
        proyekFromDTO.setId(proyekDTO.getId());
        proyekFromDTO.setNama(proyekDTO.getNama());
        proyekFromDTO.setDeskripsi(proyekDTO.getDeskripsi());
        proyekFromDTO.setTanggalMulai(proyekDTO.getTanggalMulai());
        proyekFromDTO.setTanggalSelesai(proyekDTO.getTanggalSelesai());
        proyekFromDTO.setStatus(proyekDTO.getStatus());
        proyekFromDTO.setDeveloper(proyekDTO.getDeveloper());
        proyekFromDTO.setUpdatedAt(proyekDTO.getUpdatedAt());
        proyekFromDTO.setListPekerja(proyekDTO.getListPekerja());
        var proyek = proyekService.updateProyek(proyekFromDTO);

        model.addAttribute("responseMessage",
                String.format("Proyek %s dengan ID %s berhasil diupdate.", proyek.getNama(), proyek.getId()));
        model.addAttribute("activeTab","proyek");
        return "response-proyek";
    }
    @PostMapping(value = "/proyek/update", params = {"addRow"})
    public String addRowDeveloperProyekUpdate(@ModelAttribute UpdateProyekRequestDTO proyekDTO, Model model) {
        if (proyekDTO.getListPekerja() == null || proyekDTO.getListPekerja().isEmpty()) {
            proyekDTO.setListPekerja(new ArrayList<>());
        }

        proyekDTO.getListPekerja().add(new Pekerja());
        
        model.addAttribute("proyekDTO", proyekDTO);
        model.addAttribute("listDeveloper", developerService.getAllDeveloper());
        model.addAttribute("listPekerjaExisting", pekerjaService.getAllPekerja());
        model.addAttribute("statusLevel", StatusLevel.values());

        model.addAttribute("activeTab","proyek");
        return "form-update-proyek";
    }

    @PostMapping(value = "/proyek/update", params = {"deleteRow"})
    public String deleteRowDeveloperProyekUpdate(@ModelAttribute UpdateProyekRequestDTO proyekDTO,
                                        @RequestParam("deleteRow") int row, Model model) {
        proyekDTO.getListPekerja().remove(row);
        
        model.addAttribute("listPekerjaExisting", pekerjaService.getAllPekerja());
        model.addAttribute("listDeveloper", developerService.getAllDeveloper());
        model.addAttribute("proyekDTO", proyekDTO);
        model.addAttribute("statusLevel", StatusLevel.values());
        model.addAttribute("activeTab","proyek");
        return "form-update-proyek";
    }

    @GetMapping("/proyek/{id}/delete")
    public String deleteProyek(@PathVariable("id") UUID id, Model model) {
        var proyek = proyekService.getProyekById(id);
        proyekService.deleteProyek(proyek);

        model.addAttribute("responseMessage",
                String.format("Proyek %s dengan ID %s berhasil dihapus.", proyek.getNama(), proyek.getId()));
        model.addAttribute("activeTab","proyek");
        return "response-proyek";
    }

    @GetMapping("/proyek/datatable")
    public String listProyekDatatable(@RequestParam(value = "nama", required = false, defaultValue = "") String nama, 
                            @RequestParam(value = "status", required = false, defaultValue = "") String status, 
                            Model model) {
        List<Proyek> listProyek;

        if (nama.isEmpty() && status.isEmpty()) {
            listProyek = proyekService.getAllProyek();
        } else {
            listProyek = proyekService.getAllProyek(nama, status);
        }

        model.addAttribute("listProyek", listProyek);
        model.addAttribute("nama", nama);
        model.addAttribute("status", status);
        model.addAttribute("activeTab","proyek");
        return "viewall-proyek-datatable";
    }

}