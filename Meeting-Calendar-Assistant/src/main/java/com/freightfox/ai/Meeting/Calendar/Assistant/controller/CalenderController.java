package com.freightfox.ai.Meeting.Calendar.Assistant.controller;

import com.freightfox.ai.Meeting.Calendar.Assistant.dto.EmployeeDto;
import com.freightfox.ai.Meeting.Calendar.Assistant.dto.ErrorResponseDto;
import com.freightfox.ai.Meeting.Calendar.Assistant.dto.ResponseDto;
import com.freightfox.ai.Meeting.Calendar.Assistant.service.ICalenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/frightfox/api/calender")
@Tag(
        name = "REST API for calender application",
        description = "REST API to create employees, schedule meetings, check for free slots between two employees, conflicted participants during the meeting "
)
public class CalenderController {

    private final ICalenderService calenderService;

    @Operation(
            summary = "Add employee to the database",
            description = "This endpoint will allow you to add user to the database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Employee added successfully"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/employees")
    public ResponseEntity<ResponseDto> addEmployee(@RequestBody EmployeeDto employeeDto) {
        ResponseDto responseDto = calenderService.addEmployee(employeeDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDto);
    }

    @Operation(summary = "Book a meeting",
            description = "This endpoint allows you to book a meeting for a specified employee.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Meeting booked successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Meeting time conflicts with an existing meeting",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    ))
    })
    @PostMapping("/meetings")
    public ResponseEntity<ResponseDto> bookMeeting(
            @Parameter(description = "Name of the employee")
            @RequestParam String employeeName,
            @Parameter(description = "Start time of the meeting")
            @RequestParam LocalDateTime startTime
            ) {
        ResponseDto responseDto = calenderService.bookMeeting(employeeName, startTime, 30);
        return ResponseEntity.ok(responseDto);
    }
    @Operation(
            summary = "Find free slots",
            description = "This endpoint finds available time slots for two employees.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Free slots found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "One or both employees not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/free-slots")
    public ResponseEntity<Set<LocalDateTime>> findFreeSlots(
            @Parameter(description = "Name of the first employee")
            @RequestParam String employee1Name,
            @Parameter(description = "Name of the second employee")
            @RequestParam String employee2Name) {
        Set<LocalDateTime> freeSlots = calenderService.findFreeSlots(employee1Name, employee2Name, 30);
        return ResponseEntity.ok(freeSlots);
    }

    @Operation(
            summary = "Find conflicted participants",
            description = "This endpoint checks for employees with meetings conflicting with a specified time range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List of conflicted participants found"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid time range")
    })
    @GetMapping("/conflicted-participants")
    public ResponseEntity<List<String>> findConflictedParticipants(
            @Parameter(description = "Start time of the meeting")
            @RequestParam LocalDateTime startTime,
            @Parameter(description = "End time of the meeting")
            @RequestParam LocalDateTime endTime) {
        List<String> conflictedParticipants = calenderService.findConflictedParticipants(startTime, endTime);
        return ResponseEntity.ok(conflictedParticipants);
    }
}
