package com.prototype.natlexservice.contoller;

import com.prototype.natlexservice.config.AuthProps;
import com.prototype.natlexservice.exception.NotAuthorizationException;
import com.prototype.natlexservice.http.response.section.SectionResponse;
import com.prototype.natlexservice.service.SectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Slf4j
@RequiredArgsConstructor
public class SectionsController {

    private final SectionService sectionService;
    private final AuthProps props;

    @GetMapping
    @RequestMapping("/sections")
    public ResponseEntity<List<SectionResponse>> getSections(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {

        if (!Objects.equals(this.props.token(), token)) {
            throw new NotAuthorizationException();
        }

        logger.info("handle get sections");

        final var sections = this.sectionService.getSections();
        if (sections.isEmpty())
            return ResponseEntity.notFound().build();

        final var responses = sections.stream().map(SectionResponse::from).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping
    @RequestMapping("/sections/by-code")
    public ResponseEntity<List<SectionResponse>> getSectionsByCode(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
            @RequestParam(value = "code") String code) {

        if (!Objects.equals(this.props.token(), token)) {
            throw new NotAuthorizationException();
        }

        logger.info("handle get sections by code {}", code);
        final var sections = this.sectionService.getSectionsByCode(code);

        if (sections.isEmpty())
            return ResponseEntity.notFound().build();

        final var responses = sections.stream().map(SectionResponse::from).toList();
        return ResponseEntity.ok(responses);
    }

}
