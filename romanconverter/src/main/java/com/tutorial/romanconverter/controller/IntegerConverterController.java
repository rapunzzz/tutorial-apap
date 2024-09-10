package com.tutorial.romanconverter.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tutorial.romanconverter.dto.IntegerRequestDTO;
import com.tutorial.romanconverter.dto.RequestDTO;
import com.tutorial.romanconverter.model.IntegerConverter;

@Controller
public class IntegerConverterController {
    private String getIntegerConverterPage(int integer, Model model) {
        final IntegerConverter integerConverter = new IntegerConverter(integer);
        model.addAttribute("integerConverter", integerConverter);
        return "view-conversion-result-integer.html";
    }

    @GetMapping(value = "/convert-integer")
    public String getIntegerCoverterWithView(Model model) {
        var requestDTOInteger = new IntegerRequestDTO();
        model.addAttribute("requestDTOInteger", requestDTOInteger);
        return "form-integer-converter.html";
    }

    @PostMapping(value = "/convert-integer")
    public String postIntegerConverterWithView(
        @ModelAttribute IntegerRequestDTO requestDTOInteger, Model model
    ) {
        int IntegerFromView = requestDTOInteger.getInteger();
        return getIntegerConverterPage(IntegerFromView, model);
    }
}
