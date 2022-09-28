package com.example.spring_boot_rest_api_project.service;

import com.example.spring_boot_rest_api_project.dto.request.StudentRequest;
import com.example.spring_boot_rest_api_project.dto.response.InstructorResponse;
import com.example.spring_boot_rest_api_project.dto.response.StudentResponse;
import com.example.spring_boot_rest_api_project.dto.view.InstructorResponseView;
import com.example.spring_boot_rest_api_project.dto.view.StudentResponseView;
import com.example.spring_boot_rest_api_project.exception.NotFoundException;
import com.example.spring_boot_rest_api_project.model.*;
import com.example.spring_boot_rest_api_project.repository.*;
import com.example.spring_boot_rest_api_project.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthInfoRepository authInfoRepository;

    public StudentResponse saveStudent(StudentRequest request) {
        Student student = studentRepository.save(convertToEntity(request));
        return convertToResponse(student);
    }

    private Student convertToEntity(StudentRequest request) {
        Student student = new Student(request.getFirstName(), request.getLastName(),
                request.getPhoneNumber(), request.getEmail(), request.getStudyFormat());
        Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(
                () -> new NotFoundException((String.format("Company with id - %s not found", request.getCompanyId()))));
        AuthInfo authInfo = new AuthInfo();
        authInfo.setUsername(request.getFirstName());
        authInfo.setEmail(request.getEmail());
        authInfo.setPassword(passwordEncoder.encode(request.getPassword()));
        authInfo.setCreated(LocalDateTime.now());
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByRoleName("STUDENT").orElseThrow(
                () -> new NotFoundException("Student with name - %s not found"));
        roles.add(role);
        authInfo.setRoles(roles);
        student.setAuthInfo(authInfo);
        company.addStudent(student);
        student.setCompany(company);
        return student;
    }

    private StudentResponse convertToResponse(Student student) {
        StudentResponse studentResponse = new StudentResponse(
                student.getStudentId(), student.getFirstName(), student.getLastName(),
                student.getPhoneNumber(), student.getEmail(), student.getStudyFormat(),
                student.getAuthInfo().getPassword(), student.getCompany().getCompanyId(),
                student.getAuthInfo().getIsActive(), student.getAuthInfo().getCreated());
        String token = jwtTokenUtil.generateToken(authInfoRepository.findByEmail(student.getEmail()).orElseThrow(
                () -> new NotFoundException("Student with email - %s not found")));
        studentResponse.setToken(token);
        Set<Role> roles = student.getAuthInfo().getRoles();
        Set<String> rolesSet = new HashSet<>();
        for (Role role : roles) {
            rolesSet.add(role.getRoleName());
        }
        studentResponse.setRoles(rolesSet);
        return studentResponse;
    }

    public StudentResponseView getAllStudentsPagination(String text, int page, int size) {
        StudentResponseView view = new StudentResponseView();
        Pageable pageable = PageRequest.of(page - 1, size);
        view.setStudentResponses(getStudents(search(text, pageable)));
        return view;
    }

    private List<Student> search(String name, Pageable pageable) {
        String text = name == null ? "" : name;
        return studentRepository.searchByStudent(text.toUpperCase(), pageable);
    }

    public List<StudentResponse> getStudents(List<Student> students) {
        List<StudentResponse> responses = new ArrayList<>();
        for (Student student : students) {
            responses.add(mapToResponse(student));
        }
        return responses;
    }

    public StudentResponse getStudentById(Long id) {
        return convertToResponse(studentRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Student with id -%s not found", id)))));
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Student with id - %s not found", id))));
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setStudyFormat(request.getStudyFormat());
        student.setPhoneNumber(request.getPhoneNumber());
        student.getAuthInfo().setPassword(passwordEncoder.encode(request.getPassword()));
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Company with id - %s not found"));
        company.addStudent(student);
        student.setCompany(company);
        studentRepository.save(student);
        return mapToResponse(student);
    }

    public StudentResponse block(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student with id - %s not found"));
        AuthInfo authInfo = authInfoRepository.findById(student.getAuthInfo().getId())
                .orElseThrow(() -> new NotFoundException("User with id - %s not found"));
        authInfo.setIsActive(false);
        studentRepository.save(student);
        return mapToResponse(student);
    }

    public StudentResponse deleteById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Student with id - %s not found", id))));
        student.setCompany(null);
        student.setCourse(null);
        AuthInfo authInfo = authInfoRepository.findByEmail(student.getEmail()).orElseThrow(
                () -> new NotFoundException("User with email - %s not found"));
        authInfo.getRoles().forEach(role -> {
            if (role.getRoleName().equals("STUDENT")) {
                authInfo.setRoles(null);
            } else {
                System.out.println("Not found");
            }
        });
        studentRepository.delete(student);
        return new StudentResponse(student.getStudentId(), student.getFirstName(), student.getLastName(),
                student.getPhoneNumber(), student.getEmail(), null, null, null,
                student.getAuthInfo().getIsActive(), student.getAuthInfo().getCreated());
    }

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public String assignStudentToCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new NotFoundException((String.format("Student with id - %s not found", studentId))));
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException((String.format("Course with id - %s not found", courseId))));
        student.setCourse(course);
        course.addStudents(student);
        studentRepository.save(student);
        return "Assign student to course was successfully!";
    }

    private StudentResponse mapToResponse(Student student) {
        return new StudentResponse(
                student.getStudentId(), student.getFirstName(), student.getLastName(),
                student.getPhoneNumber(), student.getEmail(), student.getStudyFormat(),
                student.getAuthInfo().getPassword(), student.getCompany().getCompanyId(),
                student.getAuthInfo().getIsActive(), student.getAuthInfo().getCreated());
    }

}