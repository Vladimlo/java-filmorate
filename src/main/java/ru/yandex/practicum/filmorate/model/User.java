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
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
