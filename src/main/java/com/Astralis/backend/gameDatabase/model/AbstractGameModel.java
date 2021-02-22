package com.Astralis.backend.gameDatabase.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EqualsAndHashCode
@Getter
@Setter
@ToString
public abstract class AbstractGameModel {
    @Id
    @GeneratedValue
    protected Long id;
}
