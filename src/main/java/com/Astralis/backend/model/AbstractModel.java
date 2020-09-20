package com.Astralis.backend.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@EqualsAndHashCode
@Getter
@Setter
@ToString
public abstract class AbstractModel {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    protected String identifier;
}
