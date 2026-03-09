package ru.yandex.practicum.filmorate.storage.like;

import java.util.Set;

public interface LikeStorage {
    public void addLike(Integer userId, Integer filmId);

    public void removeLike(Integer userId, Integer filmId);

    public Set<Integer> getLikes(Integer filmId);

    public boolean isExist(Integer userId, Integer filmId);
}
