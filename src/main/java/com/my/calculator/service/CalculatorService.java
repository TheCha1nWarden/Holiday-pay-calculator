package com.my.calculator.service;

import com.my.calculator.repository.HolidayRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.Locale;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@AllArgsConstructor
public class CalculatorService {

    private static final String DATE_TIME_FORMAT = "uuuu-MM-dd";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String ILLEGAL_ARGUMENT_EXCEPTION_MSG = "lastDay cant be before firstDay!";
    private static final String RESPONSE_STATUS_EXCEPTION_MSG = "incorrect input data";
    private static final double AVG_AMOUNT_OF_DAY_IN_A_MONTH = 29.3;

    private final HolidayRepository holidayRepository;

    public Double calculateHolidayPayByAmountOfDays(Double avgSalary, Integer amountOfDays) {
        Double output = avgSalary / AVG_AMOUNT_OF_DAY_IN_A_MONTH * amountOfDays;
        output = Math.round(output * 100) / 100.0;
        return output;
    }


    public Double calculateHolidayPayByFirstAndLastDays(Double avgSalary, String firstDay, String lastDay) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                    .ofPattern(DATE_TIME_FORMAT, Locale.getDefault())
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate firstLocalDate = LocalDate.parse(firstDay, dateTimeFormatter);
            LocalDate lastLocalDate = LocalDate.parse(lastDay, dateTimeFormatter);
            long amountOfDays = DAYS.between(firstLocalDate, lastLocalDate) + 1;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            Date firstDate = simpleDateFormat.parse(firstDay);
            Date lastDate = simpleDateFormat.parse(lastDay);
            if (firstDate.after(lastDate)) {
                throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MSG);
            }
            long amountOfHolidays = holidayRepository.countByDateBetween(firstDate, lastDate);
            Double output = avgSalary / AVG_AMOUNT_OF_DAY_IN_A_MONTH * (amountOfDays - amountOfHolidays);
            output = Math.round(output * 100) / 100.0;
            return output;
        } catch (ParseException | IllegalArgumentException | DateTimeException | NullPointerException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, RESPONSE_STATUS_EXCEPTION_MSG, e);
        }
    }

}
