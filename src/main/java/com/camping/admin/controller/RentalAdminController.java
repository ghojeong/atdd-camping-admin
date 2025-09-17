package com.camping.admin.controller;

import com.camping.admin.dto.CreateRentalRequest;
import com.camping.admin.dto.RentalResponse;
import com.camping.admin.service.RentalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/rentals")
@RequiredArgsConstructor
public class RentalAdminController {

    private final RentalService rentalService;

    @GetMapping
    public ResponseEntity<List<RentalResponse>> getAllRentals() {
        List<RentalResponse> rentals = rentalService.findAll();
        return ResponseEntity.ok(rentals);
    }

    @PostMapping
    public ResponseEntity<RentalResponse> createRental(@RequestBody CreateRentalRequest request) {
        RentalResponse createdRental = rentalService.createRental(
                request.getProductId(),
                request.getQuantity(),
                request.getReservationId()
        );
        return new ResponseEntity<>(createdRental, HttpStatus.CREATED);
    }

    @PatchMapping("/{rentalRecordId}/return")
    public ResponseEntity<RentalResponse> markAsReturned(@PathVariable Long rentalRecordId) {
        RentalResponse updatedRental = rentalService.markAsReturned(rentalRecordId);
        return ResponseEntity.ok(updatedRental);
    }
}