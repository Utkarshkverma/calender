package com.freightfox.ai.Meeting.Calendar.Assistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Response",
        description = "Schema to hold successful response information"
)
public class ResponseDto {
    @Schema(
            description = "Status code in the response",
            example = "201"
    )
    private String statusCode;
    @Schema(
            description = "Status message in the response",
            example = "Employee added successfully"
    )
    private String message;
}
