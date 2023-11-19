package org.demo.todolist.todos;

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
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoResource {
    private final TodoRepository repository;

    public TodoResource(TodoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Todo>> getAll() {
        List<Todo> all = repository.findAll();

        return ResponseEntity.ok(all);
    }

    @GetMapping("{id}")
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

    @PatchMapping("/{id}")
    public ResponseEntity<Todo> patchResource(@PathVariable Long id, @RequestBody Map<String, Object> updates) throws TodoNotFoundException {
        Optional<Todo> optTodo = repository.findById(id);
        Todo found = optTodo.orElseThrow(() -> new TodoNotFoundException(id));
        if (updates.containsKey("completed")) {
            found.setCompleted((Boolean) updates.get("completed"));
        }
        if (updates.containsKey("title")) {
            found.setTitle((String) updates.get("title"));
        }

        found = repository.save(found);

        return ResponseEntity
                .ok(found);
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
