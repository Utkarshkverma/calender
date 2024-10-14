package com.freightfox.ai.Meeting.Calendar.Assistant.service;

import com.freightfox.ai.Meeting.Calendar.Assistant.dto.EmployeeDto;
import com.freightfox.ai.Meeting.Calendar.Assistant.dto.ResponseDto;
import com.freightfox.ai.Meeting.Calendar.Assistant.entity.Employee;
import com.freightfox.ai.Meeting.Calendar.Assistant.entity.Meeting;
import com.freightfox.ai.Meeting.Calendar.Assistant.exception.ResourceNotFoundException;
import com.freightfox.ai.Meeting.Calendar.Assistant.exception.ResponseException;
import com.freightfox.ai.Meeting.Calendar.Assistant.repository.EmployeeRepository;
import com.freightfox.ai.Meeting.Calendar.Assistant.repository.MeetingRepository;
import com.freightfox.ai.Meeting.Calendar.Assistant.service.impl.CalenderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

public class CalenderServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private CalenderServiceImpl calenderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addEmployeeTest()
    {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName("Utkarsh Kumar Verma");

        ResponseDto responseDto = calenderService.addEmployee(employeeDto);

        assertNotNull(responseDto);
        assertEquals(HttpStatus.ACCEPTED.toString(), responseDto.getStatusCode());
        assertEquals("Employee added successfully", responseDto.getMessage());

    }

    @Test
    void employeeNotFoundTest()
    {
        String employeeName = "Utkarsh Kumar Verma";
        LocalDateTime startTime = LocalDateTime.of(2024, 10, 15, 10, 0);
        int durationMinutes = 30;

        when(employeeRepository.findByName(employeeName)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                calenderService.bookMeeting(employeeName, startTime, durationMinutes));
        assertEquals("Employee not found with the given name", exception.getMessage());
    }

    @Test
    void meetingConflictTest()
    {
        String employeeName = "Utkarsh Kumar Verma";
        LocalDateTime startTime = LocalDateTime.of(2024, 10, 15, 10, 0);
        int durationMinutes = 30;
        LocalDateTime endTime = startTime.plusMinutes(durationMinutes);

        Employee employee = new Employee();
        employee.setId(UUID.randomUUID().toString());
        employee.setName(employeeName);

        Meeting existingMeeting = new Meeting();
        existingMeeting.setStartTime(LocalDateTime.of(2024, 10, 15, 9, 30));
        existingMeeting.setEndTime(LocalDateTime.of(2024, 10, 15, 10, 30));

        employee.setMeetings(List.of(existingMeeting));

        when(employeeRepository.findByName(employeeName)).thenReturn(Optional.of(employee));

        ResponseException exception = assertThrows(ResponseException.class, () ->
                calenderService.bookMeeting(employeeName, startTime, durationMinutes));

        assertEquals("The requested meeting time conflicts with an existing meeting", exception.getMessage());
    }

    @Test
    void meetingBookedTest() {
        String employeeName = "Utkarsh Kumar Verma";
        LocalDateTime startTime = LocalDateTime.of(2024, 10, 15, 10, 0);
        int durationMinutes = 30;
        LocalDateTime endTime = startTime.plusMinutes(durationMinutes);

        Employee employee = new Employee();
        employee.setId(UUID.randomUUID().toString());
        employee.setName(employeeName);

        when(employeeRepository.findByName(employeeName)).thenReturn(Optional.of(employee));
        when(meetingRepository.save(any(Meeting.class))).thenAnswer(i -> i.getArgument(0));

        ResponseDto response = calenderService.bookMeeting(employeeName, startTime, durationMinutes);

        assertNotNull(response);
        assertEquals("200", response.getStatusCode());
        assertEquals("Meeting booked successfully for " + employeeName, response.getMessage());
    }

    @Test
    void participantsConflictTest()
    {
        LocalDateTime startTime = LocalDateTime.of(2024, 10, 15, 10, 0);
        LocalDateTime endTime = startTime.plusMinutes(30);

        List<Employee> employees = new ArrayList<>();

        Meeting meeting1 = new Meeting("meeting1", startTime.minusMinutes(15), startTime.plusMinutes(15), null);
        Employee emp1 = new Employee("1", "Utkarsh", List.of(meeting1));

        Meeting meeting2 = new Meeting("meeting2", startTime.plusMinutes(40), startTime.plusMinutes(70), null);
        Employee emp2 = new Employee("2", "Ayush", List.of(meeting2));

        Meeting meeting3 = new Meeting("meeting3", startTime.minusMinutes(10), startTime.plusMinutes(10), null);
        Employee emp3 = new Employee("3", "Saharsh", List.of(meeting3));

        Meeting meeting4 = new Meeting("meeting4", startTime.plusMinutes(35), startTime.plusMinutes(65), null);
        Employee emp4 = new Employee("4", "Rishab", List.of(meeting4));

        Meeting meeting5 = new Meeting("meeting5", startTime.plusMinutes(5), startTime.plusMinutes(35), null);
        Employee emp5 = new Employee("5", "Kartikeya", List.of(meeting5));

        employees.add(emp1);
        employees.add(emp2);
        employees.add(emp3);
        employees.add(emp4);
        employees.add(emp5);

        when(employeeRepository.findAll()).thenReturn(employees);

        List<String> result = calenderService.findConflictedParticipants(startTime, endTime);

        assertEquals(3, result.size(), "Expected three conflicts");
        assertTrue(result.contains("Utkarsh"), "Expected Utkarsh to be in the conflict list");
        assertTrue(result.contains("Saharsh"), "Expected Saharsh to be in the conflict list");
        assertTrue(result.contains("Kartikeya"), "Expected Kartikeya to be in the conflict list");
        assertFalse(result.contains("Ayush"), "Ayush should not be in the conflict list");
        assertFalse(result.contains("Rishab"), "Rishab should not be in the conflict list");
    }

    @Test
    void freeSlotEmployee1DoesNotExist()
    {
        String employee1Name = "Rahul";
        String employee2Name = "Utkarsh";
        when(employeeRepository.findByName(employee1Name)).thenReturn(Optional.empty());
        when(employeeRepository.findByName(employee2Name)).thenReturn(Optional.of(new Employee()));

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                calenderService.findFreeSlots(employee1Name,employee2Name,30)
        );

        assertEquals("Employee not found with the name " + employee1Name, exception.getMessage());
    }

    @Test
    void freeSlotEmployee2DoesNotExist()
    {
        String employee1Name = "Utkarsh";
        String employee2Name = "Vartika";
        when(employeeRepository.findByName(employee1Name)).thenReturn(Optional.of(new Employee()));
        when(employeeRepository.findByName(employee2Name)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                calenderService.findFreeSlots(employee1Name,employee2Name,30)
        );

        assertEquals("Employee not found with the name " + employee2Name, exception.getMessage());
    }



}
