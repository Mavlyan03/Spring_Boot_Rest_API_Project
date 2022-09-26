package com.example.spring_boot_rest_api_project.repository;

import com.example.spring_boot_rest_api_project.model.Company;
import com.example.spring_boot_rest_api_project.model.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("select v from Video v where upper(v.videoName) like concat('%',:text,'%') " +
            "or upper(v.link) like concat('%',:text,'%')" +
            "or upper(v.lesson.lessonName) like concat('%',:text,'%')")
    List<Video> searchByVideo(@Param("text") String text, Pageable pageable);
}