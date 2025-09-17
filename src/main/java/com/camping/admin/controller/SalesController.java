package com.camping.admin.controller;

import com.camping.admin.dto.ProcessSaleRequest;
import com.camping.admin.dto.SalesRecordResponse;
import com.camping.admin.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales") // Public-facing API prefix
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    @PostMapping
    public ResponseEntity<Void> processSale(@RequestBody ProcessSaleRequest request) {
        salesService.processSale(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<java.util.List<SalesRecordResponse>> listSales() {
        java.util.List<SalesRecordResponse> records = salesService.findRecentSales(10);
        return ResponseEntity.ok(records);
    }
}