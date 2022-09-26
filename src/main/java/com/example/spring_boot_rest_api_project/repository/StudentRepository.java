package com.example.spring_boot_rest_api_project.repository;

import com.example.spring_boot_rest_api_project.model.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("select s from Student s join Course c on s.course.courseName like c.courseName where c.courseName = ?1")
    List<Student> findStudentsByCourse(String courseName);

    @Query("select s from Student s join Company c on s.company.companyName like c.companyName where c.companyName = ?1")
    List<Student> findStudentsByCompany(String companyName);

//    @Query("select s from Student s where upper(s.firstName) like concat('%',:text,'%')" +
//            "or upper(s.lastName) like concat('%',:text,'%') or upper(s.email) like concat('%',:text,'%')" +
//            "or upper(s.phoneNumber) like concat('%',:text,'%') or upper(s.studyFormat) like concat('%',:text,'%')" +
//            "or upper(s.company.companyName) like concat('%',:text,'%') or upper(s.course.courseName) like concat('%',:text,'%')")
//    List<Student> searchByStudent(@Param("text") String text, Pageable pageable);

    @Query("select s from Student s where upper(s.firstName) like concat('%',:text,'%')" +
            "or upper(s.lastName) like concat('%',:text,'%') or upper(s.email) like concat('%',:text,'%')" +
            "or upper(s.studyFormat) like concat('%',:text,'%') or upper(s.company.companyName) like concat('%',:text,'%')")
    List<Student> searchByStudent(@Param("text")String text, Pageable pageable);
}