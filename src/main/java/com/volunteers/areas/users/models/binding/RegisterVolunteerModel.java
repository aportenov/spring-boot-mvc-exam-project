package com.volunteers.areas.users.models.binding;


import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RegisterVolunteerModel {

    private Long id;

    private String username;

    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String countryName;

    @NotBlank
    private String gender;

    @NotNull
    @Min(value = 17,message = "You must be over 17 to participate")
    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
