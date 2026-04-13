package com.fx.spring_boot_application.repository;

import com.fx.spring_boot_application.dto.ReservationStatus;
import com.fx.spring_boot_application.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    @Query("SELECT r FROM ReservationEntity r " +
        "WHERE r.id != :id " +
        "AND r.roomId = :roomId " +
        "AND r.reservationStatus = :status AND (" +
        ":endDate BETWEEN r.startDate AND r.endDate " +
        "OR :startDate BETWEEN r.startDate AND r.endDate" +
        ")")
    List<ReservationEntity> findConflictingReservations(
        @Param("id") Long id,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("roomId") Long roomId,
        @Param("status") ReservationStatus status
    );
}
