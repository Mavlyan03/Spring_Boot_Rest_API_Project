package com.example.spring_boot_rest_api_project.api;

import com.example.spring_boot_rest_api_project.dto.request.CourseRequest;
import com.example.spring_boot_rest_api_project.dto.response.CourseResponse;
import com.example.spring_boot_rest_api_project.dto.view.CompanyResponseView;
import com.example.spring_boot_rest_api_project.dto.view.CourseResponseView;
import com.example.spring_boot_rest_api_project.model.Course;
import com.example.spring_boot_rest_api_project.service.CourseServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
@Tag(name = "Course API", description = "ADMIN and MANAGER can create,update,delete courses")
public class CourseController {

    private final CourseServiceImpl service;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(description = "MANAGER can create the course")
    public CourseResponse create(@RequestBody CourseRequest request) {
        return service.saveCourse(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    @Operation(description = "MANAGER and ADMIN can get course by id")
    public CourseResponse getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    @Operation(description = "MANAGER and ADMIN can update the course")
    public CourseResponse update(@PathVariable("id") Long id, @RequestBody CourseRequest request) {
        return service.updateCourse(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(description = "ADMIN can delete the course")
    public CourseResponse deleteById(@PathVariable("id") Long id) {
        return service.deleteById(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    @Operation(description = "MANAGER and ADMIN can find all courses")
    public List<Course> findAll() {
        return service.findAllCourses();
    }

    @GetMapping("/searchByCourse")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(description = "ADMIN and MANAGER can searching the courses")
    public CourseResponseView getAllCoursesPagination(@RequestParam(value = "text", required = false) String text,
                                                      @RequestParam int page,
                                                      @RequestParam int size) {
        return service.getAllCoursesPagination(text, page, size);
    }
}