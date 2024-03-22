package com.glushkov.http_crud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties
public class User extends BaseItem{
    private String name;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.DETACH, CascadeType.REMOVE})
    @Fetch(FetchMode.SUBSELECT)
    private Set<Event> events;

    public User(Long id, String name, Date created, Date updated, Status status) {
        super(id, created, updated, status);
        this.name = name;
    }

}
