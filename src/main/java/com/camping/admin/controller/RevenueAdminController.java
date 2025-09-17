package com.camping.admin.controller;

import com.camping.admin.dto.DailyRevenueReportResponse;
import com.camping.admin.dto.RangeRevenueReportResponse;
import com.camping.admin.dto.RevenueEntryResponse;
import com.camping.admin.service.SalesService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/reports/revenue")
@RequiredArgsConstructor
public class RevenueAdminController {

    private final SalesService salesService;

    @GetMapping("/daily")
    public ResponseEntity<DailyRevenueReportResponse> getDailyRevenueReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailyRevenueReportResponse report = salesService.generateDailyRevenueReport(date);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/range")
    public ResponseEntity<RangeRevenueReportResponse> getRangeRevenueReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        RangeRevenueReportResponse report = salesService.generateRangeRevenueReport(from, to);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/range/entries")
    public ResponseEntity<java.util.List<RevenueEntryResponse>> getRangeRevenueEntries(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        java.util.List<RevenueEntryResponse> entries = salesService.generateRangeRevenueEntries(from, to);
        return ResponseEntity.ok(entries);
    }
}