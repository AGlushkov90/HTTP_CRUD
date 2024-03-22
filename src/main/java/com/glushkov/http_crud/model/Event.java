package com.glushkov.http_crud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties
public class Event extends BaseItem{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.REMOVE})
    @JoinColumn( name = "file_id", referencedColumnName = "id")
    private File file;

    public Event(Long id, File file, Date created, Date updated, Status status) {
        super(id, created, updated, status);
        this.file = file;
    }
}
