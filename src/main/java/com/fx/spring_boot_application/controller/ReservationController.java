package com.fx.spring_boot_application.controller;

import com.fx.spring_boot_application.dto.Reservation;
import com.fx.spring_boot_application.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.NoSuchObjectException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/{id}")
    public Reservation getReservationById(
        @PathVariable Long id
    ) throws NoSuchObjectException {
        return reservationService.getReservationById(id);
    }

    @GetMapping("/all")
    public List<Reservation> getAllReservation(){
        return reservationService.findAllReservation();
    }
}
