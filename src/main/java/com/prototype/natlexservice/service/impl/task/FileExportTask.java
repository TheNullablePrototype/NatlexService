package com.prototype.natlexservice.service.impl.task;

import com.prototype.natlexservice.helper.ExcelHelper;
import com.prototype.natlexservice.model.Section;
import lombok.Getter;

import java.nio.file.Path;
import java.util.List;

public final class FileExportTask extends ProgressedTask<Path> {

    @Getter
    private Path path;

    public FileExportTask(List<Section> sections) {
        super(() -> ExcelHelper.write(sections));
    }

    @Override
    public void onSuccess(Path result) {
        super.onSuccess(result);
        this.path = result;
    }

}
