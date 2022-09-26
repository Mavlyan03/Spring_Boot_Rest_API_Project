package com.example.spring_boot_rest_api_project.dto.view;

import com.example.spring_boot_rest_api_project.dto.response.TaskResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskResponseView {
    private List<TaskResponse> taskResponses;

}
