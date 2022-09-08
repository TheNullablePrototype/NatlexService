package com.prototype.natlexservice.service.impl;

import com.prototype.natlexservice.model.Section;
import com.prototype.natlexservice.repository.SectionRepository;
import com.prototype.natlexservice.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SectionServiceImpl implements SectionService {

    private final SectionRepository repository;

    @Autowired
    public SectionServiceImpl(SectionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Section> getSections() {
        return this.repository.findAll();
    }

    @Override
    public List<Section> getSectionsByCode(String code) {
        return this.repository.findByClassesCode(code);
    }

    @Override
    @Transactional
    public void saveAll(List<Section> sections) {
        this.repository.saveAll(sections);
    }

}
