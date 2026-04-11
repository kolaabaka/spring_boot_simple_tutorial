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

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final Map<Long, Reservation> reservationMap = Map.of(
        1L, new Reservation(40L, 1L, 14L, LocalDate.now(),
            LocalDate.now().plusDays(2), ReservationStatus.APPROVED),
        2L, new Reservation(40L, 2L, 14L, LocalDate.now(),
            LocalDate.now().plusDays(2), ReservationStatus.APPROVED)
    );

    public Reservation getReservationById(Long id) throws NoSuchObjectException {
        if (!reservationMap.containsKey(id)) {
            throw new NoSuchObjectException("");
        }
        return reservationMap.get(id);
    }

    public List<Reservation> findAllReservation() {
        return reservationMap.keySet().stream().map(reservationMap::get).toList();
    }
}
