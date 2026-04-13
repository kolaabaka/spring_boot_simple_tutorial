package com.fx.spring_boot_application.entity;

import com.fx.spring_boot_application.dto.Reservation;
import com.fx.spring_boot_application.dto.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "reservation_status")
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    public ReservationEntity(Long userId, ReservationStatus reservationStatus, LocalDate endDate, LocalDate startDate, Long roomId) {
        this.userId = userId;
        this.reservationStatus = reservationStatus;
        this.endDate = endDate;
        this.startDate = startDate;
        this.roomId = roomId;
    }

    public static ReservationEntity reservationFromDto(Reservation reservation, ReservationStatus status){
        return new ReservationEntity(
            reservation.userId(),
            status,
            reservation.startDate(),
            reservation.endDate(),
            reservation.roomId()
        );
    }
}
