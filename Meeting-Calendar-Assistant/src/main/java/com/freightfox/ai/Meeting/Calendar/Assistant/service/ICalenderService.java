package com.freightfox.ai.Meeting.Calendar.Assistant.service;

import com.freightfox.ai.Meeting.Calendar.Assistant.dto.EmployeeDto;
import com.freightfox.ai.Meeting.Calendar.Assistant.dto.MeetingDto;
import com.freightfox.ai.Meeting.Calendar.Assistant.dto.ResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ICalenderService {

    ResponseDto addEmployee(EmployeeDto employeeDto);
    ResponseDto bookMeeting(String employeeName, LocalDateTime startTime, int durationMinutes);
    Set<LocalDateTime> findFreeSlots(String employee1Name, String employee2Name, int durationMinutes);
    List<String> findConflictedParticipants(LocalDateTime startTime, LocalDateTime endTime);



}
