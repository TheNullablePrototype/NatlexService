package com.prototype.natlexservice.http.response.upload;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Value
public class UploadFileResponse {

    int id;

}
