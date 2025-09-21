package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class User {
    int id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}
