package com.test.webservices.restfulwebservices.webapp.repository;

import com.test.webservices.restfulwebservices.webapp.dto.User;
import com.test.webservices.restfulwebservices.webapp.dto.UserCourse;
import com.test.webservices.restfulwebservices.webapp.dto.UserCourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, UserCourseId> {
    List<UserCourse> findByUserCourseId_UserId(Integer userId);
}
