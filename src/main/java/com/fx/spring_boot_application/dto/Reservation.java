package com.fx.spring_boot_application.dto;

import com.fx.spring_boot_application.entity.ReservationEntity;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;

public record Reservation(
    @Null
    Long id,
    @NotNull
    Long userId,
    @NotNull
    Long roomId,
    @FutureOrPresent
    @NotNull
    LocalDate startDate,
    @Future
    @NotNull
    LocalDate endDate,
    ReservationStatus reservationStatus
) {
    public static Reservation copyChangeIdAndStatus(Long id, ReservationStatus status, Reservation reservation) {
        return new Reservation(
            id,
            reservation.userId(),
            reservation.roomId(),
            reservation.startDate(),
            reservation.endDate(),
            status
        );
    }

    public static Reservation update(Long id, ReservationStatus status, Reservation reservation) {
        return new Reservation(
            id,
            reservation.userId(),
            reservation.roomId(),
            reservation.startDate(),
            reservation.endDate(),
            status
        );
    }

    public Reservation approve() {
        return new Reservation(
            this.id,
            this.userId(),
            this.roomId(),
            this.startDate(),
            this.endDate(),
            ReservationStatus.APPROVED
        );
    }

    public static Reservation reservationFromEntity(ReservationEntity reservation) {
        return new Reservation(
            reservation.getId(),
            reservation.getUserId(),
            reservation.getRoomId(),
            reservation.getStartDate(),
            reservation.getEndDate(),
            reservation.getReservationStatus()
        );
    }
}
