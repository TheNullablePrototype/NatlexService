package com.prototype.natlexservice.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class GeoClass {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String code;

    public GeoClass(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof GeoClass other))
            return false;

        return id != null &&
                id.equals(other.getId());
    }

}
