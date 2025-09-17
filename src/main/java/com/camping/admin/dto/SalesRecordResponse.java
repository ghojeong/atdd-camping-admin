package com.camping.admin.dto;

import com.camping.admin.domain.entity.SalesRecord;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SalesRecordResponse {
    private Long id;
    private String productName;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;

    public static SalesRecordResponse from(SalesRecord record) {
        return new SalesRecordResponse(record);
    }

    private SalesRecordResponse(SalesRecord record) {
        this.id = record.getId();
        this.productName = record.getProduct().getName();
        this.quantity = record.getQuantity();
        this.totalPrice = record.getTotalPrice();
        this.createdAt = record.getCreatedAt();
    }
}


