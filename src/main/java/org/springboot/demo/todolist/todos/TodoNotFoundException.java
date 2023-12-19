package org.springboot.demo.todolist.todos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TodoNotFoundException extends Exception {
    public static final String MESSAGE = "The Todo with id %d was not found.";

    public TodoNotFoundException(long id) {
        super(String.format(MESSAGE, id));
    }
}
