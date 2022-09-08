package com.prototype.natlexservice.model;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Section {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Fetch(FetchMode.JOIN)
    @OneToMany(cascade = CascadeType.ALL)
    private List<GeoClass> classes;

    public Section(String name) {
        this.name = name;
        this.classes = new ArrayList<>();
    }

    public void addGeoClass(GeoClass geoClass) {
        this.classes.add(geoClass);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Section other))
            return false;

        return id != null &&
                id.equals(other.getId());
    }

}
