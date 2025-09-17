package com.camping.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRentalRequest {
    private Long reservationId; // Can be null for walk-ins
    private Long productId;
    private Integer quantity;
}