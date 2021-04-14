package com.test.webservices.restfulwebservices.webapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;

@ApiModel(description = "Course main details")
@Entity
public class Course extends RepresentationModel<Course> {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author=" + author +
                ", user=" + user +
                '}';
    }
}
