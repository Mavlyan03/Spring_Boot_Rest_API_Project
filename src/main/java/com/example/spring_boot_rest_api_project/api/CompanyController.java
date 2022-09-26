package com.example.spring_boot_rest_api_project.api;

import com.example.spring_boot_rest_api_project.dto.request.CompanyRequest;
import com.example.spring_boot_rest_api_project.dto.response.CompanyResponse;
import com.example.spring_boot_rest_api_project.dto.view.CompanyResponseView;
import com.example.spring_boot_rest_api_project.model.Company;
import com.example.spring_boot_rest_api_project.service.CompanyServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company")
@Tag(name = "Company API", description = "ADMIN can create,update,delete students")
@PreAuthorize("hasAuthority('ADMIN')")
public class CompanyController {

    private final CompanyServiceImpl service;

    @PostMapping
    @Operation(description = "ADMIN can create the company")
    public CompanyResponse create(@RequestBody CompanyRequest student) {
        return service.saveCompany(student);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(description = "MANAGER and ADMIN can get company by id")
    public CompanyResponse getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(description = "ADMIN can update the company")
    public CompanyResponse update(@PathVariable("id") Long id, @RequestBody CompanyRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("{id}")
    @Operation(description = "ADMIN can delete the company")
    public CompanyResponse deleteById(@PathVariable("id") Long id) {
        return service.deleteById(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    @Operation(description = "MANAGER and ADMIN can find all companies")
    public List<Company> findAll() {
        return service.findAllCompanies();
    }

    @GetMapping("/searchByCompany")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(description = "ADMIN can searching the companies")
    public CompanyResponseView getAllPagination(@RequestParam(value = "text", required = false) String text,
                                                @RequestParam int page,
                                                @RequestParam int size) {
        return service.getAllCompaniesPagination(text, page, size);
    }
}
