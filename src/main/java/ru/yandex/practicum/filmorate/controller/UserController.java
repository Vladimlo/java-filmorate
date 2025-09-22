package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }


    @PostMapping
    public User create(@RequestBody User newUser) {
        log.info("Поступил запрос на создание пользователя {}", newUser);
        userCheck(newUser);
        newUser.setId(getNextId());

        users.put(newUser.getId(), newUser);
        log.info("Успешно создан новый пользователь c id: {}", newUser.getId());

        return newUser;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Поступил запрос на обновление пользователя с id {}", newUser.getId());
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            userCheck(newUser);

            LocalDate newBirthday = newUser.getBirthday();

            oldUser.setName(newUser.getName());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            if (newUser.getBirthday() != null) oldUser.setBirthday(newBirthday);
            log.info("Пользователь с id {} успешно обновлен", newUser.getId());

            return oldUser;
        }

        log.warn("Отсутствует пользователь с id {}", newUser.getId());
        throw new NullPointerException("Пользователь с таким id не найден");
    }

    private int getNextId() {
        int maxId = users.keySet()
                .stream()
                .max(Integer::compareTo)
                .orElse(0);

        return ++maxId;
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
}
