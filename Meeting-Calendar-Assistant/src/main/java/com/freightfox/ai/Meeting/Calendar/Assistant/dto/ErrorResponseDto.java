package com.freightfox.ai.Meeting.Calendar.Assistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "Schema to hold error response information"
)
public class ErrorResponseDto {
    @Schema(
            description = "API path invoked by the client",
            example = "/api/create"
    )
    private String apiPath;
    @Schema(
            description = "Error code representing the error happened",
            example = "BAD_REQUEST"
    )
    private HttpStatus status;
    @Schema(
            description = "Error message representing the error happened",
            example = "Employee not found with the given name"
    )
    private String errorMessage;
    @Schema(
            description = "Time representing when the error happened",
            example = "2024-09-12T19:19:48.4453257"
    )
    private LocalDateTime errorTime;

}
