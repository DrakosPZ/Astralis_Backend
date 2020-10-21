package com.Astralis.logic.model;

import com.Astralis.backend.model.AbstractModel;
import lombok.*;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LogicGameState extends AbstractModel {
    //#possibly put into own class ffs
    private int year;
    private int month;
    private int day;
    private int hour;

    private List<Country> countries;
}
