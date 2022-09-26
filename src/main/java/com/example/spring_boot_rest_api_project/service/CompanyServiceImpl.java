package com.example.spring_boot_rest_api_project.service;

import com.example.spring_boot_rest_api_project.dto.request.CompanyRequest;
import com.example.spring_boot_rest_api_project.dto.response.CompanyResponse;
import com.example.spring_boot_rest_api_project.dto.view.CompanyResponseView;
import com.example.spring_boot_rest_api_project.exception.NotFoundException;
import com.example.spring_boot_rest_api_project.model.Company;
import com.example.spring_boot_rest_api_project.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CompanyServiceImpl {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyResponse saveCompany(CompanyRequest request) {
        Company company = new Company();
        company.setCompanyName(request.getCompanyName());
        company.setLocatedCountry(request.getLocatedCountry());
        Company company1 = companyRepository.save(company);
        return new CompanyResponse(company1.getCompanyId(), company1.getCompanyName(), company1.getLocatedCountry());
    }

    public CompanyResponseView getAllCompaniesPagination(String text, int page, int size) {
        CompanyResponseView view = new CompanyResponseView();
        Pageable pageable = PageRequest.of(page - 1, size);
        view.setCompanyResponses(getCompanies(search(String.valueOf(Sort.by(text)), pageable)));
        return view;
    }

    private List<Company> search(String name, Pageable pageable) {
        String text = name == null ? "" : name;
        return companyRepository.searchByCompany(text.toUpperCase(), pageable);
    }

    public List<CompanyResponse> getCompanies(List<Company> companies) {
        List<CompanyResponse> responses = new ArrayList<>();
        for (Company company : companies) {
            responses.add(new CompanyResponse(company.getCompanyId(),
                    company.getCompanyName(), company.getLocatedCountry()));
        }
        return responses;
    }

    public CompanyResponse getById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Company with id - %s not found", id))));
        return new CompanyResponse(company.getCompanyId(), company.getCompanyName(), company.getLocatedCountry());
    }

    public CompanyResponse update(Long id, CompanyRequest request) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Company with id - %s not found", id))));
        company.setCompanyName(request.getCompanyName());
        company.setLocatedCountry(request.getLocatedCountry());
        companyRepository.save(company);
        return new CompanyResponse(company.getCompanyId(), company.getCompanyName(), company.getLocatedCountry());
    }

    public CompanyResponse deleteById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Company with id - %s not found", id))));
        companyRepository.delete(company);
        return new CompanyResponse(company.getCompanyId(), company.getCompanyName(), company.getLocatedCountry());
    }

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

}