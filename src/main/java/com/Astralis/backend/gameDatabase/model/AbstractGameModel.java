package com.Astralis.backend.gameDatabase.model;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EqualsAndHashCode
@Getter
@Setter
@ToString
@NoArgsConstructor
public abstract class AbstractGameModel {
    @Id
    @GeneratedValue
    protected Long id;

    public AbstractGameModel(Long id) {
        super();
        this.id = id < 0 ?  0 : id;
    }
}
