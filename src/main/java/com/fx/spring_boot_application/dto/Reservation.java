package com.fx.spring_boot_application.dto;

import java.time.LocalDate;

public record Reservation(
    Long id,
    Long userId,
    Long roomId,
    LocalDate startDate,
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
}
