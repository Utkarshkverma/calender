package com.freightfox.ai.Meeting.Calendar.Assistant.repository;

import com.freightfox.ai.Meeting.Calendar.Assistant.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findByName(String name);
}
