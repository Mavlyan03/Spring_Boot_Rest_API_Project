package com.example.spring_boot_rest_api_project.service;

import com.example.spring_boot_rest_api_project.dto.request.CourseRequest;
import com.example.spring_boot_rest_api_project.dto.response.CourseResponse;
import com.example.spring_boot_rest_api_project.dto.view.CourseResponseView;
import com.example.spring_boot_rest_api_project.exception.NotFoundException;
import com.example.spring_boot_rest_api_project.model.Company;
import com.example.spring_boot_rest_api_project.model.Course;
import com.example.spring_boot_rest_api_project.model.Instructor;
import com.example.spring_boot_rest_api_project.repository.CompanyRepository;
import com.example.spring_boot_rest_api_project.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl {

    private final CourseRepository courseRepository;
    private final CompanyRepository companyRepository;

    public CourseResponse saveCourse(CourseRequest request) {
        Course course = new Course();
        Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(
                () -> new NotFoundException("Company with id - %s not found"));
        course.setCourseName(request.getCourse_name());
        course.setDateOfStart(LocalDate.now());
        course.setDescription(request.getDescription());
        course.setDuration(request.getDuration());
        course.setImage(request.getImage());
        company.addCourse(course);
        course.setCompany(company);
        Course course1 = courseRepository.save(course);
        return mapToResponse(course1);
    }

    public CourseResponseView getAllCoursesPagination(String text, int page, int size) {
        CourseResponseView view = new CourseResponseView();
        Pageable pageable = PageRequest.of(page - 1, size);
        view.setCourseResponses(getCourses(search(text, pageable)));
        return view;
    }

    private List<Course> search(String name, Pageable pageable) {
        String text = name == null ? "" : name;
        return courseRepository.searchByCourse(text.toUpperCase(), pageable);
    }

    public List<CourseResponse> getCourses(List<Course> courses) {
        List<CourseResponse> responses = new ArrayList<>();
        for (Course course : courses) {
            responses.add(mapToResponse(course));
        }
        return responses;
    }

    public CourseResponse getById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException((String.format("Course with id - %s not found", id))));
        return mapToResponse(course);
    }

    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException((String.format("Course with id - %s not found", id))));
        course.setCourseName(request.getCourse_name());
        course.setDateOfStart(request.getDateOfStart());
        course.setImage(request.getImage());
        course.setDescription(request.getDescription());
        course.setDuration(request.getDuration());
        Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(
                () -> new NotFoundException("Company with id - %s not found"));
        company.addCourse(course);
        course.setCompany(company);
        courseRepository.save(course);
        return mapToResponse(course);
    }

    public CourseResponse deleteById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Course with id - %s not found", id))));
        course.setCompany(null);
        for (Instructor instructor : course.getInstructors()) {
            instructor.setCourses(null);
        }
        courseRepository.delete(course);
        return new CourseResponse(course.getCourseId(), course.getCourseName(),
                course.getDuration(), course.getImage(), course.getDescription(), null);
    }

    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    private CourseResponse mapToResponse(Course course) {
        return new CourseResponse(
                course.getCourseId(), course.getCourseName(),
                course.getDuration(), course.getImage(),
                course.getDescription(), course.getCompany().getCompanyId());
    }

}