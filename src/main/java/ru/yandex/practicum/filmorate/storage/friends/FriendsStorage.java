package ru.yandex.practicum.filmorate.storage.friends;

import java.util.Collection;

/*
 * Интерфейс описывает контракт для работы с хранилищем друзей
 * */
public interface FriendsStorage {
    Collection<Integer> getFriendsById(Integer id);

    Collection<Integer> addFriend(Integer userId, Integer friendId);

    Collection<Integer> deleteFriendsById(Integer userId, Integer friendId);
}
