package com.prototype.natlexservice.contoller;

import com.prototype.natlexservice.config.AuthProps;
import com.prototype.natlexservice.exception.NotAuthorizationException;
import com.prototype.natlexservice.http.response.upload.ImportFileStatusResponse;
import com.prototype.natlexservice.http.response.upload.UploadFileResponse;
import com.prototype.natlexservice.service.ImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Slf4j
@RestController
@Validated
public class ImportController {

    private final ImportService importService;
    private final AuthProps props;

    @Autowired
    public ImportController(ImportService importService, AuthProps props) {
        this.importService = importService;
        this.props = props;
    }

    @PostMapping(path = "/api/import", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UploadFileResponse> handleImportFile(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
            @RequestParam(value = "file") MultipartFile file) {

        if (!Objects.equals(this.props.token(), token)) {
            throw new NotAuthorizationException();
        }

        logger.info("handle import file: {}", file.getOriginalFilename());

        //String path = StringUtils.cleanPath(file.getOriginalFilename());
        final var extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        if (!"xls".equals(extension)) {
            return ResponseEntity.badRequest().build();
        }

        Path upload;
        try {
            upload = Files.createTempFile("upload", '.' + extension);
            Files.copy(file.getInputStream(), upload, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Successfully copy multipart file to {}", upload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final var id = importService.importFile(upload);
        return ResponseEntity.ok(UploadFileResponse.builder().id(id).build());
    }

    @GetMapping("/api/import/{id}")
    public ResponseEntity<ImportFileStatusResponse> getImportFileStatus(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
            @PathVariable int id) {

        if (!Objects.equals(this.props.token(), token)) {
            throw new NotAuthorizationException();
        }

        logger.info("handle get import file status by id: {}", id);
        var status = this.importService.getImportFileStatus(id);
        return status.map(type -> ResponseEntity.ok(ImportFileStatusResponse.builder()
                        .id(id)
                        .type(type)
                        .build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
