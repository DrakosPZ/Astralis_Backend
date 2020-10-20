package com.Astralis.logic.model;

import com.Astralis.backend.model.AbstractModel;
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
public class Ship extends AbstractModel {
    private Position targetPosition;
    private Position currentPosition;
}
