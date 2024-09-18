package apap.tutorial.manpromanpro.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import apap.tutorial.manpromanpro.controller.DTO.ProyekDTO;
import apap.tutorial.manpromanpro.model.Proyek;
import apap.tutorial.manpromanpro.service.ProyekService;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ProyekController  {
    @Autowired
    private ProyekService proyekService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/proyek/add")
    public String addProyekForm(Model model) {
        var proyekDTO = new ProyekDTO();

        model.addAttribute("proyekDTO", proyekDTO);

        return "form-add-proyek";
    }

    @PostMapping("/proyek/add")
    public String addProyek(@ModelAttribute ProyekDTO proyekDTO, Model model) {
        if (proyekDTO.getTanggalSelesai().before(proyekDTO.getTanggalMulai())) {
            model.addAttribute("error", "Tanggal Selesai harus lebih awal dari Tanggal Mulai.");
            return "error-tanggal"; 
        }
        // Generate UUID baru untuk proyek
        UUID idProyek = UUID.randomUUID();

        // Membuat objek proyek baru dengan data dari DTO
        var proyek = new Proyek(idProyek,  proyekDTO.getNama(),  proyekDTO.getTanggalMulai(), proyekDTO.getTanggalSelesai(), proyekDTO.getStatus(), proyekDTO.getDeveloper());

        // Memanggil service untuk menambahkan proyek
        proyekService.createProyek(proyek);

        // Add variabel id proyek ke 'id' untuk dirender di thymeleaf
        model.addAttribute("id", proyek.getId());

        // Add variabel nama proyek ke 'Nama' untuk dirender di thymeleaf
        model.addAttribute("Nama", proyek.getNama());

        return "success-add-proyek";
    }

    @GetMapping("/proyek/viewall")
    public String listProyek(Model model) {
        // Mengambil semua proyek yang telah tersimpan
        List<Proyek> listProyek = proyekService.getAllProyek();

        // Add listproyek ke 'listProyek' untuk dirender di thymeleaf
        model.addAttribute("listProyek", listProyek);

        return "viewall-proyek";
    }
    
    @GetMapping("/proyek/{id}")
    public String detailProyek(@PathVariable(value = "id") String id, Model model) {
        // Convert String ID to UUID
        UUID proyekId = UUID.fromString(id);

        // Mengambil proyek berdasarkan id
        var proyek = proyekService.getProyekById(proyekId);

        // Add proyek ke 'proyek' untuk dirender di thymeleaf
        model.addAttribute("proyek", proyek);

        return "view-proyek";
    }

    @GetMapping("/proyek/{id}/update")
    public String updateProyekForm(@PathVariable(value = "id") UUID id, Model model) {
        
        // Mengambil proyek berdasarkan id
        var proyek = proyekService.getProyekById(id);
        var proyekDTO = new ProyekDTO(proyek.getId(), proyek.getNama(),  proyek.getTanggalMulai(), proyek.getTanggalSelesai(), proyek.getStatus(), proyek.getDeveloper());
        
        // Add proyek ke 'proyekDTO' untuk dirender di thymeleaf
        model.addAttribute("proyekDTO", proyekDTO);

        return "form-update-proyek";
    }

    @PostMapping("/proyek/update")
    public String updateProyek(@ModelAttribute ProyekDTO proyekDTO, Model model) {
        if (proyekDTO.getTanggalSelesai().before(proyekDTO.getTanggalMulai())) {
            model.addAttribute("error", "Tanggal Selesai harus lebih awal dari Tanggal Mulai.");
            return "error-tanggal"; 
        }
        // Membuat objek proyek baru dengan data dari DTO
        var proyek = new Proyek(proyekDTO.getId(),  proyekDTO.getNama(),  proyekDTO.getTanggalMulai(), proyekDTO.getTanggalSelesai(), proyekDTO.getStatus(), proyekDTO.getDeveloper());

        // Memanggil service untuk menambahkan proyek
        proyekService.updateProyek(proyek);

        // Add variabel id proyek ke 'id' untuk dirender di thymeleaf
        model.addAttribute("id", proyek.getId());

        // Add variabel nama proyek ke 'Nama' untuk dirender di thymeleaf
        model.addAttribute("Nama", proyek.getNama());

        return "success-update-proyek";
    }

    @GetMapping("/proyek/{id}/delete")
    public String deleteProyek(@PathVariable(value = "id") UUID id, Model model) {
        
        // Mengambil proyek berdasarkan id
        var proyek = proyekService.getProyekById(id);
        proyekService.deleteProyek(proyek);
        // Add variabel id proyek ke 'id' untuk dirender di thymeleaf
        model.addAttribute("id", proyek.getId());

        // Add variabel nama proyek ke 'Nama' untuk dirender di thymeleaf
        model.addAttribute("Nama", proyek.getNama());

        return "success-delete-proyek";
    }
}
