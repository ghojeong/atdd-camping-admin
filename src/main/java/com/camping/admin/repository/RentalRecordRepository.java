package com.camping.admin.repository;

import com.camping.admin.domain.entity.RentalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRecordRepository extends JpaRepository<RentalRecord, Long> {
    List<RentalRecord> findByReservationId(Long reservationId);

    List<RentalRecord> findByProductId(Long productId);

    List<RentalRecord> findByIsReturned(Boolean isReturned);

    @Query("SELECT rr FROM RentalRecord rr " +
           "LEFT JOIN rr.reservation r " +
           "WHERE rr.product.id = :productId " +
           "AND rr.isReturned = false")
    List<RentalRecord> findActiveRentalsByProductId(@Param("productId") Long productId);
}