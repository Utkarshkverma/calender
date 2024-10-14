package com.freightfox.ai.Meeting.Calendar.Assistant.repository;

import com.freightfox.ai.Meeting.Calendar.Assistant.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, String> {
}
