package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final UserService userService;

    public FilmService(FilmStorage filmStorage,
                       LikeStorage likeStorage,
                       UserService userService) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
        this.userService = userService;
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film newFilm) {
        checkFields(newFilm);
        return filmStorage.create(newFilm);
    }

    public Film update(Film newFilm) {
        filmExistCheck(newFilm.getId());
        checkFields(newFilm);
        return filmStorage.update(newFilm);
    }

    public Collection<Film> getMostPopularFilms(Integer count) {
        return filmStorage.getAll().stream()
                .sorted((a, b) -> Integer.compare(
                        likeStorage.getLikes(b.getId()).size(),
                        likeStorage.getLikes(a.getId()).size()))
                .limit(count)
                .toList();
    }

    public void addLike(Integer filmId, Integer userId) {
        filmExistCheck(filmId);
        userService.userExistCheck(userId);

        if (!likeStorage.isExist(userId, filmId)) {
            likeStorage.addLike(userId, filmId);
        } else {
            throw new ValidationException(String.format("Пользователь %d уже лайкнул фильм %d", userId, filmId));
        }
    }

    public void removeLike(Integer filmId, Integer userId) {
        filmExistCheck(filmId);
        userService.userExistCheck(userId);

        if (likeStorage.isExist(userId, filmId)) {
            likeStorage.removeLike(userId, filmId);
        } else {
            throw new NotFoundException(String.format("Пользователь %d не лайкал фильм %d", userId, filmId));
        }
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

    private void filmExistCheck(Integer filmId) {
        if (!filmStorage.isExist(filmId)) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
    }
}
