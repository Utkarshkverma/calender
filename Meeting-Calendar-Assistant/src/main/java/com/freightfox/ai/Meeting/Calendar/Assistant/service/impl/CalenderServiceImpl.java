package com.freightfox.ai.Meeting.Calendar.Assistant.service.impl;

import com.freightfox.ai.Meeting.Calendar.Assistant.dto.EmployeeDto;
import com.freightfox.ai.Meeting.Calendar.Assistant.dto.MeetingDto;
import com.freightfox.ai.Meeting.Calendar.Assistant.dto.ResponseDto;
import com.freightfox.ai.Meeting.Calendar.Assistant.entity.Employee;
import com.freightfox.ai.Meeting.Calendar.Assistant.entity.Meeting;
import com.freightfox.ai.Meeting.Calendar.Assistant.exception.ResourceNotFoundException;
import com.freightfox.ai.Meeting.Calendar.Assistant.exception.ResponseException;
import com.freightfox.ai.Meeting.Calendar.Assistant.repository.EmployeeRepository;
import com.freightfox.ai.Meeting.Calendar.Assistant.repository.MeetingRepository;
import com.freightfox.ai.Meeting.Calendar.Assistant.service.ICalenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CalenderServiceImpl implements ICalenderService {

    private final MeetingRepository meetingRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public ResponseDto addEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setId(UUID.randomUUID().toString());
        employeeRepository.save(employee);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.ACCEPTED.toString());
        responseDto.setMessage("Employee added successfully");
        return responseDto;
    }

    @Override
    public ResponseDto bookMeeting(String employeeName, LocalDateTime startTime, int durationMinutes) {
        Employee employee = employeeRepository
                .findByName(employeeName)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with the given name"));
        LocalDateTime endTime = startTime.plusMinutes(durationMinutes);

        if (hasConflict(employee, startTime, endTime)) {
            throw new ResponseException("The requested meeting time conflicts with an existing meeting");
        }

        Meeting meeting = new Meeting();
        meeting.setId(UUID.randomUUID().toString());
        meeting.setStartTime(startTime);
        meeting.setEndTime(endTime);
        meeting.setEmployee(employee);

        meetingRepository.save(meeting);
        return new ResponseDto("200", "Meeting booked successfully for " + employee.getName());
    }

    @Override
    public Set<LocalDateTime> findFreeSlots(String employee1Name, String employee2Name, int durationMinutes) {
        Employee employee1 = employeeRepository
                .findByName(employee1Name)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with the name " + employee1Name));
        Employee employee2 = employeeRepository
                .findByName(employee2Name)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with the name " + employee2Name));

        Set<LocalDateTime> freeSlots = new HashSet<>();
        LocalDateTime startOfDay = LocalDateTime.now().withHour(9).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(17).withMinute(0).withSecond(0);

        List<LocalDateTime> busySlots = new ArrayList<>();

        for (Meeting meeting : employee1.getMeetings()) {
            busySlots.add(meeting.getStartTime());
            busySlots.add(meeting.getEndTime());
        }
        for (Meeting meeting : employee2.getMeetings()) {
            busySlots.add(meeting.getStartTime());
            busySlots.add(meeting.getEndTime());
        }

        LocalDateTime currentSlot = startOfDay;
        while (currentSlot.plusMinutes(durationMinutes).isBefore(endOfDay.plusMinutes(1))) {
            LocalDateTime nextSlot = currentSlot.plusMinutes(durationMinutes);

            LocalDateTime finalCurrentSlot = currentSlot;
            boolean isBusy = busySlots.stream().anyMatch(busy ->
                    (busy.isAfter(finalCurrentSlot) && busy.isBefore(nextSlot)) ||
                            busy.equals(finalCurrentSlot) || busy.equals(nextSlot)
            );

            if (!isBusy) {
                freeSlots.add(currentSlot);
            }

            currentSlot = currentSlot.plusMinutes(30);
        }

        return freeSlots;
    }



    @Override
    public List<String> findConflictedParticipants(LocalDateTime startTime, LocalDateTime endTime) {
        List<Employee> allEmployees = employeeRepository.findAll();
        List<String> conflictedParticipants = new ArrayList<>();

        for(Employee employee : allEmployees) {
            if (employee.getMeetings() != null)
            {
                for (Meeting meeting : employee.getMeetings())
                {
                    if (isOverlapping(meeting.getStartTime(), meeting.getEndTime(), startTime, endTime)) {
                        conflictedParticipants.add(employee.getName());
                        break;
                    }
                }
            }
        }

        return conflictedParticipants;
    }

    private boolean isOverlapping(LocalDateTime meetingStart, LocalDateTime meetingEnd, LocalDateTime requestStart, LocalDateTime requestEnd) {
        return meetingStart.isBefore(requestEnd) && meetingEnd.isAfter(requestStart);
    }

    private boolean hasConflict(Employee employee, LocalDateTime startTime, LocalDateTime endTime) {
        return employee.getMeetings().stream()
                .anyMatch(meeting -> meeting.getStartTime().isBefore(endTime) && meeting.getEndTime().isAfter(startTime));
    }
}
