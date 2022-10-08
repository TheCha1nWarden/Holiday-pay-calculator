package com.my.calculator.repository;

import com.my.calculator.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    long countByDateBetween(Date firstDate, Date endDate);

}
