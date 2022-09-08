package com.prototype.natlexservice.http.response.upload;

import com.prototype.natlexservice.enums.ProgressType;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Value
public class ImportFileStatusResponse {

    int id;
    ProgressType type;

}
