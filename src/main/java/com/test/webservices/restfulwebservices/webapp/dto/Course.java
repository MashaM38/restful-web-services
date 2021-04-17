package com.test.webservices.restfulwebservices.webapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;

@ApiModel(description = "Course main details")
@Entity
public class Course extends RepresentationModel<Course> {
    @Id
    //TODO: @GeneratedValue
    private Integer id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Author author;

    public Course() {
    }

    public Course(Integer id, String name, Author author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }

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

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author=" + author +
                '}';
    }
}
