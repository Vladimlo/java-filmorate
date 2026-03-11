package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film create(Film newFilm) {
        newFilm.setId(getNextId());

        films.put(newFilm.getId(), newFilm);
        log.info("Успешно создан фильм с id: {}", newFilm.getId());

        return newFilm;
    }

    @Override
    public Film update(Film newFilm) {
        Film oldFilm = films.get(newFilm.getId());

        String newDescription = newFilm.getDescription();
        LocalDate newFilmReleaseDate = newFilm.getReleaseDate();

        oldFilm.setName(newFilm.getName());
        oldFilm.setDuration(newFilm.getDuration());
        if (newDescription != null) oldFilm.setDescription(newDescription);
        if (newFilmReleaseDate != null) oldFilm.setReleaseDate(newFilmReleaseDate);

        log.info("Фильм с id {} успешно обновлен", newFilm.getId());
        return oldFilm;
    }

    @Override
    public boolean isExist(Integer filmId) {
        return films.containsKey(filmId);
    }

    private int getNextId() {
        int maxId = films.keySet()
                .stream()
                .max(Integer::compareTo)
                .orElse(0);

        return ++maxId;
    }
}
