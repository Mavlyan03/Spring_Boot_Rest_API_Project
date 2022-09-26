package com.example.spring_boot_rest_api_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CourseResponse {

    private Long id;
    private String course_name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private int duration;
    private String image;
    private String description;
    private Long companyId;
}
