package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

/*
 * Интерфейс описывает контракт для работы с хранилищем пользователей
 * */
public interface UserStorage {
    Collection<User> getAll();

    User getUserById(Integer userId);

    User create(User newUser);

    User update(User newUser);

    boolean isExist(Integer userId);
}
