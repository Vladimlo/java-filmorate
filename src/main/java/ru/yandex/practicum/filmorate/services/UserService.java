package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User getUserById(int userId) {
        userExistCheck(userId);

        return userStorage.getUserById(userId);
    }

    public User create(User newUser) {
        userCheck(newUser);

        return userStorage.create(newUser);
    }

    public User update(User newUser) {
        userExistCheck(newUser.getId());
        userCheck(newUser);

        return userStorage.update(newUser);
    }

    public Collection<User> getFriendsById(Integer userId) {
        userExistCheck(userId);

        return friendsStorage.getFriendsById(userId)
                .stream()
                .map(this::getUserById)
                .toList();
    }

    public Collection<User> addFriends(Integer userId, Integer friendId) {
        if (userId.equals(friendId)) {
            throw new ValidationException("Нельзя добавить самого себя в друзья");
        }

        userExistCheck(userId);
        userExistCheck(friendId);

        return friendsStorage.addFriend(userId, friendId)
                .stream()
                .map(this::getUserById)
                .toList();
    }

    public Collection<User> deleteFriends(Integer userId, Integer friendId) {
        userExistCheck(userId);
        userExistCheck(friendId);

        return friendsStorage.deleteFriendsById(userId, friendId)
                .stream()
                .map(this::getUserById)
                .toList();
    }

    public Collection<User> getCommonFriends(Integer firstUserId, Integer secondUserId) {
        userExistCheck(firstUserId);
        userExistCheck(secondUserId);

        Collection<Integer> firstUserFriends = friendsStorage.getFriendsById(firstUserId);
        Collection<Integer> secondUserFriends = friendsStorage.getFriendsById(secondUserId);

        return firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .map(this::getUserById)
                .toList();
    }

    private void userCheck(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Не корректное значение email: {}", user.getEmail());
            throw new ValidationException("Email должен содержать \"@\"");
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Не корректное значение login: {}", user.getLogin());
            throw new ValidationException("Логин не должен быть пустым или содержать пробелы");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Не корректное значение birthDay: {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть больше текущей");
        }

        if (user.getName() == null) user.setName(user.getLogin());
    }

    public void userExistCheck(Integer userId) {
        if (!userStorage.isExist(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
    }
}
