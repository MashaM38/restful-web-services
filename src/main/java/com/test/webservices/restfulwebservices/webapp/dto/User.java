package com.test.webservices.restfulwebservices.webapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@ApiModel(description = "User main details")
@Entity
public class User extends RepresentationModel<User> {

    protected User(){};

    public User(Integer id, String name, String surname, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    @Id
    private Integer id;

    private String name;

    @ApiModelProperty(notes = "Name should have at least 2 characters")
    @Size(min = 2, message = "Name should have at least 2 characters!")
    private String surname;

    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Course> courses;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", courses=" + courses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                name.equals(user.name) &&
                surname.equals(user.surname) &&
                email.equals(user.email) &&
                Objects.equals(courses, user.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, surname, email, courses);
    }
}
