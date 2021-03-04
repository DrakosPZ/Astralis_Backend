package com.Astralis.backend.gameLogic.model;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@ToString
public abstract class AbstractMemoryModel implements Serializable {
    protected Long id;
}
