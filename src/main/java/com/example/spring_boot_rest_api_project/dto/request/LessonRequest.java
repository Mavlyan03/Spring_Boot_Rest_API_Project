package com.example.spring_boot_rest_api_project.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonRequest {

    private Long courseId;
    private String lessonName;

}
