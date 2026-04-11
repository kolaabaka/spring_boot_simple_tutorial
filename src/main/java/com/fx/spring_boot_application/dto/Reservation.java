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
}
