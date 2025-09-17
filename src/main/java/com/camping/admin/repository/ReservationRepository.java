package com.camping.admin.repository;

import com.camping.admin.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByCampsiteId(Long campsiteId);

    @Query("SELECT r FROM Reservation r WHERE r.campsite.id = :campsiteId " +
           "AND r.status != 'CANCELLED' " +
           "AND (r.startDate < :endDate AND r.endDate > :startDate)")
    List<Reservation> findOverlappingReservations(
        @Param("campsiteId") Long campsiteId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    List<Reservation> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

}