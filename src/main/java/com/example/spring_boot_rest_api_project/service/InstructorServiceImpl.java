package com.example.spring_boot_rest_api_project.service;

import com.example.spring_boot_rest_api_project.dto.request.InstructorRequest;
import com.example.spring_boot_rest_api_project.dto.response.InstructorResponse;
import com.example.spring_boot_rest_api_project.dto.view.InstructorResponseView;
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
public class InstructorServiceImpl {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthInfoRepository authInfoRepository;
    private final JwtTokenUtil jwtTokenUtil;

    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;

    public InstructorResponse saveInstructor(InstructorRequest request) {
        Instructor instructor = instructorRepository.save(convertToEntity(request));
        return convertToResponse(instructor);
    }

    private Instructor convertToEntity(InstructorRequest request) {
        Instructor instructor = new Instructor(request.getFirstName(), request.getLastName(),
                request.getPhoneNumber(), request.getEmail(), request.getSpecialization());
        Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(
                () -> new NotFoundException((String.format("Company with id - %s not found", request.getCompanyId()))));
        AuthInfo authInfo = new AuthInfo();
        authInfo.setUsername(request.getFirstName());
        authInfo.setEmail(request.getEmail());
        authInfo.setPassword(passwordEncoder.encode(request.getPassword()));
        authInfo.setCreated(LocalDateTime.now());
        authInfo.setIsActive(authInfo.getIsActive());
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByRoleName("INSTRUCTOR").orElseThrow(() ->
                new NotFoundException("Role with name - %s not found"));
        roles.add(role);
        authInfo.setRoles(roles);
        instructor.setAuthInfo(authInfo);
        company.addInstructor(instructor);
        instructor.setCompany(company);
        return instructor;
    }

    private InstructorResponse convertToResponse(Instructor instructor) {
        InstructorResponse instructorResponse = new InstructorResponse(instructor.getInstructorId(),
                instructor.getFirstName(), instructor.getLastName(), instructor.getPhoneNumber(),
                instructor.getEmail(), instructor.getSpecialization(),
                instructor.getAuthInfo().getPassword(), instructor.getCompany().getCompanyId(),
                instructor.getAuthInfo().getIsActive(), instructor.getAuthInfo().getCreated());
        String token = jwtTokenUtil.generateToken(authInfoRepository.findByEmail(instructor.getEmail()).orElseThrow(() ->
                new NotFoundException(("Instructor with email - %s not found"))));
        instructorResponse.setToken(token);
        Set<Role> roles = instructor.getAuthInfo().getRoles();
        Set<String> rolesSet = new HashSet<>();
        for (Role role : roles) {
            rolesSet.add(role.getRoleName());
        }
        instructorResponse.setRole(rolesSet);
        return instructorResponse;
    }

    public InstructorResponseView getAllInstructorsPagination(String text, int page, int size) {
        InstructorResponseView view = new InstructorResponseView();
        Pageable pageable = PageRequest.of(page - 1, size);
        view.setInstructorResponses(getInstructors(search(text, pageable)));
        return view;
    }

    private List<Instructor> search(String name, Pageable pageable) {
        String text = name == null ? "" : name;
        return instructorRepository.searchByInstructor(text.toUpperCase(), pageable);
    }

    public List<InstructorResponse> getInstructors(List<Instructor> instructors) {
        List<InstructorResponse> responses = new ArrayList<>();
        for (Instructor instructor : instructors) {
            responses.add(new InstructorResponse(instructor.getInstructorId(), instructor.getFirstName(),
                    instructor.getLastName(), instructor.getPhoneNumber(), instructor.getEmail(),
                    instructor.getSpecialization(), instructor.getAuthInfo().getPassword(),
                    instructor.getCompany().getCompanyId(), instructor.getAuthInfo().getIsActive(),
                    instructor.getAuthInfo().getCreated()));
        }
        return responses;
    }

    public InstructorResponse getInstructorById(Long id) {
        return convertToResponse(instructorRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Instructor with id -%s not found", id)))));
    }

    @Transactional
    public InstructorResponse updateInstructor(Long id, InstructorRequest request) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Instructor with id - %s not found", id))));
        instructor.setFirstName(request.getFirstName());
        instructor.setLastName(request.getLastName());
        instructor.setEmail(request.getEmail());
        instructor.setPhoneNumber(request.getPhoneNumber());
        instructor.setSpecialization(request.getSpecialization());
        instructor.getAuthInfo().setPassword(passwordEncoder.encode(request.getPassword()));
        Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(
                () -> new NotFoundException((String.format("Company with id -%s not found", id))));
        company.addInstructor(instructor);
        instructor.setCompany(company);
        instructorRepository.save(instructor);
        return new InstructorResponse(instructor.getInstructorId(), instructor.getFirstName(),
                instructor.getLastName(), instructor.getPhoneNumber(), instructor.getEmail(),
                instructor.getSpecialization(), instructor.getAuthInfo().getPassword(),
                instructor.getCompany().getCompanyId(), instructor.getAuthInfo().getIsActive(),
                instructor.getAuthInfo().getCreated());
    }

    public InstructorResponse deleteById(Long id) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Instructor with id - %s not found", id))));
        instructor.setCompany(null);
        AuthInfo authInfo = authInfoRepository.findByEmail(instructor.getEmail()).orElseThrow(
                () -> new NotFoundException("User with email %s not found"));
        authInfo.getRoles().forEach(role -> {
            if (role.getRoleName().equals("INSTRUCTOR")) {
                authInfo.setRoles(null);
            } else {
                System.out.println("Not found");
            }
        });
        instructorRepository.delete(instructor);
        Instructor instructor1 = new Instructor();
        instructor1.setFirstName(instructor.getFirstName());
        instructor1.setLastName(instructor.getLastName());
        instructor1.setEmail(instructor.getEmail());
        instructor1.setPhoneNumber(instructor.getPhoneNumber());
        instructor1.setSpecialization(instructor.getSpecialization());
        instructor1.setCompany(instructor.getCompany());
        return new InstructorResponse(instructor1.getInstructorId(), instructor1.getFirstName(),
                instructor1.getLastName(), instructor1.getPhoneNumber(), instructor1.getEmail(),
                instructor1.getSpecialization(), null, null,
                instructor1.getAuthInfo().getIsActive(), instructor1.getAuthInfo().getCreated());
    }

    public List<Instructor> findAllInstructors() {
        return instructorRepository.findAll();
    }

    public String assignInstructorToCourse(Long instructorId, Long courseId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(
                () -> new NotFoundException((String.format("Instructor with id - %s not found", instructorId))));
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException((String.format("Course with id - %s not found", courseId))));
        instructor.addCourse(course);
        course.addInstructors(instructor);
        instructorRepository.save(instructor);
        return "Assign instructor to course was successfully!";
    }
}