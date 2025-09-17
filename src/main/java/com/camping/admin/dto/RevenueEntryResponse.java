package com.camping.admin.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RevenueEntryResponse {

    public enum EntryType { RESERVATION, SALE, RENTAL }

    private EntryType type;
    private String title;
    private BigDecimal amount;
    private LocalDateTime occurredAt;
}


