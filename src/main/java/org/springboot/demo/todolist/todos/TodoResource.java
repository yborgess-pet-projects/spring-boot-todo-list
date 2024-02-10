package org.springboot.demo.todolist.todos;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.JsonMergePatch;
import jakarta.json.JsonValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/todos")
public class TodoResource {
    private final TodoRepository repository;
    private final ObjectMapper mapper;

    public TodoResource(TodoRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<Todo>> getAll() {
        List<Todo> all = repository.findAll();

        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getById(@PathVariable Long id) throws TodoNotFoundException {
        Optional<Todo> optional = repository.findById(id);

        Todo todo = optional.orElseThrow(() -> new TodoNotFoundException(id));

        return ResponseEntity.ok(todo);
    }

    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        Todo saved = repository.save(todo);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(saved);
    }

    @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Todo> patchResource(@PathVariable Long id, @RequestBody JsonMergePatch updates) throws TodoNotFoundException {
        Optional<Todo> optTodo = repository.findById(id);
        Todo found = optTodo.orElseThrow(() -> new TodoNotFoundException(id));

        JsonValue target = mapper.convertValue(found, JsonValue.class);
        JsonValue patched = updates.apply(target);
        Todo patchedTodo = mapper.convertValue(patched, Todo.class);
        patchedTodo = repository.save(patchedTodo);

        return ResponseEntity
                .ok(patchedTodo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> putResource(@PathVariable Long id, @RequestBody Todo todo) {
        todo.setId(id);
        Todo saved = repository.save(todo);

        return ResponseEntity
                .ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent()
                .build();
    }
}
