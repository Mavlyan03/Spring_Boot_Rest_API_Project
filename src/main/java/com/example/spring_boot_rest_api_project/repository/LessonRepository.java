package com.example.spring_boot_rest_api_project.repository;

import com.example.spring_boot_rest_api_project.model.Company;
import com.example.spring_boot_rest_api_project.model.Lesson;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("select l from Lesson l where upper(l.lessonName) like concat('%',:text,'%') " +
            "or upper(l.courses.courseName) like concat('%',:text,'%')")
    List<Lesson> searchByLesson(@Param("text")String text, Pageable pageable);


}