package org.springboot.demo.todolist;

import org.springboot.demo.todolist.config.TodoListProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.TimeZone;

@SpringBootApplication
@EnableConfigurationProperties(TodoListProperties.class)
public class TodoApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication application = new SpringApplication(TodoApplication.class);
        application.addListeners(new PrintVersionInfo());
        application.run(args);
    }
}
