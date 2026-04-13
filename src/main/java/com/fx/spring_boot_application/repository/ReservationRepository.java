package com.fx.spring_boot_application.repository;

import com.fx.spring_boot_application.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    @Query("select r " +
        "from ReservationEntity r " +
        "where r.id != ?1 and (r.endDate >= ?2 or r.startDate <= ?3) and r.roomId = ?4")
    List<ReservationEntity> findByIdAndDate(Long id, LocalDate startDate, LocalDate endDate, Long roomId);
}
