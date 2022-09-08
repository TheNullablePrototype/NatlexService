package com.prototype.natlexservice.repository;

import com.prototype.natlexservice.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findByClassesCode(@NonNull String code);

}
