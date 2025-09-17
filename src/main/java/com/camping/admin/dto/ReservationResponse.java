package com.camping.admin.dto;

import com.camping.admin.domain.entity.Reservation;
import java.time.LocalDate;

import lombok.Getter;

@Getter
public class ReservationResponse {
    private Long id;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String campsiteSiteNumber;
    private LocalDate reservationDate;

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation);
    }

    private ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.customerName = reservation.getCustomerName();
        this.startDate = reservation.getStartDate();
        this.endDate = reservation.getEndDate();
        this.status = reservation.getStatus();
        this.campsiteSiteNumber = reservation.getCampsite().getSiteNumber();
        this.reservationDate = reservation.getReservationDate();
    }
}