package com.example.spring_boot_rest_api_project.repository;

import com.example.spring_boot_rest_api_project.model.Instructor;
import com.example.spring_boot_rest_api_project.model.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    @Query("select i from Instructor i join Company c on i.company.companyName like c.companyName where c.companyName = ?1")
    List<Instructor> findStudentsByCompany(String companyName);

    @Query("select i from Instructor i where upper(i.firstName) like concat('%',:text,'%') " +
            "or upper(i.lastName) like concat('%',:text,'%') or upper(i.email) like concat('%',:text,'%') " +
            "or upper(i.specialization) like concat('%',:text,'%') or upper(i.phoneNumber) like concat('%',:text,'%')" +
            "or upper(i.company.companyName) like concat('%',:text,'%')")
    List<Instructor> searchByInstructor(@Param("text") String text, Pageable pageable);
}