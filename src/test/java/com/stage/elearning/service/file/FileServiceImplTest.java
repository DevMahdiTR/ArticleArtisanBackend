package com.stage.elearning.service.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.model.file.FileData;
import com.stage.elearning.repository.FileDataRepository;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;

import java.io.IOException;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {FileServiceImpl.class})
@ExtendWith(SpringExtension.class)
class FileServiceImplTest {
    @MockBean
    private FileDataRepository fileDataRepository;

    @Autowired
    private FileServiceImpl fileServiceImpl;


    @Test
    void testSave() {
        FileData fileData = new FileData();
        fileData.setFilePath("/directory/foo.txt");
        fileData.setId(1L);
        fileData.setName("Name");
        fileData.setType("Type");
        when(fileDataRepository.save(Mockito.<FileData>any())).thenReturn(fileData);

        FileData fileData2 = new FileData();
        fileData2.setFilePath("/directory/foo.txt");
        fileData2.setId(1L);
        fileData2.setName("Name");
        fileData2.setType("Type");
        assertSame(fileData, fileServiceImpl.save(fileData2));
        verify(fileDataRepository).save(Mockito.<FileData>any());
    }


    @Test
    void testSave2() {
        when(fileDataRepository.save(Mockito.<FileData>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        FileData fileData = new FileData();
        fileData.setFilePath("/directory/foo.txt");
        fileData.setId(1L);
        fileData.setName("Name");
        fileData.setType("Type");
        assertThrows(ResourceNotFoundException.class, () -> fileServiceImpl.save(fileData));
        verify(fileDataRepository).save(Mockito.<FileData>any());
    }

    @Test
    void testDeleteFileById() {
        FileData fileData = new FileData();
        fileData.setFilePath("/directory/foo.txt");
        fileData.setId(1L);
        fileData.setName("Name");
        fileData.setType("Type");
        Optional<FileData> ofResult = Optional.of(fileData);
        doNothing().when(fileDataRepository).deleteFileDataById(anyLong());
        when(fileDataRepository.fetchFileDataById(anyLong())).thenReturn(ofResult);
        fileServiceImpl.deleteFileById(1L);
        verify(fileDataRepository).fetchFileDataById(anyLong());
        verify(fileDataRepository).deleteFileDataById(anyLong());
    }


    @Test
    void testDeleteFileById2() {
        FileData fileData = new FileData();
        fileData.setFilePath("/directory/foo.txt");
        fileData.setId(1L);
        fileData.setName("Name");
        fileData.setType("Type");
        Optional<FileData> ofResult = Optional.of(fileData);
        doThrow(new ResourceNotFoundException("An error occurred")).when(fileDataRepository)
                .deleteFileDataById(anyLong());
        when(fileDataRepository.fetchFileDataById(anyLong())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> fileServiceImpl.deleteFileById(1L));
        verify(fileDataRepository).fetchFileDataById(anyLong());
        verify(fileDataRepository).deleteFileDataById(anyLong());
    }


    @Test
    void testDeleteFileById3() {
        when(fileDataRepository.fetchFileDataById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> fileServiceImpl.deleteFileById(1L));
        verify(fileDataRepository).fetchFileDataById(anyLong());
    }

    @Test
    void testProcessUploadedFile3() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> fileServiceImpl.processUploadedFile(null));
    }

    @Test
    void testProcessUploadedFile5() throws IOException, IllegalStateException {
        FileData fileData = new FileData();
        fileData.setFilePath("/directory/foo.txt");
        fileData.setId(1L);
        fileData.setName("Name");
        fileData.setType("Type");
        when(fileDataRepository.save(Mockito.<FileData>any())).thenReturn(fileData);
        MockMultipartFile file = mock(MockMultipartFile.class);
        when(file.getContentType()).thenReturn("text/plain");
        doNothing().when(file).transferTo(Mockito.<File>any());
        when(file.getOriginalFilename()).thenReturn("foo.txt");
        FileData actualProcessUploadedFileResult = fileServiceImpl.processUploadedFile(file);
        assertEquals("text/plain", actualProcessUploadedFileResult.getType());
        assertEquals("foo.txt", actualProcessUploadedFileResult.getName());
        assertEquals(0L, actualProcessUploadedFileResult.getId());
        verify(fileDataRepository).save(Mockito.<FileData>any());
        verify(file).getContentType();
        verify(file, atLeast(1)).getOriginalFilename();
        verify(file).transferTo(Mockito.<File>any());
    }


    @Test
    void testDownloadFile2() throws IOException {
        assertThrows(IllegalArgumentException.class,
                () -> (new FileServiceImpl(mock(FileDataRepository.class))).downloadFile(null));
    }

    @Test
    void testDeleteFileFromFileSystem2() throws IOException {
        assertThrows(IllegalArgumentException.class,
                () -> (new FileServiceImpl(mock(FileDataRepository.class))).deleteFileFromFileSystem(null));
    }


    @Test
    void testDetermineContentType() {
        assertEquals("application/octet-stream", fileServiceImpl.determineContentType("text/plain"));
        assertThrows(IllegalArgumentException.class, () -> fileServiceImpl.determineContentType(null));
    }

    @Test
    void testGetFileDataById() {
        FileData fileData = new FileData();
        fileData.setFilePath("/directory/foo.txt");
        fileData.setId(1L);
        fileData.setName("Name");
        fileData.setType("Type");
        Optional<FileData> ofResult = Optional.of(fileData);
        when(fileDataRepository.fetchFileDataById(anyLong())).thenReturn(ofResult);
        assertSame(fileData, fileServiceImpl.getFileDataById(1L));
        verify(fileDataRepository).fetchFileDataById(anyLong());
    }

    @Test
    void testGetFileDataById2() {
        when(fileDataRepository.fetchFileDataById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> fileServiceImpl.getFileDataById(1L));
        verify(fileDataRepository).fetchFileDataById(anyLong());
    }

    @Test
    void testGetFileDataById3() {
        when(fileDataRepository.fetchFileDataById(anyLong()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> fileServiceImpl.getFileDataById(1L));
        verify(fileDataRepository).fetchFileDataById(anyLong());
    }
}

