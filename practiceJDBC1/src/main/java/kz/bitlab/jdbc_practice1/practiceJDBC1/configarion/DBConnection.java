package kz.bitlab.jdbc_practice1.practiceJDBC1.configarion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
public class DBConnection {

    private Connection connection;

    public DBConnection() {
        try{
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jdbc1_DB",
                    "postgres",
                    "root");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Bean
    public Connection getConnection() {
        return connection;
    }
}
