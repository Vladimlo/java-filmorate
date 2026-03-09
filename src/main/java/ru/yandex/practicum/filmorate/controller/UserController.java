package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Поступил запрос на получение всех пользователей");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        log.info("Поступил запрос на получение пользователя с id = {}", id);
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User newUser) {
        log.info("Поступил запрос на создание пользователя {}", newUser);
        return userService.create(newUser);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Поступил запрос на обновление пользователя с id {}", newUser.getId());
        return userService.update(newUser);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Integer id) {
        log.info("Поступил запрос на получение друзей пользователя с id = {}", id);
        return userService.getFriendsById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Collection<User> addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Поступил запрос на добавление друга с id = {} пользователю с id = {}", friendId, id);
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public Collection<User> deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Поступил запрос на удаление друга с id = {} пользователю с id = {}", friendId, id);
        return userService.deleteFriends(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Поступил запрос на получение списка общих друзей для пользователей с id = {}, {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
