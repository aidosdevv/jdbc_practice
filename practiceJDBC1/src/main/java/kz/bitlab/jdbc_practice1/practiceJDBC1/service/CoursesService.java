package kz.bitlab.jdbc_practice1.practiceJDBC1.service;

import kz.bitlab.jdbc_practice1.practiceJDBC1.model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CoursesService {

    private final Connection connection;

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM t_course");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Course course = Course
                        .builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .description(resultSet.getString("description"))
                        .price(resultSet.getInt("price"))
                        .build();
                courses.add(course);
            }
            preparedStatement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return courses;
    }

    public Course getCourseById(Long id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM t_course WHERE id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Course course = Course.builder()
                                .id(resultSet.getLong("id"))
                                .name(resultSet.getString("name"))
                                .description(resultSet.getString("description"))
                                .price(resultSet.getInt("price"))
                                .build();
                return course;
            }
            preparedStatement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
