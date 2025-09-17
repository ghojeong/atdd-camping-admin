package com.camping.admin.web;

import com.camping.admin.domain.entity.Campsite;
import com.camping.admin.repository.CampsiteRepository;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/console/campsites")
public class ConsoleCampsiteController {

    private final CampsiteRepository campsiteRepository;

    public ConsoleCampsiteController(CampsiteRepository campsiteRepository) {
        this.campsiteRepository = campsiteRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("campsites", campsiteRepository.findAll());
        return "campsites/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("formAction", "/console/campsites");
        return "campsites/form";
    }

    @PostMapping
    public String create(@RequestParam Map<String, String> params, RedirectAttributes redirectAttributes) {
        String siteNumber = params.containsKey("siteNumber") ? params.get("siteNumber") : null;
        String description = params.containsKey("description") ? params.get("description") : "";
        Integer maxPeople;
        if (params.containsKey("maxPeople")) {
            try {
                maxPeople = Integer.valueOf(params.get("maxPeople"));
            } catch (Exception e) {
                maxPeople = null;
            }
        } else {
            maxPeople = null;
        }

        Campsite entity = new Campsite(siteNumber, description, maxPeople);
        campsiteRepository.save(entity);
        redirectAttributes.addFlashAttribute("success", "캠프사이트가 등록되었습니다.");
        return "redirect:/console/campsites";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Campsite campsite = campsiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find campsite with id: " + id));
        model.addAttribute("formAction", "/console/campsites/" + id);
        model.addAttribute("campsite", campsite);
        return "campsites/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @RequestParam Map<String, String> params, RedirectAttributes redirectAttributes) {
        Campsite campsite = campsiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find campsite with id: " + id));

        if (params.containsKey("siteNumber")) {
            campsite.setSiteNumber(params.get("siteNumber"));
        }
        if (params.containsKey("description")) {
            campsite.setDescription(params.get("description"));
        }
        if (params.containsKey("maxPeople")) {
            try {
                campsite.setMaxPeople(Integer.valueOf(params.get("maxPeople")));
            } catch (Exception ignore) {}
        }

        redirectAttributes.addFlashAttribute("success", "캠프사이트가 수정되었습니다.");
        return "redirect:/console/campsites";
    }
}


