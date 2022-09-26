package com.example.spring_boot_rest_api_project.service;

import com.example.spring_boot_rest_api_project.dto.request.LessonRequest;
import com.example.spring_boot_rest_api_project.dto.response.LessonResponse;
import com.example.spring_boot_rest_api_project.dto.view.LessonResponseView;
import com.example.spring_boot_rest_api_project.exception.NotFoundException;
import com.example.spring_boot_rest_api_project.model.Course;
import com.example.spring_boot_rest_api_project.model.Lesson;
import com.example.spring_boot_rest_api_project.repository.CourseRepository;
import com.example.spring_boot_rest_api_project.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonServiceImpl {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;


    public LessonResponse saveLesson(LessonRequest request) {
        Lesson lesson = new Lesson();
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Course with id - %s not found"));
        course.addLessons(lesson);
        lesson.setCourses(course);
        lesson.setLessonName(request.getLessonName());
        Lesson lesson1 = lessonRepository.save(lesson);
        return new LessonResponse(lesson1.getLessonId(), lesson1.getLessonName(), lesson1.getCourses().getCourseId());
    }

    public LessonResponseView getLessonsPagination(String text, int page, int size) {
        LessonResponseView view = new LessonResponseView();
        Pageable pageable = PageRequest.of(page - 1, size);
        view.setLessonResponses(getLessons(search(text,pageable)));
        return view;
    }
    private List<Lesson> search(String name, Pageable pageable) {
        String text = name == null ? "" : name;
        return lessonRepository.searchByLesson(text.toUpperCase(), pageable);
    }

    public List<LessonResponse> getLessons(List<Lesson> lessons) {
        List<LessonResponse> responses = new ArrayList<>();
        for (Lesson lesson : lessons) {
            responses.add(new LessonResponse(lesson.getLessonId(),
                    lesson.getLessonName(), lesson.getCourses().getCourseId()));
        }
        return responses;
    }

    public LessonResponse getById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Lesson with id - %s not found", id))));
        return new LessonResponse(lesson.getLessonId(), lesson.getLessonName(), lesson.getCourses().getCourseId());
    }

    public LessonResponse updateLessons(Long id, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new NotFoundException((String.format("Lesson with id - %s not found", id))));
        lesson.setLessonName(request.getLessonName());
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException((String.format("Course with id - %s not found", request.getCourseId()))));
        course.addLessons(lesson);
        lesson.setCourses(course);
        lessonRepository.save(lesson);
        return new LessonResponse(lesson.getLessonId(), lesson.getLessonName(), lesson.getCourses().getCourseId());
    }

    public LessonResponse deleteById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new NotFoundException((String.format("Lesson with id - %s not found", id))));
        lesson.setVideo(null);
        lessonRepository.delete(lesson);
        Lesson lesson1 = new Lesson();
        lesson1.setLessonName(lesson.getLessonName());
        return new LessonResponse(lesson1.getLessonId(), lesson1.getLessonName(), lesson.getCourses().getCourseId());
    }

    public List<Lesson> findAllLessons() {
        return lessonRepository.findAll();
    }
}
