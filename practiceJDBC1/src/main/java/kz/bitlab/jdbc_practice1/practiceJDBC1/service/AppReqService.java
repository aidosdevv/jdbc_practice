package kz.bitlab.jdbc_practice1.practiceJDBC1.service;

import kz.bitlab.jdbc_practice1.practiceJDBC1.model.ApplicationRequest;
import kz.bitlab.jdbc_practice1.practiceJDBC1.model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AppReqService {

    private final Connection connection;
    private final ConfigurableServletWebServerFactory configurableServletWebServerFactory;

    public List<ApplicationRequest> getAllApplicationRequest(){
        List<ApplicationRequest> list = new ArrayList<>();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT a.id AS app_id,a.userName,a.courseName,a.commentary,a.phone,a.course_id,c.name,c.price FROM t_application AS a INNER JOIN t_course AS c ON a;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                ApplicationRequest app = ApplicationRequest
                                        .builder()
                                        .id(resultSet.getLong("id"))
                                        .userName(resultSet.getString("userName"))
                                        .courseName(resultSet.getString("courseName"))
                                        .commentary(resultSet.getString("commentary"))
                                        .phone(resultSet.getString("phone"))
                                        .handled(resultSet.getBoolean("handled"))
                                        .course(
                                                Course.builder()
                                                        .id(resultSet.getLong("courseId"))
                                                        .name(resultSet.getString("name"))
                                                        .description(resultSet.getString("description"))
                                                        .price(resultSet.getInt("price"))
                                                        .build()
                                        )
                                        .build();
                list.add(app);

            }
            preparedStatement.close();

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public boolean addRequest(ApplicationRequest app){
        try{
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO t_application(id,userName,courseName,commentary,phone,handled,course_id) VALUES(DEFAULT,?,?,?,?,?,?);");

            stmt.setString(1,app.getUserName());
            stmt.setString(2,app.getCourseName());
            stmt.setString(3, app.getCommentary());
            stmt.setString(4, app.getPhone());
            stmt.setBoolean(5, app.isHandled());
            stmt.setLong(6, app.getCourse().getId());

            stmt.executeUpdate();
            stmt.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ApplicationRequest updateRequest(ApplicationRequest app){
        try{
            PreparedStatement smtm = connection.prepareStatement("UPDATE t_application SET userName=?,courseName=?,commentary=?,phone=?,handled=?,course_id=? WHERE id=?;");
            smtm.setString(1,app.getUserName());
            smtm.setString(2,app.getCourseName());
            smtm.setString(3,app.getCommentary());
            smtm.setString(4,app.getPhone());
            smtm.setBoolean(5,app.isHandled());
            smtm.setLong(6,app.getCourse().getId());
            smtm.setLong(7,app.getId());

            smtm.executeUpdate();
            ResultSet rs = smtm.getGeneratedKeys();
            if(rs.next()){
                ApplicationRequest updatedReq = ApplicationRequest.builder()
                        .id(rs.getLong("id"))
                        .userName(rs.getString("userName"))
                        .courseName(rs.getString("courseName"))
                        .commentary(rs.getString("commentary"))
                        .phone(rs.getString("phone"))
                        .handled(rs.getBoolean("handled"))
                        .course(Course
                                .builder()
                                .id(rs.getLong("courseId"))
                                .name(rs.getString("name"))
                                .description(rs.getString("description"))
                                .price(rs.getInt("price"))
                                .build())
                        .build();
                app = updatedReq;
            }
            smtm.close();

        }catch (Exception e){
            e.printStackTrace();

        }
        return app;
    }


    public ApplicationRequest getRequestById(Long id){
        try{
            PreparedStatement stmm = connection.prepareStatement("SELECT a.id AS app_id,a.userName,a.courseName,a.commentary,a.phone,a.course_id,c.name,c.price FROM t_application AS a INNER JOIN t_course AS c ON a.course_id = c.id where a.id=?;");
            stmm.setLong(1, id);
            ResultSet resultSet = stmm.executeQuery();
            if(resultSet.next()){
                ApplicationRequest req = ApplicationRequest
                        .builder()
                        .id(resultSet.getLong("id"))
                        .userName(resultSet.getString("userName"))
                        .courseName(resultSet.getString("courseName"))
                        .commentary(resultSet.getString("commentary"))
                        .phone(resultSet.getString("phone"))
                        .handled(resultSet.getBoolean("handled"))
                        .course(Course
                                .builder()
                                .id(resultSet.getLong("courseId"))
                                .name(resultSet.getString("name"))
                                .description(resultSet.getString("description"))
                                .price(resultSet.getInt("price"))
                                .build()
                        )
                        .build();
                stmm.close();
                resultSet.close();
                return req;
            }else{
                stmm.close();
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }


    public boolean deleteRequestById(Long id){
        try{
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM t_application WHERE id=?;");
            stmt.setLong(1, id);
            stmt.executeUpdate();
            stmt.close();

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public ApplicationRequest updateReqHandled(Long id, boolean handled){
        try{
            PreparedStatement stmt = connection.prepareStatement("UPDATE t_application SET handled=? WHERE id=?;");
            stmt.setBoolean(1, handled);
            stmt.setLong(2, id);
            stmt.executeUpdate();
            ResultSet resultSet = stmt.getGeneratedKeys();
            if(resultSet.next()){
                ApplicationRequest req = ApplicationRequest
                        .builder()
                        .id(resultSet.getLong("id"))
                        .userName(resultSet.getString("userName"))
                        .courseName(resultSet.getString("courseName"))
                        .commentary(resultSet.getString("commentary"))
                        .phone(resultSet.getString("phone"))
                        .handled(resultSet.getBoolean("handled"))
                        .course(Course
                                .builder()
                                .id(resultSet.getLong("courseId"))
                                .name(resultSet.getString("name"))
                                .description(resultSet.getString("description"))
                                .price(resultSet.getInt("price"))
                                .build())
                        .build();
                stmt.close();
                resultSet.close();
                return req;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }



    public List<ApplicationRequest> getAllHandledRequest(boolean isActive){
        List<ApplicationRequest> list = new ArrayList<>();
        try{
            PreparedStatement stmt = connection.prepareStatement("SELECT a.id AS app_id,a.userName,a.courseName,a.commentary,a.phone,a.course_id,c.name,c.price FROM t_application AS a INNER JOIN t_course AS c ON a.course_id = c.id where a.handled=?;");
            stmt.setBoolean(1, isActive);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                ApplicationRequest req = ApplicationRequest
                        .builder()
                        .id(resultSet.getLong("id"))
                        .userName(resultSet.getString("userName"))
                        .courseName(resultSet.getString("courseName"))
                        .commentary(resultSet.getString("commentary"))
                        .phone(resultSet.getString("phone"))
                        .handled(resultSet.getBoolean("handled"))
                        .course(Course
                                .builder()
                                .id(resultSet.getLong("courseId"))
                                .name(resultSet.getString("name"))
                                .description(resultSet.getString("description"))
                                .price(resultSet.getInt("price"))
                                .build())
                        .build();
                list.add(req);
            }
            stmt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }



}

