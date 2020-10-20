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
public class GameMap extends AbstractModel {
    private Galaxy galaxy;
}
