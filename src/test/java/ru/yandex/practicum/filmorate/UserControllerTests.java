package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private URI uri;

    User defaultUser;

    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    @BeforeEach
    void setUp() {
        uri = URI.create(String.format("http://localhost:%d/users", port));

        defaultUser = User.builder()
                .email("test@test.test")
                .login("Test")
                .name("nameTest")
                .birthday(LocalDate.of(1994, 1, 1))
                .build();
    }

    @Test
    public void getTest() throws IOException, InterruptedException {
        assertEquals(200, sendGetRequest().statusCode());
    }

    @Test
    public void createTest() throws IOException, InterruptedException {
        assertEquals(200, sendPostRequest(defaultUser).statusCode());
    }

    @Test
    public void createWithEmptyEmailTest() throws IOException, InterruptedException {
        assertEquals(500, sendPostRequest(defaultUser.toBuilder().
                email(null)
                .build()).statusCode());
    }

    @Test
    public void createWithEmptyLoginTest() throws IOException, InterruptedException {
        assertEquals(500, sendPostRequest(defaultUser.toBuilder().
                login(null)
                .build()).statusCode());
    }

    @Test
    public void createWithEmptyNameTest() throws IOException, InterruptedException {
        HttpResponse<String> response = sendPostRequest(defaultUser.toBuilder().
                name(null)
                .build());

        String expectedName = defaultUser.getLogin();
        String actualName = objectMapper.readValue(response.body(), User.class).getName();

        assertEquals(200, response.statusCode());
        assertEquals(expectedName, actualName);
    }

    @Test
    public void createWithFutureBirthdayTest() throws IOException, InterruptedException {
        assertEquals(500, sendPostRequest(defaultUser.toBuilder()
                .birthday(LocalDate.now().plusDays(1))
                .build()).statusCode());
    }

    @Test
    public void updateTest() throws IOException, InterruptedException {
        int id = objectMapper.readValue(sendPostRequest(defaultUser).body(), User.class).getId();

        String expectedName = "updated";

        HttpResponse<String> response = sendPutRequest(defaultUser.toBuilder()
                .id(id)
                .name(expectedName)
                .build());

        String actualName = objectMapper.readValue(response.body(), User.class).getName();

        assertEquals(200, response.statusCode());
        assertEquals(expectedName, actualName);
    }

    private HttpResponse<String> sendGetRequest() throws IOException, InterruptedException {
        try (HttpClient client = HttpClient.newHttpClient()) {
            return client.send(HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build(), handler);
        }
    }

    private HttpResponse<String> sendPostRequest(User user) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(user);

        try (HttpClient client = HttpClient.newHttpClient()) {
            return client.send(HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .uri(uri)
                    .setHeader("Content-Type", "application/json")
                    .build(), handler);
        }
    }

    private HttpResponse<String> sendPutRequest(User user) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(user);

        try (HttpClient client = HttpClient.newHttpClient()) {
            return client.send(HttpRequest.newBuilder()
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .uri(uri)
                    .setHeader("Content-Type", "application/json")
                    .build(), handler);
        }
    }
}
