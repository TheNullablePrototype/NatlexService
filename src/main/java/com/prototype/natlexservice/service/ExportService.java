package com.prototype.natlexservice.service;

import com.prototype.natlexservice.enums.ProgressType;
import com.prototype.natlexservice.model.Section;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface ExportService {

    int exportFile(List<Section> sections);

    Optional<ProgressType> getExportFileStatus(int id);

    Optional<Path> getExportedFile(int id);
}
