package com.example.spring_boot_rest_api_project.dto.view;

import com.example.spring_boot_rest_api_project.dto.response.StudentResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentResponseView {
    private List<StudentResponse> studentResponses;
}
