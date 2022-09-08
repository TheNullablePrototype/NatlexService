package com.prototype.natlexservice.repository;

import com.prototype.natlexservice.model.GeoClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoGlassRepository extends JpaRepository<GeoClass, Long> {


}
