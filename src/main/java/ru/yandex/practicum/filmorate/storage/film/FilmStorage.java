package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

/*
* Интерфейс описывает контракт для работы с хранилищем фильмов
* */
public interface FilmStorage {
    Collection<Film> getAll();

    Film create(Film newFilm);

    Film update(Film newFilm);

    boolean isExist(Integer filmId);
}
