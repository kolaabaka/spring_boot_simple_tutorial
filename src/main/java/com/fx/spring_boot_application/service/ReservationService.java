package com.fx.spring_boot_application.service;

import com.fx.spring_boot_application.dto.Reservation;
import com.fx.spring_boot_application.dto.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

import static com.fx.spring_boot_application.dto.Reservation.copyChangeIdAndStatus;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final Map<Long, Reservation> reservationMap = new HashMap<>();
    private final AtomicLong id = new AtomicLong(1);

    {
        reservationMap.put(1L, new Reservation(
            1L, 13L, 2L, LocalDate.now(),
            LocalDate.now().plusDays(2), ReservationStatus.PENDING
        ));
    }

    public Reservation getReservationById(Long id) throws NoSuchObjectException {
        if (!reservationMap.containsKey(id)) {
            throw new NoSuchObjectException("No such reservation in container");
        }
        return reservationMap.get(id);
    }

    public List<Reservation> findAllReservation() {
        return reservationMap.keySet().stream().map(reservationMap::get).toList();
    }

    public Reservation createReservation(Reservation reservation) {
        if (reservation.id() != null) {
            throw new IllegalArgumentException("ID should be empty");
        }
        if (reservation.reservationStatus() != null) {
            throw new IllegalArgumentException("Status should be empty");
        }

        var reservationToSave = copyChangeIdAndStatus(id.get(), ReservationStatus.PENDING, reservation);
        reservationMap.put(id.get(), reservationToSave);
        id.incrementAndGet();
        return reservationToSave;
    }

    public Reservation updateReservation(Long id, Reservation reservation) {
        if (!reservationMap.containsKey(id)) {
            throw new NoSuchElementException(String.format("No reservation with %s", id));
        }

        var reservationFromBd = reservationMap.get(id);
        if (reservationFromBd.reservationStatus().equals(ReservationStatus.APPROVED)
            || reservationFromBd.reservationStatus().equals(ReservationStatus.CANCELED)) {
            throw new IllegalStateException(String.format("Reservation in status %s cannot be update",
                reservationFromBd.reservationStatus()));
        }


        reservationMap.put(id, copyChangeIdAndStatus(id, ReservationStatus.PENDING, reservation));
        return reservation;
    }

    public void deleteReservation(Long id) {
        if (!reservationMap.containsKey(id)) {
            throw new NoSuchElementException(String.format("No reservation with %s", id));
        }
        reservationMap.remove(id);
    }

    public Reservation approveReservation(Long id) {
        if (!reservationMap.containsKey(id)) {
            throw new NoSuchElementException(String.format("No reservation with %s", id));
        }
        var reservationFromBd = reservationMap.get(id);

        if (!reservationFromBd.reservationStatus().equals(ReservationStatus.PENDING)) {
            throw new IllegalStateException(String.format("Reservation in status %s cannot be update",
                reservationFromBd.reservationStatus()));
        }

        if (isReservationConflict(reservationFromBd)) {
            throw new IllegalStateException("Reservation date conflict");
        }

        var reservationNewApproved = new Reservation(
            id,
            reservationFromBd.userId(),
            reservationFromBd.roomId(),
            reservationFromBd.startDate(),
            reservationFromBd.endDate(),
            ReservationStatus.APPROVED
        );

        reservationMap.put(id, reservationNewApproved);
        return reservationNewApproved;
    }

    private boolean isReservationConflict(Reservation reservation) {
        return reservationMap.values().stream()
            .filter(r -> r.roomId().equals(reservation.roomId())
                && r.reservationStatus().equals(ReservationStatus.APPROVED)
                && !r.id().equals(reservation.id()))
            .anyMatch(r -> r.startDate().isBefore(reservation.endDate())
                && r.endDate().isAfter(reservation.startDate()));
    }
}
