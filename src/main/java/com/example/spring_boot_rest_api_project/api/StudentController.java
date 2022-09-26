package com.example.spring_boot_rest_api_project.api;

import com.example.spring_boot_rest_api_project.dto.request.StudentRequest;
import com.example.spring_boot_rest_api_project.dto.response.StudentResponse;
import com.example.spring_boot_rest_api_project.dto.view.StudentResponseView;
import com.example.spring_boot_rest_api_project.model.Student;
import com.example.spring_boot_rest_api_project.service.StudentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
@Tag(name = "Student API",description = "ADMIN and MANAGER can create,update,delete students")
@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','INSTRUCTOR')")
public class StudentController {

    private final StudentServiceImpl service;

    @PostMapping
    @Operation(description = "ADMIN, MANAGER and INSTRUCTOR can create a student")
    public StudentResponse create(@RequestBody StudentRequest request) {
        return service.saveStudent(request);
    }

    @GetMapping("{id}")
    @Operation(description = "ADMIN, MANAGER and INSTRUCTOR can get the student by id")
    public StudentResponse getById(@PathVariable("id") Long id) {
        return service.getStudentById(id);
    }


    @PutMapping("/{id}")
    @Operation(description = "ADMIN, MANAGER and INSTRUCTOR can update the student")
    public StudentResponse update(@PathVariable("id") Long id, @RequestBody StudentRequest request) {
        return service.updateStudent(id,request);
    }

    @PutMapping("/block/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(description = "ADMIN can block the student")
    public StudentResponse block(@PathVariable("id")Long id) {
        return service.block(id);
    }


    @DeleteMapping("{id}")
    @Operation(description = "ADMIN, MANAGER and INSTRUCTOR can delete the student")
    public StudentResponse deleteById(@PathVariable("id") Long id) {
        return service.deleteById(id);
    }

    @GetMapping("/all")
    @Operation(description = "ADMIN, MANAGER and INSTRUCTOR can find all students")
    public List<Student> findAll() {
        return service.findAllStudents();
    }

    @PostMapping("/assignStudentToCourse")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(description = "STUDENT can assign to any course")
    public String assignStudentToCourse(@RequestParam Long studentId, @RequestParam Long courseId) {
        return service.assignStudentToCourse(studentId,courseId);
    }

    @GetMapping("/searchByStudent")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','INSTRUCTOR')")
    @Operation(description = "ADMIN,MANAGER and INSTRUCTOR can searching the students")
    public StudentResponseView getAllStudentsPagination(@RequestParam(value = "text",required = false)String text,
                                                          @RequestParam int page,
                                                          @RequestParam int size) {
        return service.getAllStudentsPagination(text,page,size);
    }
}