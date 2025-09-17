package com.camping.admin.web;

import com.camping.admin.dto.ReservationResponse;
import com.camping.admin.repository.ReservationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/console/reservations")
public class ConsoleReservationController {

    private final ReservationRepository reservationRepository;

    public ConsoleReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public String list(Model model) {
        List<ReservationResponse> response = reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());

        model.addAttribute("reservations", response);
        return "reservations/list";
    }

    @GetMapping("/new")
    public String createForm() {
        return "reservations/form";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id) {
        return "reservations/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id) {
        return "reservations/form";
    }
}


