package com.Astralis.logic.model;

import com.Astralis.backend.model.AbstractModel;
import com.Astralis.backend.model.User;
import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Country extends AbstractModel {
    private User leadingUser;
    private Ship ship;
    private String colour;
}
