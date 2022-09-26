package com.example.spring_boot_rest_api_project.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VideoResponse {
    private Long id;
    private String videoName;
    private String link;
    private Long lessonId;
}
