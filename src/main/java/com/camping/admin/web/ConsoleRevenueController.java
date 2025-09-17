package com.camping.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/console/revenue")
public class ConsoleRevenueController {

    @GetMapping("/daily")
    public String daily() {
        return "revenue/daily";
    }
}


