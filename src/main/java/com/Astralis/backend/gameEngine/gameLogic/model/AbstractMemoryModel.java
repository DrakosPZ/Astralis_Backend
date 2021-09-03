package com.Astralis.backend.gameEngine.gameLogic.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractMemoryModel implements Serializable {
    protected Long id;
}
