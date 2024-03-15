package com.glushkov.http_crud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table()
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties
public class File extends BaseItem{
    private String name;
    private String filePath;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "event_id", referencedColumnName = "id")
    @JsonIgnore
    private Event event;
}
