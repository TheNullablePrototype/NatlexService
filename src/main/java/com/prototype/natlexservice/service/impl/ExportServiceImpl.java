package com.prototype.natlexservice.service.impl;

import com.prototype.natlexservice.enums.ProgressType;
import com.prototype.natlexservice.exception.ExportFileNotReadyException;
import com.prototype.natlexservice.model.Section;
import com.prototype.natlexservice.service.ExportService;
import com.prototype.natlexservice.service.TaskHandler;
import com.prototype.natlexservice.service.impl.task.FileExportTask;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
@Value
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    TaskHandler taskHandler;

    @Override
    public int exportFile(List<Section> sections) {
        final var task = new FileExportTask(sections);
        final var id = this.taskHandler.addTask(task);
        task.setName(String.format("file-export-task-%d", id));
        return id;
    }

    @Override
    public Optional<ProgressType> getExportFileStatus(int id) {
        return this.taskHandler.getTaskProgressById(id);
    }

    @Override
    public Optional<Path> getExportedFile(int id) {
        var taskById = this.taskHandler.getTaskById(id);
        if (taskById.isPresent()) {
            var progressedTask = taskById.get();
            if (progressedTask instanceof FileExportTask task) {
                if (!task.isDone() || task.getProgressType() != ProgressType.DONE) {
                    throw new ExportFileNotReadyException("Exporting file is in process");
                }

                return Optional.of(task.getPath());
            }
        }
        return Optional.empty();
    }

}
