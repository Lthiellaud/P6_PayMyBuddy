package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Rib;
import com.paymybuddy.webapp.model.DTO.RibDTO;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.PMBSharedService;
import com.paymybuddy.webapp.service.RibService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;


@Controller
public class RibController {

    @Autowired
    private RibService ribService;
    
    @Autowired
    private PMBSharedService pmbSharedService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RibController.class);

    @ModelAttribute("ribDTO")
    public RibDTO getRibDTOObject(){
        return new RibDTO();
    }

    @GetMapping("/home/rib")
    public String getRibPage(Model model) {
        List<Rib> ribs = pmbSharedService.getUserRib();
        model.addAttribute("ribs", ribs);
        return "ribPage";
    }
    
    @PostMapping("/home/rib")
    public String addRib(@ModelAttribute ("ribDTO") @Valid RibDTO ribDTO,
                               final BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return getRibPage(model);
        }
        Response response= ribService.processRib(ribDTO);
        switch (response) {
            case OK:
                model.addAttribute("RibDTO", new RibDTO());
                model.addAttribute("message", response.getMessage());
                break;
            case EXISTING_RIB_NAME:
                bindingResult.rejectValue("ribName", "error.ribDTO", response.getMessage());
                break;
            case EXISTING_IBAN:
            case IBAN_BIC_KO:
            case SAVE_KO:
                model.addAttribute("message", response.getMessage());
                break;
            default:
        }
        return getRibPage(model);
    }
    
}
