package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Film.
 */
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class Film {
    int id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
}
