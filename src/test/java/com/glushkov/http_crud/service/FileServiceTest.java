package com.glushkov.http_crud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.glushkov.http_crud.model.File;
import com.glushkov.http_crud.model.Status;
import com.glushkov.http_crud.repository.FileRepository;
import com.glushkov.http_crud.repository.impl.ORMFileRepositoryImpl;
import jakarta.servlet.http.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileService fileServiceMock;

    private final String uploadPath = "C:\\project java\\HTTP_CRUD\\src\\main\\resources\\files";

    @Test
    void whenGivenId_shouldReturnFileData() {
        File file = new com.glushkov.http_crud.model.File(1L, "first", uploadPath + java.io.File.separator + "first.txt",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE);
        java.io.File fileData = new java.io.File(file.getFilePath());
        when(fileRepository.getByID(file.getId())).thenReturn(file);
        java.io.File fileResult = fileService.getByID(file.getId());
        assertEquals(fileData, fileResult);
        verify(fileRepository).getByID(file.getId());
    }
    @Test
    void whenGivenId_shouldReturnNull() {
        File file = new com.glushkov.http_crud.model.File(2L, "first", uploadPath + java.io.File.separator + "first.txt",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE);
        when(fileServiceMock.getByID(file.getId())).thenReturn(null);
        java.io.File fileResult = fileService.getByID(file.getId());
        assertNull(fileResult);
        verify(fileRepository).getByID(file.getId());
    }
    @Test
    void whenSaveFile_shouldReturnFile() throws IOException {
        File file = new com.glushkov.http_crud.model.File(1L, "first", uploadPath + java.io.File.separator + "first.txt",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE);
        when(fileServiceMock.save(any(), any(Long.class))).thenReturn(file);
        File created = fileServiceMock.save(null, 1L);
        assertEquals(file, created);
    }

    @Test
    void updateFile_whenPutFile() throws IOException {
        File file = new com.glushkov.http_crud.model.File(1L, "first", uploadPath + java.io.File.separator + "first.txt",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE);
        File updateFile = new com.glushkov.http_crud.model.File(1L, "first update", uploadPath + java.io.File.separator + "first.txt",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE);
        when(fileServiceMock.edit(any(), any(Long.class))).thenReturn(updateFile);
        File edited = fileServiceMock.edit(null, 1L);
        assertEquals(file.getId(), edited.getId());
        assertEquals(updateFile, edited);
    }

    @Test
    void whenGivenId_shouldDeleteFile_ifFound() throws JsonProcessingException {
        File file = new File(1L, "first", uploadPath + java.io.File.separator + "first.txt",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE);
        when(fileRepository.delete(ArgumentMatchers.any(Long.class))).thenReturn(true);
        boolean deleted = fileService.delete(file.getId());
        assertTrue(deleted);
        verify(fileRepository).delete(file.getId());
    }

    @Test
    void shouldReturnAllFiles() throws JsonProcessingException {
        List<File> files = new ArrayList<>();
        files.add(new File(1L, "first", uploadPath + java.io.File.separator + "first.txt",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE));
        files.add(new File(2L, "first", uploadPath + java.io.File.separator + "second.txt",
                Date.valueOf(LocalDate.now()), null, Status.ACTIVE));
        Set<java.io.File> filesData = new HashSet<>();
        filesData.add(new java.io.File(files.get(0).getFilePath()));
        filesData.add(new java.io.File(files.get(1).getFilePath()));
        when(fileRepository.getAll()).thenReturn(files);
        Set<java.io.File> fileResult = fileService.getAll();
        assertEquals(filesData, fileResult);
        verify(fileRepository).getAll();
    }
}