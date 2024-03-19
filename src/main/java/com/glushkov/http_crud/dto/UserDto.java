package com.glushkov.http_crud.dto;

import com.glushkov.http_crud.model.Event;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private long id;
    private String name;

    private Set<EventDto> events;
}
