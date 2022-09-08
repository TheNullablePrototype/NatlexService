package com.prototype.natlexservice.http.response.section;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Value
public class GeoClassResponse {

    String name;
    String code;

}
