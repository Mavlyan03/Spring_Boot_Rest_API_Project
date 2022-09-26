package com.example.spring_boot_rest_api_project.api;

import com.example.spring_boot_rest_api_project.dto.request.VideoRequest;
import com.example.spring_boot_rest_api_project.dto.response.VideoResponse;
import com.example.spring_boot_rest_api_project.dto.view.VideoResponseView;
import com.example.spring_boot_rest_api_project.model.Video;
import com.example.spring_boot_rest_api_project.service.VideoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/video")
@Tag(name = "Video API", description = "ADMIN and INSTRUCTOR can create,update,delete videos")
@PreAuthorize("hasAuthority('INSTRUCTOR')")
public class VideoController {

    private final VideoServiceImpl service;

    @PostMapping
    @Operation(description = "INSTRUCTOR can create the video")
    public VideoResponse create(@RequestBody VideoRequest request) {
        return service.saveVideo(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','ADMIN')")
    @Operation(description = "INSTRUCTOR and ADMIN can get the video by id")
    public VideoResponse getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }


    @PutMapping("/{id}")
    @Operation(description = "INSTRUCTOR can update the video")
    public VideoResponse update(@PathVariable("id") Long id, @RequestBody VideoRequest request) {
        return service.updateVideo(id, request);
    }

    @DeleteMapping("{id}")
    @Operation(description = "INSTRUCTOR can delete the video")
    public VideoResponse deleteById(@PathVariable("id") Long id) {
        return service.deleteById(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','ADMIN')")
    @Operation(description = "INSTRUCTOR and ADMIN can find all videos")
    public List<Video> findAll() {
        return service.findAllVideos();
    }

    @GetMapping("/searchByVideo")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(description = "ADMIN and MANAGER can searching the videos")
    public VideoResponseView getAllVideosPagination(@RequestParam(value = "text", required = false) String text,
                                                    @RequestParam int page,
                                                    @RequestParam int size) {
        return service.getVideosPagination(text, page, size);
    }
}
