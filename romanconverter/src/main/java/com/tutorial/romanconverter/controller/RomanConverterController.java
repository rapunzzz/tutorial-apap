package com.tutorial.romanconverter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tutorial.romanconverter.dto.RequestDTO;
import com.tutorial.romanconverter.model.RomanConverter;

@Controller
public class RomanConverterController {
    @GetMapping(value = "/")
    public String home(Model model) {
        return "view-home.html";
    }
    private String getRomanConverterPage(String roman, Model model) {
        final RomanConverter romanConverter = new RomanConverter(roman);
        if (!roman.matches("[IVXLCDM]+")) {
            model.addAttribute("error", "Terdapat kesalahan pada input");
            return "view-conversion-result.html"; // Kembali ke halaman dengan pesan error
        }
        model.addAttribute("romanConverter", romanConverter);
        return "view-conversion-result.html";
    }
    @GetMapping(value = "/roman-converter/{roman}")
    public String romanConverterWithPathVariable(@PathVariable(value = "roman") String roman, Model model) {
        return getRomanConverterPage(roman, model);
    }
    @GetMapping(value = "/roman-converter")
    public String romanConverterWithReqParam(@RequestParam(value = "roman") String roman, Model model) {
        return getRomanConverterPage(roman, model);
    }
    @GetMapping(value = "/convert")
    public String getRomanCoverterWithView(Model model) {
        var requestDTO = new RequestDTO();
        model.addAttribute("requestDTO", requestDTO);
        return "form.html";
    }

    @PostMapping(value = "/convert")
    public String postRomanConverterWithView(
        @ModelAttribute RequestDTO requestDTO, Model model
    ) {
        String romanFromView = requestDTO.getRoman();
        return getRomanConverterPage(romanFromView, model);
    }

    private String getAboutMePage(Model model) {
        model.addAttribute("name", "Thaariq Kurnia Spama");
        model.addAttribute("npm", "2206082801");
        return "view-about-me.html";
    }
    @GetMapping(value = "/about-me")
    public String aboutMe(Model model) {
        return getAboutMePage(model);
    }

}

