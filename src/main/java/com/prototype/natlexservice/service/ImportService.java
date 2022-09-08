package com.prototype.natlexservice.service;

import com.prototype.natlexservice.enums.ProgressType;

import java.nio.file.Path;
import java.util.Optional;

public interface ImportService {

    int importFile(Path path);

    Optional<ProgressType> getImportFileStatus(int id);

}
