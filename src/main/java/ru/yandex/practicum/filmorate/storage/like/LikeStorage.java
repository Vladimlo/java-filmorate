package ru.yandex.practicum.filmorate.storage.like;

import java.util.Set;

/*
 * Интерфейс описывает контракт для работы с хранилищем лайков
 * */
public interface LikeStorage {
    void addLike(Integer userId, Integer filmId);

    void removeLike(Integer userId, Integer filmId);

    Set<Integer> getLikes(Integer filmId);

    boolean isExist(Integer userId, Integer filmId);
}
