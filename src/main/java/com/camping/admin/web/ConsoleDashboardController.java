package com.camping.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/console")
public class ConsoleDashboardController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard/index";
    }
}


