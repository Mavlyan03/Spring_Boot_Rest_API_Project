package com.example.spring_boot_rest_api_project.service;

import com.example.spring_boot_rest_api_project.dto.request.TaskRequest;
import com.example.spring_boot_rest_api_project.dto.response.TaskResponse;
import com.example.spring_boot_rest_api_project.dto.view.TaskResponseView;
import com.example.spring_boot_rest_api_project.exception.NotFoundException;
import com.example.spring_boot_rest_api_project.model.Lesson;
import com.example.spring_boot_rest_api_project.model.Task;
import com.example.spring_boot_rest_api_project.repository.LessonRepository;
import com.example.spring_boot_rest_api_project.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl {

    private final TaskRepository taskRepository;
    private final LessonRepository lessonRepository;


    public TaskResponse saveTask(TaskRequest request) {
        Task task = new Task();
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> new NotFoundException(String.format("Lesson with id - %s not found", request.getLessonId())));
        task.setTaskName(request.getTaskName());
        task.setTaskText(request.getTaskText());
        task.setDeadline(request.getDeadline());
        lesson.addTasks(task);
        task.setLessons(lesson);
        Task task1 = taskRepository.save(task);
        return new TaskResponse(task1.getTaskId(), task1.getTaskName(),
                                task1.getTaskText(), task1.getDeadline(),
                                task.getLessons().getLessonId());
    }

    public TaskResponseView getTasksPagination(String text, int page, int size) {
        TaskResponseView view = new TaskResponseView();
        Pageable pageable = PageRequest.of(page - 1, size);
        view.setTaskResponses(getTasks(search(text, pageable)));
        return view;
    }

    private List<Task> search(String name, Pageable pageable) {
        String text = name == null ? "" : name;
        return taskRepository.searchByTask(text.toUpperCase(), pageable);
    }

    public List<TaskResponse> getTasks(List<Task> tasks) {
        List<TaskResponse> responses = new ArrayList<>();
        for (Task task : tasks) {
            responses.add(new TaskResponse(task.getTaskId(), task.getTaskName(),
                                           task.getTaskText(), task.getDeadline(),
                                           task.getLessons().getLessonId()));
        }
        return responses;
    }

    public TaskResponse getById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Task with id - %s not found", id))));
        return new TaskResponse(task.getTaskId(), task.getTaskName(),
                                task.getTaskText(), task.getDeadline(),
                                task.getLessons().getLessonId());
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Task with id - %s not found", id))));
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> new NotFoundException(String.format("Lesson with id - %s not found", request.getLessonId())));
        task.setTaskName(request.getTaskName());
        task.setTaskText(request.getTaskText());
        task.setDeadline(request.getDeadline());
        lesson.addTasks(task);
        task.setLessons(lesson);
        taskRepository.save(task);
        return new TaskResponse(task.getTaskId(), task.getTaskName(),
                                task.getTaskText(), task.getDeadline(),
                                task.getLessons().getLessonId());
    }

    public TaskResponse deleteById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Task with id - %s not found", id))));
        task.setLessons(null);
        taskRepository.delete(task);
        Task task1 = new Task();
        task1.setTaskName(task.getTaskName());
        task1.setTaskText(task.getTaskText());
        task1.setDeadline(task.getDeadline());
        return new TaskResponse(task.getTaskId(), task.getTaskName(),
                task.getTaskText(), task.getDeadline(), null);
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }
}
