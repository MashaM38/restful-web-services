package com.test.webservices.restfulwebservices.webapp.dto;

import io.swagger.annotations.ApiModel;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@ApiModel(description = "User Course main details")
@Entity
@Table(name = "USERCOURSE")
public class UserCourse extends RepresentationModel<UserCourse> {

    @EmbeddedId
    private UserCourseId userCourseId;

    public UserCourse() {};

    public UserCourse(UserCourseId userCourseId) {
        this.userCourseId = userCourseId;
    }

    public UserCourseId getUserCourseId() {
        return userCourseId;
    }

    public void setUserCourseId(UserCourseId userCourseId) {
        this.userCourseId = userCourseId;
    }

    @Override
    public String toString() {
        return "UserCourse{" +
                "userCourseId=" + userCourseId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserCourse that = (UserCourse) o;
        return userCourseId.equals(that.userCourseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userCourseId);
    }
}
