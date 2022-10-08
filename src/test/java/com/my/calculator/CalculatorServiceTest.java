package com.my.calculator;

import com.my.calculator.repository.HolidayRepository;
import com.my.calculator.service.CalculatorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class CalculatorServiceTest {

    @Mock
    private HolidayRepository holidayRepository;
    private CalculatorService calculatorService;

    @BeforeEach
    public void init() {
        calculatorService = new CalculatorService(holidayRepository);
    }

    @ParameterizedTest
    @MethodSource("correctDataForTestCalculateHolidayPayByFirstAndLastDays")
    public void testCalculateHolidayPayByFirstAndLastDaysWithCorrectData(Double expected, Double avgSalary,
                                                                         String firstDay, String lastDay,
                                                                         long amountOfHolidays) {
        Assertions.assertNotNull(holidayRepository);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date firstDate = simpleDateFormat.parse(firstDay);
            Date lastDate = simpleDateFormat.parse(lastDay);
            Mockito.when(holidayRepository
                    .countByDateBetween(firstDate, lastDate)).thenReturn(amountOfHolidays);
            Assertions.assertEquals(expected, calculatorService
                    .calculateHolidayPayByFirstAndLastDays(avgSalary, firstDay, lastDay));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @MethodSource("incorrectDataForTestCalculateHolidayPayByFirstAndLastDays")
    public void testCalculateHolidayPayByFirstAndLastDaysWithIncorrectData(Double avgSalary,
                                                                           String firstDay,
                                                                           String lastDay) {
        Assertions.assertThrows(ResponseStatusException.class,
                () -> calculatorService.calculateHolidayPayByFirstAndLastDays(avgSalary, firstDay, lastDay));
    }

    @ParameterizedTest
    @MethodSource("dataForTestCalculateHolidayPayByAmountOfDays")
    public void testCalculateHolidayPayByAmountOfDays(Double expected, Double avgSalary, Integer amountOfHolidays) {
        Assertions.assertEquals(expected, calculatorService
                .calculateHolidayPayByAmountOfDays(avgSalary, amountOfHolidays));
    }

    public static Stream<Arguments> correctDataForTestCalculateHolidayPayByFirstAndLastDays() {
        return Stream.of(Arguments.arguments(23890.78, 50000.0, "2022-03-01", "2022-03-14", 0),
                Arguments.arguments(62528.97, 65432.1, "2022-07-07", "2022-08-03", 0),
                Arguments.arguments(13139.93, 55000.0, "2021-12-30", "2022-01-14", 9),
                Arguments.arguments(1877.13, 55000.0, "2022-01-01", "2022-01-10", 9),
                Arguments.arguments(18771.33, 50000.0, "2022-06-01", "2022-06-14", 3),
                Arguments.arguments(1706.48, 50000.0, "2022-06-05", "2022-06-05", 0)
        );
    }

    public static Stream<Arguments> incorrectDataForTestCalculateHolidayPayByFirstAndLastDays() {
        return Stream.of(Arguments.arguments(50000.0, "2022-02-01", "2022-02-29"),
                Arguments.arguments(65432.1, "2022-08-07", "2022-08-03"),
                Arguments.arguments(55000.0, "2021-12-30", "2022-0114"),
                Arguments.arguments(55000.0, "2022-01-01", null),
                Arguments.arguments(50000.0, null, null)
        );
    }

    public static Stream<Arguments> dataForTestCalculateHolidayPayByAmountOfDays() {
        return Stream.of(Arguments.arguments(22331.77, 65432.1, 10),
                Arguments.arguments(62528.97, 65432.1, 28),
                Arguments.arguments(11945.39, 50000.0, 7),
                Arguments.arguments(3412.97, 100000.0, 1),
                Arguments.arguments(0.0, 50000.0, 0)
        );
    }

}
