package com.my.calculator.controller;

import com.my.calculator.service.CalculatorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/calculator")
@AllArgsConstructor
public class CalculatorController {

    private final CalculatorService calculatorService;

    @GetMapping("/calculacte")
    public Double calculate(@RequestParam Double avgSalary,
                            @RequestParam(required = false) Integer amountOfDays,
                            @RequestParam(required = false) String firstDay,
                            @RequestParam(required = false) String lastDay) {
        Double output;
        if (amountOfDays == null) {
            output = calculatorService.calculateHolidayPayByFirstAndLastDays(avgSalary, firstDay, lastDay);
        } else {
            if (firstDay == null && lastDay == null) {
                output = calculatorService.calculateHolidayPayByAmountOfDays(avgSalary, amountOfDays);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
        return output;
    }

}
