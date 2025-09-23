package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film newFilm) {
        log.info("Получен запрос на создание фильма: {}", newFilm);
        checkFields(newFilm);
        newFilm.setId(getNextId());

        films.put(newFilm.getId(), newFilm);
        log.info("Успешно создан фильм с id: {}", newFilm.getId());

        return newFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Получен запрос на обновление фильма с id {}", newFilm.getId());
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            checkFields(newFilm);

            String newDescription = newFilm.getDescription();
            LocalDate newFilmReleaseDate = newFilm.getReleaseDate();

            oldFilm.setName(newFilm.getName());
            oldFilm.setDuration(newFilm.getDuration());
            if (newDescription != null) oldFilm.setDescription(newDescription);
            if (newFilmReleaseDate != null) oldFilm.setReleaseDate(newFilmReleaseDate);

            log.info("Фильм с id {} успешно обновлен", newFilm.getId());
            return oldFilm;
        }

        log.warn("Не найден фильм с id {}", newFilm.getId());
        throw new NullPointerException("Фильм с таким id не найден");
    }

    private int getNextId() {
        int maxId = films.keySet()
                .stream()
                .max(Integer::compareTo)
                .orElse(0);

        return ++maxId;
    }

    private void checkFields(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Не корректное значение name: {}", film.getName());
            throw new ValidationException("Название не может быть пустым");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Не корректное значение description: {}", film.getDescription());
            throw new ValidationException("Максимальная длинна описания - 200 символов");
        }

        if (film.getDuration() <= 0) {
            log.warn("Не корректное значение duration: {}", film.getDuration());
            throw new ValidationException("Продолжительность должна быть положительным числом");
        }

        if (film.getReleaseDate() != null
                && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Не корректное значение releaseDate: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }
}
