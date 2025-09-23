package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private URI uri;

    Film defaultFilm;

    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();


    @BeforeEach
    void setUp() {
        uri = URI.create(String.format("http://localhost:%d/films", port));

        defaultFilm = Film.builder()
                .name("Test")
                .description("Test")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .build();
    }

    @Test
    public void getTest() throws IOException, InterruptedException {
        assertEquals(200, sendGetRequest().statusCode());
    }

    @Test
    public void createTest() throws IOException, InterruptedException {
        assertEquals(200, sendPostRequest(defaultFilm).statusCode());
    }

    @Test
    public void createWithEmptyNameTest() throws IOException, InterruptedException {
        assertEquals(500, sendPostRequest(defaultFilm.toBuilder()
                .name(null)
                .build()).statusCode());
    }

    @Test
    public void createWithLongDescriptionTest() throws IOException, InterruptedException {
        assertEquals(500, sendPostRequest(defaultFilm.toBuilder()
                .description("a".repeat(201))
                .build()).statusCode());
    }

    @Test
    public void createWithOldReleaseDateTest() throws IOException, InterruptedException {
        assertEquals(500, sendPostRequest(defaultFilm.toBuilder()
                .releaseDate(LocalDate.of(1895, 12, 27))
                .build()).statusCode());
    }

    @Test
    public void createWithZeroDurationTest() throws IOException, InterruptedException {
        assertEquals(500, sendPostRequest(defaultFilm.toBuilder()
                .duration(0)
                .build()).statusCode());
    }

    @Test
    public void updateTest() throws IOException, InterruptedException {
        int id = objectMapper.readValue(sendPostRequest(defaultFilm).body(), Film.class).getId();

        String expectedDescription = "updated";

        HttpResponse<String> response = sendPutRequest(defaultFilm.toBuilder()
                .id(id)
                .description(expectedDescription)
                .build());

        String actualDescription = objectMapper.readValue(response.body(), Film.class).getDescription();

        assertEquals(200, response.statusCode());
        assertEquals(expectedDescription, actualDescription);
    }

    private HttpResponse<String> sendGetRequest() throws IOException, InterruptedException {
        try (HttpClient client = HttpClient.newHttpClient()) {
            return client.send(HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build(), handler);
        }
    }

    private HttpResponse<String> sendPostRequest(Film film) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(film);

        try (HttpClient client = HttpClient.newHttpClient()) {
            return client.send(HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .uri(uri)
                    .setHeader("Content-Type", "application/json")
                    .build(), handler);
        }
    }

    private HttpResponse<String> sendPutRequest(Film film) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(film);

        try (HttpClient client = HttpClient.newHttpClient()) {
            return client.send(HttpRequest.newBuilder()
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .uri(uri)
                    .setHeader("Content-Type", "application/json")
                    .build(), handler);
        }
    }
}
