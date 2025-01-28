package kz.bitlab.jdbc_practice1.practiceJDBC1.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Course {
    private Long id;
    private String name;
    private String description;
    private int price;
}
