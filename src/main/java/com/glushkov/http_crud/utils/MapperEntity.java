package com.glushkov.http_crud.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glushkov.http_crud.dto.EventDto;
import com.glushkov.http_crud.dto.FileDto;
import com.glushkov.http_crud.dto.UserDto;
import com.glushkov.http_crud.model.Event;
import com.glushkov.http_crud.model.File;
import com.glushkov.http_crud.model.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class MapperEntity {
    static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    public  static FileDto convertToFileDto(File file){
        FileDto fileDto = new FileDto();
        fileDto.setId(file.getId());
        fileDto.setName(file.getName());
        fileDto.setFilePath(file.getFilePath());
        return fileDto;
    }

    public static Set<FileDto> convertToFilesDto(Collection<File> files){
       return files.stream().map(MapperEntity::convertToFileDto).collect(Collectors.toSet());
    }

    public static Set<EventDto> convertToEventsDto(Collection<Event> files){
        return files.stream().map(MapperEntity::convertToEventDto).collect(Collectors.toSet());
    }

    public static Set<UserDto> convertToUsersDto(Collection<User> files){
        return files.stream().map(MapperEntity::convertToUserDto).collect(Collectors.toSet());
    }



    public static EventDto convertToEventDto(Event event){
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setFile(convertToFileDto(event.getFile()));
        return eventDto;
    }

    public static UserDto convertToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        Set<Event> events = user.getEvents();
        if (events != null) {
            userDto.setEvents(user.getEvents().stream().map(MapperEntity::convertToEventDto).collect(Collectors.toSet()));
        }
        return userDto;
    }

    public static ObjectMapper getObjectMapper(){
            return objectMapper;
    }
}
