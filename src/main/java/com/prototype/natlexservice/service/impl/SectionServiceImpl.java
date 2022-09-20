package com.prototype.natlexservice.service.impl;

import com.prototype.natlexservice.model.Section;
import com.prototype.natlexservice.repository.SectionRepository;
import com.prototype.natlexservice.service.SectionService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Value
@NonFinal
public class SectionServiceImpl implements SectionService {

    SectionRepository repository;

    @Override
    public List<Section> getSections() {
        return this.repository.findAll();
    }

    @Override
    public List<Section> getSectionsByCode(String code) {
        return this.repository.findByClassesCode(code);
    }

    @Override
    public void saveAll(List<Section> sections) {
        this.repository.saveAll(sections);
    }

}
