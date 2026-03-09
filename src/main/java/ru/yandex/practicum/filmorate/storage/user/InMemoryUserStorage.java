package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User getUserById(Integer userId) {
        return users.get(userId);
    }

    @Override
    public User create(User newUser) {
        newUser.setId(getNextId());

        users.put(newUser.getId(), newUser);
        log.info("Успешно создан новый пользователь c id: {}", newUser.getId());

        return newUser;
    }

    @Override
    public User update(User newUser) {
        User oldUser = users.get(newUser.getId());

        LocalDate newBirthday = newUser.getBirthday();

        oldUser.setName(newUser.getName());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        if (newUser.getBirthday() != null) oldUser.setBirthday(newBirthday);
        log.info("Пользователь с id {} успешно обновлен", newUser.getId());

        return oldUser;
    }

    public boolean isExist(Integer userId) {
        return users.containsKey(userId);
    }

    private int getNextId() {
        int maxId = users.keySet()
                .stream()
                .max(Integer::compareTo)
                .orElse(0);

        return ++maxId;
    }
}