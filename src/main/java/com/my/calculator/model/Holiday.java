package com.my.calculator.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "holidays")
@NoArgsConstructor
@AllArgsConstructor
public class Holiday {

    @Id
    @GeneratedValue
    Long id;

    @Temporal(TemporalType.DATE)
    Date date;

    public Holiday(Date date) {
        this.date = date;
    }

}
