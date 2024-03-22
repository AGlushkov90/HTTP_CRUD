package com.glushkov.http_crud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties
public class File extends BaseItem{
    private String name;
    private String filePath;

    @OneToOne(mappedBy = "file")
    private Event event;

    public File(Long id, String name, String filePath, Date created, Date updated, Status status) {
        super(id, created, updated, status);
        this.name = name;
        this.filePath = filePath;
    }
}
