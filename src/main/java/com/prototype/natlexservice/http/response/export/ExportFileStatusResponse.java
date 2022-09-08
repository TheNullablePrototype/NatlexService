package com.prototype.natlexservice.http.response.export;

import com.prototype.natlexservice.enums.ProgressType;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Value
public class ExportFileStatusResponse {

    int id;
    ProgressType type;

}
