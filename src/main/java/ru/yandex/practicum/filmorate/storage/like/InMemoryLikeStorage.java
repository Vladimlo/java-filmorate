package ru.yandex.practicum.filmorate.storage.like;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class InMemoryLikeStorage implements LikeStorage {
    private Map<Integer, Set<Integer>> likes = new HashMap<>();

    @Override
    public void addLike(Integer userId, Integer filmId) {
        Set<Integer> filmLikes = likes.getOrDefault(filmId, new HashSet<>());
        filmLikes.add(userId);
        likes.put(filmId, filmLikes);
        log.info("Лайк пользователя {} фильму {} успешно добавлен", userId, filmId);
    }

    @Override
    public void removeLike(Integer userId, Integer filmId) {
        likes.get(filmId).remove(userId);
        log.info("Лайк пользователя {} фильму {} успешно удален", userId, filmId);
    }

    @Override
    public Set<Integer> getLikes(Integer filmId) {
        if (!likes.containsKey(filmId)) {
            return new HashSet<>();
        }

        return likes.get(filmId);
    }

    @Override
    public boolean isExist(Integer userId, Integer filmId) {
        return likes.containsKey(filmId) && likes.get(filmId).contains(userId);
    }
}
