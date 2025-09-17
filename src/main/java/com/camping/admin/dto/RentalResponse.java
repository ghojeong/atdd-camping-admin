package com.camping.admin.dto;

import com.camping.admin.domain.entity.RentalRecord;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class RentalResponse {
    private Long id;
    private Long reservationId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Boolean isReturned;
    private LocalDateTime createdAt;

    public static RentalResponse from(RentalRecord rentalRecord) {
        return new RentalResponse(rentalRecord);
    }

    private RentalResponse(RentalRecord rentalRecord) {
        this.id = rentalRecord.getId();
        this.reservationId = rentalRecord.getReservation() != null ? rentalRecord.getReservation().getId() : null;
        this.productId = rentalRecord.getProduct().getId();
        this.productName = rentalRecord.getProduct().getName();
        this.quantity = rentalRecord.getQuantity();
        this.isReturned = rentalRecord.getIsReturned();
        this.createdAt = rentalRecord.getCreatedAt();
    }
}