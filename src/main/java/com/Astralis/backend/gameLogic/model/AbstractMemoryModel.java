package com.Astralis.backend.gameLogic.model;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@EqualsAndHashCode
@Getter
@Setter
@ToString
public abstract class AbstractMemoryModel {
    protected Long id;
}
