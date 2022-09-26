package com.example.spring_boot_rest_api_project.api;

import com.example.spring_boot_rest_api_project.dto.request.TaskRequest;
import com.example.spring_boot_rest_api_project.dto.response.TaskResponse;
import com.example.spring_boot_rest_api_project.dto.view.TaskResponseView;
import com.example.spring_boot_rest_api_project.model.Task;
import com.example.spring_boot_rest_api_project.service.TaskServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
@Tag(name = "Task API",description = "ADMIN and INSTRUCTOR can create,update,delete tasks")
@PreAuthorize("hasAuthority('INSTRUCTOR')")
public class TaskController {

    private final TaskServiceImpl service;

    @PostMapping
    @Operation(description = "INSTRUCTOR can create the task")
    public TaskResponse create(@RequestBody TaskRequest request) {
        return service.saveTask(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','ADMIN')")
    @Operation(description = "INSTRUCTOR and ADMIN can get the task by id")
    public TaskResponse getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }


    @PutMapping("/{id}")
    @Operation(description = "INSTRUCTOR can update the task")
    public TaskResponse update(@PathVariable("id") Long id, @RequestBody TaskRequest request) {
        return service.updateTask(id,request);
    }

    @DeleteMapping("{id}")
    @Operation(description = "ADMIN can delete the task")
    public TaskResponse deleteById(@PathVariable("id") Long id) {
        return service.deleteById(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','ADMIN')")
    @Operation(description = "INSTRUCTOR and ADMIN can find all tasks")
    public List<Task> findAll() {
        return service.findAllTasks();
    }

    @GetMapping("/searchByTask")
    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR')")
    @Operation(description = "ADMIN and MANAGER can searching the tasks")
    public TaskResponseView getAllTasksPagination(@RequestParam(value = "text",required = false)String text,
                                                  @RequestParam int page,
                                                  @RequestParam int size) {
        return service.getTasksPagination(text,page,size);
    }
}