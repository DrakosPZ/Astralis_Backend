package com.Astralis.backend.gameDatabase.model;

import com.Astralis.backend.accountManagement.model.AbstractModel;
import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
//@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GameMap extends AbstractGameModel {
    //private Galaxy galaxy;
}
