package com.example.spring_boot_rest_api_project.api;

import com.example.spring_boot_rest_api_project.dto.request.InstructorRequest;
import com.example.spring_boot_rest_api_project.dto.response.InstructorResponse;
import com.example.spring_boot_rest_api_project.dto.view.CourseResponseView;
import com.example.spring_boot_rest_api_project.dto.view.InstructorResponseView;
import com.example.spring_boot_rest_api_project.model.Instructor;
import com.example.spring_boot_rest_api_project.service.InstructorServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/instructor")
@Tag(name = "Instructor API",description = "ADMIN and MANAGER can create,update,delete instructors")
@PreAuthorize("hasAuthority('ADMIN')")
public class InstructorController {

    private final InstructorServiceImpl service;

    @PostMapping
    @Operation(description = "ADMIN can create the instructor")
    public InstructorResponse create(@RequestBody InstructorRequest request) {
        return service.saveInstructor(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    @Operation(description = "MANAGER and ADMIN can create the instructor")
    public InstructorResponse getById(@PathVariable("id") Long id) {
        return service.getInstructorById(id);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    @Operation(description = "MANAGER and ADMIN can update the instructor")
    public InstructorResponse update(@PathVariable("id") Long id, @RequestBody InstructorRequest request) {
        return service.updateInstructor(id,request);
    }

    @DeleteMapping("{id}")
    @Operation(description = "ADMIN can delete the instructor")
    public InstructorResponse deleteById(@PathVariable("id") Long id) {
        return service.deleteById(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    @Operation(description = "MANAGER and ADMIN can find all instructors")
    public List<Instructor> findAll() {
        return service.findAllInstructors();
    }

    @PostMapping("/assign")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(description = "ADMIN and MANAGER can assign instructor to course")
    public String assignInstructorToCourse(@RequestParam Long instructorId,@RequestParam Long courseId) {
        return service.assignInstructorToCourse(instructorId,courseId);
    }

    @GetMapping("/searchByInstructor")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(description = "ADMIN and MANAGER can searching the instructors")
    public InstructorResponseView getAllInstructorPagination(@RequestParam(value = "text",required = false)String text,
                                               @RequestParam int page,
                                               @RequestParam int size) {
        return service.getAllInstructorsPagination(text,page,size);
    }
}