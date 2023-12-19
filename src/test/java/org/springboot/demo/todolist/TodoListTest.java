package org.springboot.demo.todolist;

import org.springboot.demo.todolist.todos.Todo;
import org.springboot.demo.todolist.todos.TodoNotFoundException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        properties = {"spring.datasource.url=jdbc:tc:postgresql:16.0:///todos_db"}
)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles({"testdata"})
class TodoListTest {

    @LocalServerPort
    int port;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void a_whenGetTodosThenGetTodoList() {
        final List<Todo> expectedResult = new ArrayList<>();
        var todo = new Todo("Buy tickets for the concert", false);
        todo.setId(1L);
        expectedResult.add(todo);

        todo = new Todo("Learn how to make a cheesecake", true);
        todo.setId(2L);
        expectedResult.add(todo);

        todo = new Todo("Replace the bicycle light batteries", false);
        todo.setId(3L);
        expectedResult.add(todo);

        webTestClient
                .get()
                .uri("/api/v1/todos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Todo.class).value(result -> {
                    assertThat(result).hasSameElementsAs(expectedResult);
                });
    }

    @Test
    void b_whenPostTodoThenCreated() {
        var addedTodo = new Todo("Test Todo", false);

        webTestClient
                .post()
                .uri("/api/v1/todos")
                .bodyValue(addedTodo)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().value("location", result -> {
                    assertThat(result.endsWith(":" + port + "/api/v1/todos/4")).isTrue();
                })
                .expectBody(Todo.class).value(actualTodo -> {
                    assertThat(actualTodo).isNotNull();
                    assertThat(actualTodo.getTitle()).isEqualTo(addedTodo.getTitle());
                    assertThat(actualTodo.getCompleted()).isEqualTo(addedTodo.getCompleted());
                    assertThat(actualTodo.getId()).isEqualTo(4);
                });
    }

    @Test
    void c_whenGetTodoByIdThenGetTodo() {
        var expectedTodo = new Todo("Buy tickets for the concert", false);
        expectedTodo.setId(1L);

        webTestClient
                .get()
                .uri("/api/v1/todos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Todo.class).value(actualTodo -> {
                    assertThat(actualTodo).isNotNull();
                    assertThat(actualTodo).isEqualTo(expectedTodo);
                });
    }

    @Test
    void d_whenPatchTodoThenPatched() {
        var expectedTodo = new Todo("Title Patched", false);
        expectedTodo.setId(1L);

        webTestClient
                .patch()
                .uri("/api/v1/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"title\":\"" + expectedTodo.getTitle() + "\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Todo.class).value(actualTodo -> {
                    assertThat(actualTodo).isNotNull();
                    assertThat(actualTodo).isEqualTo(expectedTodo);
                });
    }

    @Test
    void e_whenPutTodoThenUpdated() {
        var expectedTodo = new Todo("Title updated", true);
        expectedTodo.setId(1L);

        webTestClient
                .put()
                .uri("/api/v1/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(expectedTodo)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Todo.class).value(actualTodo -> {
                    assertThat(actualTodo).isNotNull();
                    assertThat(actualTodo).isEqualTo(expectedTodo);
                });
    }

    @Test
    void f_whenDeleteTodoThenDeleted() {
        webTestClient
                .delete()
                .uri("/api/v1/todos/2")
                .exchange()
                .expectStatus().isNoContent();

        WebTestClient.BodyContentSpec notFound = webTestClient
                .get()
                .uri("/api/v1/todos/2")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody();

        TodoNotFoundException expected = new TodoNotFoundException(2L);

        notFound.jsonPath("status").isEqualTo("404");
        notFound.jsonPath("path").isEqualTo("/api/v1/todos/2");
        notFound.jsonPath("message").isEqualTo(expected.getMessage());
    }
}
