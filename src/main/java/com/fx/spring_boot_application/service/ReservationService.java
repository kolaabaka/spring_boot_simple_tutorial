package com.fx.spring_boot_application.service;

import com.fx.spring_boot_application.dto.Reservation;
import com.fx.spring_boot_application.dto.ReservationStatus;
import com.fx.spring_boot_application.entity.ReservationEntity;
import com.fx.spring_boot_application.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.NoSuchElementException;

import static com.fx.spring_boot_application.dto.Reservation.reservationFromEntity;
import static com.fx.spring_boot_application.dto.ReservationStatus.APPROVED;
import static com.fx.spring_boot_application.dto.ReservationStatus.CANCELED;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;

    public Reservation getReservationById(Long id) throws NoSuchObjectException {
        var find = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("No such reservation in container"));

        return reservationFromEntity(find);
    }

    public List<Reservation> findAllReservation() {
        var listEntities = repository.findAll();

        return listEntities.stream()
            .map(Reservation::reservationFromEntity).toList();
    }

    public Reservation createReservation(Reservation reservation) {
        if (reservation.reservationStatus() != null) {
            throw new IllegalArgumentException("Status should be empty");
        }
        if (reservation.endDate().isAfter(reservation.startDate())) {
            throw new IllegalArgumentException("End date must be after start date earlier 1 day");
        }

        var reservationEntity = ReservationEntity.reservationFromDto(reservation, ReservationStatus.PENDING);

        repository.save(reservationEntity);

        return Reservation.reservationFromEntity(reservationEntity);
    }

    public Reservation updateReservation(Long id, Reservation reservation) {
        if (reservation.endDate().isAfter(reservation.startDate())) {
            throw new IllegalArgumentException("End date must be after start date earlier 1 day");
        }
        var reservationToUpdate = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(String.format("No reservation with %s", id)));

        if (reservationToUpdate.getReservationStatus().equals(ReservationStatus.APPROVED)
            || reservationToUpdate.getReservationStatus().equals(CANCELED)) {
            throw new IllegalStateException(String.format("Reservation in status %s cannot be update",
                reservationToUpdate.getReservationStatus()));
        }

        var updatedReservation = new ReservationEntity(
            reservationToUpdate.getId(),
            reservation.userId(),
            reservation.roomId(),
            reservation.startDate(),
            reservation.endDate(),
            reservation.reservationStatus()
        );

        repository.save(updatedReservation);

        return Reservation.reservationFromEntity(updatedReservation);
    }

    public void deleteReservation(Long id) {
        var reservationFomDb = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("No reservation with %s", id)));
        if (reservationFomDb.getReservationStatus().equals(CANCELED) || reservationFomDb.getReservationStatus().equals(APPROVED)) {
            throw new IllegalStateException(String.format("Reservation in status %s cannot be delete",
                reservationFomDb.getReservationStatus()));
        }
        repository.deleteById(id);
    }

    public Reservation approveReservation(Long id) {
        var reservationFromBd = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(String.format("No reservation with %s", id)));

        if (!reservationFromBd.getReservationStatus().equals(ReservationStatus.PENDING)) {
            throw new IllegalStateException(String.format("Reservation in status %s cannot be update",
                reservationFromBd.getReservationStatus()));
        }

        if (!repository.findConflictingReservations(id, reservationFromBd.getStartDate(), reservationFromBd.getEndDate()
            , reservationFromBd.getRoomId(), ReservationStatus.APPROVED).isEmpty()) {
            throw new IllegalStateException("Reservation date conflict");
        }

        reservationFromBd.setReservationStatus(ReservationStatus.APPROVED);

        repository.save(reservationFromBd);
        return reservationFromEntity(reservationFromBd);
    }
}
