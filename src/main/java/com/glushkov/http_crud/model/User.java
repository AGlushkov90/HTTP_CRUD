package com.glushkov.http_crud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "User_name")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties
public class User extends BaseItem{
    private String name;

    @OneToMany(mappedBy = "user")
    private Set<Event> events;
}
