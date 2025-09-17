package com.camping.admin.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyRevenueReportResponse {
    private LocalDate date;
    private BigDecimal totalReservationRevenue;
    private BigDecimal totalSalesRevenue;
    private BigDecimal totalRentalRevenue;
    private BigDecimal grandTotalRevenue;
}