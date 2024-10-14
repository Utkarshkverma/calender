package com.freightfox.ai.Meeting.Calendar.Assistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Account",
        description = "Schema to create the employee"
)
public class EmployeeDto {
    @Schema(
            description = "Name of the employee",
            example = "Utkarsh"
    )
    private String name;

}
