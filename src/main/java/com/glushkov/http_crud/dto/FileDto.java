package com.glushkov.http_crud.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.glushkov.http_crud.model.Event;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileDto {
    private long id;

    private String name;
    private String filePath;

    private Event event;
}
