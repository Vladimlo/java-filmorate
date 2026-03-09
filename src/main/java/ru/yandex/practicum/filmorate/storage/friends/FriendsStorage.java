package ru.yandex.practicum.filmorate.storage.friends;

import java.util.Collection;

public interface FriendsStorage {
    public Collection<Integer> getFriendsById(Integer id);

    public Collection<Integer> addFriend(Integer userId, Integer friendId);

    public Collection<Integer> deleteFriendsById(Integer userId, Integer friendId);
}
