package org.springboot.demo.todolist;

import jakarta.transaction.Transactional;
import org.springboot.demo.todolist.todos.Todo;
import org.springboot.demo.todolist.todos.TodoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("testdata")
@Component
public class DataInitializer implements CommandLineRunner {

    private final TodoRepository repository;

    public DataInitializer(TodoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        repository.deleteAllTodos();
        repository.save(new Todo("Buy tickets for the concert", false));
        repository.save(new Todo("Learn how to make a cheesecake", true));
        repository.save(new Todo("Replace the bicycle light batteries", false));
    }
}
