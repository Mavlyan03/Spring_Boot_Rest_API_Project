package com.example.spring_boot_rest_api_project.repository;

import com.example.spring_boot_rest_api_project.model.Company;
import com.example.spring_boot_rest_api_project.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select t from Task t where upper(t.taskName) like concat('%',:text,'%') " +
            "or upper(t.taskText) like concat('%',:text,'%')" +
            "or upper(t.lessons.lessonName) like concat('%',:text,'%')")
    List<Task> searchByTask(@Param("text") String text, Pageable pageable);
}