package com.prototype.natlexservice.service.impl.task;

import com.prototype.natlexservice.helper.ExcelHelper;
import com.prototype.natlexservice.model.Section;
import com.prototype.natlexservice.service.SectionService;

import java.nio.file.Path;
import java.util.List;

public final class FileImportTask extends ProgressedTask<List<Section>> {

    private final SectionService sectionService;

    public FileImportTask(SectionService sectionService, Path path) {
        super(() -> ExcelHelper.read(path));
        this.sectionService = sectionService;
    }

    @Override
    public void onSuccess(List<Section> result) {
        this.sectionService.saveAll(result);
        super.onSuccess(result);
    }

}
