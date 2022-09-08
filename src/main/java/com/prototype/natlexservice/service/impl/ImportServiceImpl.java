package com.prototype.natlexservice.service.impl;

import com.prototype.natlexservice.enums.ProgressType;
import com.prototype.natlexservice.service.ImportService;
import com.prototype.natlexservice.service.SectionService;
import com.prototype.natlexservice.service.TaskHandler;
import com.prototype.natlexservice.service.impl.task.FileImportTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Optional;

@Service
public class ImportServiceImpl implements ImportService {

    private final SectionService sectionService;
    private final TaskHandler taskHandler;

    @Autowired
    public ImportServiceImpl(SectionService sectionService, TaskHandler taskHandler) {
        this.sectionService = sectionService;
        this.taskHandler = taskHandler;
    }

    @Override
    public int importFile(Path path) {
        final var task = new FileImportTask(sectionService, path);
        final var id = this.taskHandler.addTask(task);
        task.setName(String.format("file-import-task-%s-%d", path.getFileName().toString(), id));
        return id;
    }

    @Override
    public Optional<ProgressType> getImportFileStatus(int id) {
        return this.taskHandler.getTaskProgressById(id);
    }

}
