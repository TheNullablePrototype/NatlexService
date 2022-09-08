package com.prototype.natlexservice.service;

import com.prototype.natlexservice.model.Section;

import java.util.List;

public interface SectionService {

    List<Section> getSections();

    List<Section> getSectionsByCode(String code);

    void saveAll(List<Section> sections);

}
