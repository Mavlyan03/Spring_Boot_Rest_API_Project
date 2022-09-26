package com.example.spring_boot_rest_api_project.api;

import com.example.spring_boot_rest_api_project.dto.request.LessonRequest;
import com.example.spring_boot_rest_api_project.dto.response.LessonResponse;
import com.example.spring_boot_rest_api_project.dto.view.LessonResponseView;
import com.example.spring_boot_rest_api_project.model.Lesson;
import com.example.spring_boot_rest_api_project.service.LessonServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lesson")
@Tag(name = "Lesson API", description = "ADMIN and INSTRUCTOR can create,update,delete lessons")
public class LessonController {

    private final LessonServiceImpl service;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR')")
    @Operation(description = "ADMIN and INSTRUCTOR can create the lesson")
    public LessonResponse create(@RequestBody LessonRequest request) {
        return service.saveLesson(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR')")
    @Operation(description = "MANAGER and INSTRUCTOR can get the lesson by id")
    public LessonResponse getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','ADMIN')")
    @Operation(description = "INSTRUCTOR and ADMIN can update the lesson")
    public LessonResponse update(@PathVariable("id") Long id, @RequestBody LessonRequest request) {
        return service.updateLessons(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR')")
    @Operation(description = "INSTRUCTOR ADMIN can delete the lesson")
    public LessonResponse deleteById(@PathVariable("id") Long id) {
        return service.deleteById(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR')")
    @Operation(description = "ADMIN and INSTRUCTOR can find all lessons")
    public List<Lesson> findAll() {
        return service.findAllLessons();
    }

    @GetMapping("searchByLesson")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','ADMIN')")
    @Operation(description = "ADMIN and INSTRUCTOR can searching the lessons")
    public LessonResponseView getAllLessonsPagination(@RequestParam(value = "text", required = false) String text,
                                                      @RequestParam int page,
                                                      @RequestParam int size) {
        return service.getLessonsPagination(text, page, size);
    }
}