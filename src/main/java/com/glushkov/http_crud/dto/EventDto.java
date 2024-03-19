package com.glushkov.http_crud.dto;

import com.glushkov.http_crud.model.File;
import com.glushkov.http_crud.model.User;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventDto {
    private long id;
    private User user;

    private FileDto file;

}
