package com.example.spring_boot_rest_api_project.repository;

import com.example.spring_boot_rest_api_project.model.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select c from Course c where upper(c.courseName) like concat('%',:text,'%') " +
            "or upper(c.company.locatedCountry) like concat('%',:text,'%') or upper(c.description) like concat('%',:text,'%') " +
            "or upper(c.image) like concat('%',:text,'%') or upper(c.company.companyName) like concat('%',:text,'%')")
    List<Course> searchByCourse(@RequestParam("text") String text, Pageable pageable);
}