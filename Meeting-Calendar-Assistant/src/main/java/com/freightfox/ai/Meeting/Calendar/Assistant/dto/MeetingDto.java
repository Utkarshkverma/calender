package com.freightfox.ai.Meeting.Calendar.Assistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Meeting Dto",
        description = "Dto Representing the meeting time"

)
public class MeetingDto {
    @Schema(
            description = "Time representing the starting time of the meeting",
            example = "2024-09-12T19:19"
    )
    private LocalDateTime startTime;
    @Schema(
            description = "Time representing the ending time of the meeting",
            example = "2024-09-12T19:19"
    )
    private LocalDateTime endTime;
}
