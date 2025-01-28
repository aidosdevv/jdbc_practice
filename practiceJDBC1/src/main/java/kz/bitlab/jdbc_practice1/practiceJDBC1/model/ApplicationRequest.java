package kz.bitlab.jdbc_practice1.practiceJDBC1.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationRequest {
    private Long id;
    private String userName;
    private String courseName;
    private String commentary;
    private String phone;
    private boolean handled;
    private Course course;
}
