package com.example.spring_boot_rest_api_project.repository;

import com.example.spring_boot_rest_api_project.model.AuthInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthInfoRepository extends JpaRepository<AuthInfo, Long> {
    Optional<AuthInfo> findByEmail(String email);

    @Query("select a from AuthInfo a join a.roles r where r.roleName = ?1")
    List<AuthInfo> findAllByRole(String roleName);

    @Query("select a from AuthInfo a where upper(a.username) like concat('%',:text,'%')" +
            "or upper(a.email) like concat('%',:text,'%')")
    List<AuthInfo> searchByAuth(@Param("text") String text, Pageable pageable);

}