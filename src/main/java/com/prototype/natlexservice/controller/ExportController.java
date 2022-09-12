package com.prototype.natlexservice.controller;

import com.prototype.natlexservice.config.AuthProps;
import com.prototype.natlexservice.enums.ProgressType;
import com.prototype.natlexservice.exception.NotAuthorizationException;
import com.prototype.natlexservice.helper.FileHelper;
import com.prototype.natlexservice.http.response.export.ExportFileStatusResponse;
import com.prototype.natlexservice.service.ExportService;
import com.prototype.natlexservice.service.SectionService;
import com.prototype.natlexservice.service.TaskHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
public class ExportController {

    private final SectionService sectionService;
    private final ExportService exportService;
    private final TaskHandler taskHandler;

    private final AuthProps props;

    @GetMapping("/api/export/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ExportFileStatusResponse> handleExportFile(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {

        if (!Objects.equals(this.props.token(), token)) {
            throw new NotAuthorizationException();
        }

        final var sections = this.sectionService.getSections();
        final var id = this.exportService.exportFile(sections);
        logger.info("handle export file, added task to export with id: {}", id);
        return ResponseEntity.ok(ExportFileStatusResponse.builder()
                .id(id)
                .type(ProgressType.IN_PROGRESS)
                .build()
        );
    }

    @GetMapping("/api/export/{id}")
    public ResponseEntity<ExportFileStatusResponse> getExportFileStatus(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable int id) {

        if (!Objects.equals(this.props.token(), token)) {
            throw new NotAuthorizationException();
        }

        logger.info("handle get file export status with id: {}", id);
        final var status = this.exportService.getExportFileStatus(id);
        return status.map(type -> ResponseEntity.ok(ExportFileStatusResponse.builder()
                        .id(id)
                        .type(type)
                        .build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/api/export/file/{id}")
    public ResponseEntity<ByteArrayResource> getExportedFile(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
            @PathVariable int id) {

        if (!Objects.equals(this.props.token(), token)) {
            throw new NotAuthorizationException();
        }

        logger.info("handle get exported file by id: {}", id);
        final var exportedFile = this.exportService.getExportedFile(id);
        if (exportedFile.isPresent()) {
            final var path = exportedFile.get();
            final var bytes = FileHelper.readFile(path);
            this.taskHandler.removeTaskById(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", path.getFileName().toString()))
                    .body(new ByteArrayResource(bytes));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
