package com.recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @Pattern(regexp = "[A-Za-z0-9]{24}")
    private String userId;
    @NonNull
    @NotNull
    private String name;
    @NonNull
    @NotNull
    @Size(min = 2, max = 15, message = "Username should be less than 15 symbols")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Username should contain only characters")
    private String username;
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$"
            , message = "Password should contain at least one special symbol and one number")
    @Size(min = 8, message = "Password should be at least 8 symbols")
    private String password;
    @NonNull
    @NotNull
    private Gender gender;
    private Role role;
    @NonNull
    @URL
    private String imageURL;
    @Size(max = 512, message = "Personal info should be max 512 symbols")
    private String personalInfo;
    private AccountStatus active = AccountStatus.ACTIVE;
    @PastOrPresent
    private LocalDateTime created = LocalDateTime.now();
    @PastOrPresent
    private LocalDateTime modified = LocalDateTime.now();

    public User(@NonNull String name,
                @Size(min = 2, max = 15, message = "Username should be less than 15 symbols")
                @Pattern(regexp = "^[a-zA-Z]+$", message = "Username should contain only characters")
                        String username,
                @NotNull
                @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
                @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$"
                        , message = "Password should contain at least one special symbol and one number")
                @Size(min = 8, message = "Password should be at least 8 symbols")
                        String password,
                Role role,
                @NonNull @NotNull Gender gender,
                @NonNull @URL String imageURL) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.gender = gender;
        this.imageURL = imageURL;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.toString())));
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return active == AccountStatus.ACTIVE;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return active == AccountStatus.ACTIVE;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return active == AccountStatus.ACTIVE;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return active == AccountStatus.ACTIVE;
    }
}
