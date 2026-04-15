package com.fx.spring_boot_application.controller;

import com.fx.spring_boot_application.dto.Reservation;
import com.fx.spring_boot_application.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.NoSuchObjectException;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(
        @PathVariable Long id
    ) throws NoSuchObjectException {
        var response = reservationService.getReservationById(id);

        return ResponseEntity
            .ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservation() {
        var respone = reservationService.findAllReservation();

        return ResponseEntity
            .ok(respone);
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody @Valid Reservation reservation) {
        var response = reservationService.createReservation(reservation);

        return ResponseEntity
            .status(201)
            .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id,
                                                         @RequestBody @Valid Reservation reservation) {
        var response = reservationService.updateReservation(id, reservation);

        return ResponseEntity
            .ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);

        return ResponseEntity
            .status(201)
            .build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Reservation> approveReservation(@PathVariable Long id) {
        var response = reservationService.approveReservation(id);
        return ResponseEntity
            .ok(response);
    }
}
