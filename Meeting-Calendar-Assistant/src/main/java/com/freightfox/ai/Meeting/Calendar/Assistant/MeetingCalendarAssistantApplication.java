package com.freightfox.ai.Meeting.Calendar.Assistant;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Frightfox.ai.ai assignment REST API documentation",
				description = "This is the project assignment of Freightfox.ai ",
				version = "v1",
				contact = @Contact(
						name =  "Utkarsh Kumar Verma",
						email = "vermau2k01@gmail.com"
				)
		)
)
public class MeetingCalendarAssistantApplication {

	public static void main(String[] args) {

		SpringApplication.run(MeetingCalendarAssistantApplication.class, args);
	}

}
