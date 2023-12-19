package org.springboot.demo.todolist.todos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("DELETE FROM Todo")
    @Modifying
    void deleteAllTodos();
}
