package com.prototype.natlexservice.http.response.section;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prototype.natlexservice.model.Section;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Builder
@Value
public class SectionResponse {

    String name;

    @JsonProperty("geologicalClasses")
    List<GeoClassResponse> classes;

    public static SectionResponse from(Section section) {
        final var classes = section.getClasses()
                .stream()
                .map(clazz -> GeoClassResponse.builder()
                        .name(clazz.getName())
                        .code(clazz.getCode())
                        .build())
                .toList();

        return SectionResponse.builder()
                .name(section.getName())
                .classes(classes)
                .build();
    }

}
