package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@RequestBody Film newFilm) {
        log.info("Получен запрос на создание фильма: {}", newFilm);
        return filmService.create(newFilm);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Получен запрос на обновление фильма с id {}", newFilm.getId());
        return filmService.update(newFilm);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос {} самых популярных фильмов", count);
        return filmService.getMostPopularFilms(count);
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable("filmId") Integer filmId, @PathVariable("userId") Integer userId) {
        log.info("Получен запрос на добавление лайка пользователя {} фильму {}", userId, filmId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable("filmId") Integer filmId, @PathVariable("userId") Integer userId) {
        log.info("Получен запрос на удаление лайка пользователя {} с фильма {}", userId, filmId);
        filmService.removeLike(filmId, userId);
    }
}
