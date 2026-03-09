package ru.yandex.practicum.filmorate.storage.friends;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class InMemoryFriendsStorage implements FriendsStorage {

    private Map<Integer, Set<Integer>> friends = new HashMap<>();

    @Override
    public Collection<Integer> getFriendsById(Integer id) {
        friends.put(id, friends.getOrDefault(id, new HashSet<>()));

        return friends.get(id);
    }

    @Override
    public Collection<Integer> addFriend(Integer userId, Integer friendId) {
        friends.put(userId, friends.getOrDefault(userId, new HashSet<>()));
        friends.put(friendId, friends.getOrDefault(friendId, new HashSet<>()));

        friends.get(userId).add(friendId);
        friends.get(friendId).add(userId);

        log.info("Пользователи {} и {} успешно добавлены в друзья", userId, friendId);

        return friends.get(userId);
    }

    @Override
    public Collection<Integer> deleteFriendsById(Integer userId, Integer friendId) {
        friends.put(userId, friends.getOrDefault(userId, new HashSet<>()));
        friends.put(friendId, friends.getOrDefault(friendId, new HashSet<>()));

        friends.get(userId).remove(friendId);
        friends.get(friendId).remove(userId);

        log.info("Пользователи {} и {} успешно удалены из друзей", userId, friendId);

        return friends.get(userId);
    }
}
