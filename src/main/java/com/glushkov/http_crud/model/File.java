package com.glushkov.http_crud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

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
    @JsonIgnore
    private Event event;

    @Lob
    private byte[] data;
}
