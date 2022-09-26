package com.example.spring_boot_rest_api_project.model;

import com.example.spring_boot_rest_api_project.validation.PasswordValid;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "auth_info")
@NoArgsConstructor
@Getter
@Setter
public class AuthInfo implements UserDetails {
    @Id
    @GeneratedValue(generator = "auth_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "auth_seq",sequenceName = "auth_seq",allocationSize = 1)
    private Long id;

    private String username;
    private String email;
    @PasswordValid
    private String password;
    @CreatedDate
    private LocalDateTime created;
    private Boolean isActive = true;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "auth_info_roles",
            joinColumns = @JoinColumn(name = "auth_info_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public AuthInfo(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public AuthInfo(String username, String email, String password, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }


    public void addRoleToAuthInfo(Role role) {
        if(this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);

    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        for(Role role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
