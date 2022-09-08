package com.prototype.natlexservice.service.impl;

import com.prototype.natlexservice.enums.ProgressType;
import com.prototype.natlexservice.service.ImportService;
import com.prototype.natlexservice.service.SectionService;
import com.prototype.natlexservice.service.TaskHandler;
import com.prototype.natlexservice.service.impl.task.FileImportTask;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Value
public class ImportServiceImpl implements ImportService {

    SectionService sectionService;
    TaskHandler taskHandler;

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
