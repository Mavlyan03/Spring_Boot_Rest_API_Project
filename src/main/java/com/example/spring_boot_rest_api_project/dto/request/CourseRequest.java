package com.example.spring_boot_rest_api_project.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class CourseRequest {

    private String course_name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfStart;

    private int duration;

    private String image;

    private String description;
    private Long companyId;
}
