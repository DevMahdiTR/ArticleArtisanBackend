package com.stage.elearning.service.affiche;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stage.elearning.dto.affiche.AfficheDTO;
import com.stage.elearning.dto.affiche.AfficheDTOMapper;
import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.model.affiche.Affiche;
import com.stage.elearning.model.file.FileData;
import com.stage.elearning.repository.AfficheRepository;
import com.stage.elearning.service.file.FileService;
import com.stage.elearning.utility.CustomResponseEntity;
import com.stage.elearning.utility.CustomResponseList;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {AfficheServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AfficheServiceImplTest {
    @MockBean
    private AfficheDTOMapper afficheDTOMapper;

    @MockBean
    private AfficheRepository afficheRepository;

    @Autowired
    private AfficheServiceImpl afficheServiceImpl;

    @MockBean
    private FileService fileService;


    @Test
    void testCreateAffiche4() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> afficheServiceImpl.createAffiche(null, "Json Affiche"));
    }

    @Test
    void testCreateAffiche5() throws IOException {
        DataInputStream contentStream = mock(DataInputStream.class);
        when(contentStream.readAllBytes()).thenReturn("AXAXAXAX".getBytes("UTF-8"));
        doNothing().when(contentStream).close();
        assertThrows(IllegalArgumentException.class,
                () -> afficheServiceImpl.createAffiche(new MockMultipartFile("Name", contentStream), null));
        verify(contentStream).readAllBytes();
        verify(contentStream).close();
    }


    @Test
    void testFetchAfficheImage() throws IOException {
        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Affiche affiche = new Affiche();
        affiche.setId(1L);
        affiche.setImage(image);
        affiche.setTitle("Dr");
        Optional<Affiche> ofResult = Optional.of(affiche);
        when(afficheRepository.fetchAfficheById(anyLong())).thenReturn(ofResult);
        when(fileService.getFileDataById(anyLong())).thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> afficheServiceImpl.fetchAfficheImage(1L));
        verify(afficheRepository).fetchAfficheById(anyLong());
        verify(fileService).getFileDataById(anyLong());
    }

    @Test
    void testFetchAfficheImage2() throws IOException {
        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Affiche affiche = new Affiche();
        affiche.setId(1L);
        affiche.setImage(image);
        affiche.setTitle("Dr");
        Optional<Affiche> ofResult = Optional.of(affiche);
        when(afficheRepository.fetchAfficheById(anyLong())).thenReturn(ofResult);

        FileData fileData = new FileData();
        fileData.setFilePath("/directory/foo.txt");
        fileData.setId(1L);
        fileData.setName("Name");
        fileData.setType("Type");
        when(fileService.getFileDataById(anyLong())).thenReturn(fileData);
        when(fileService.downloadFile(Mockito.<FileData>any())).thenReturn(null);
        assertNull(afficheServiceImpl.fetchAfficheImage(1L));
        verify(afficheRepository).fetchAfficheById(anyLong());
        verify(fileService).getFileDataById(anyLong());
        verify(fileService).downloadFile(Mockito.<FileData>any());
    }

    @Test
    void testFetchAfficheImage3() throws IOException {
        when(afficheRepository.fetchAfficheById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> afficheServiceImpl.fetchAfficheImage(1L));
        verify(afficheRepository).fetchAfficheById(anyLong());
    }


    @Test
    void testModifyAfficheById3() throws IOException {
        when(afficheRepository.fetchAfficheById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> afficheServiceImpl.modifyAfficheById(
                        new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))), 1L,
                        "Json Affiche Details"));
        verify(afficheRepository).fetchAfficheById(anyLong());
    }

    @Test
    void testModifyAfficheById4() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> afficheServiceImpl.modifyAfficheById(
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))), 1L, null));
    }


    @Test
    void testFetchAfficheById() {
        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Affiche affiche = new Affiche();
        affiche.setId(1L);
        affiche.setImage(image);
        affiche.setTitle("Dr");
        Optional<Affiche> ofResult = Optional.of(affiche);
        when(afficheRepository.fetchAfficheById(anyLong())).thenReturn(ofResult);
        AfficheDTO afficheDTO = new AfficheDTO(1L, "Dr", new FileData());

        when(afficheDTOMapper.apply(Mockito.<Affiche>any())).thenReturn(afficheDTO);
        ResponseEntity<CustomResponseEntity<AfficheDTO>> actualFetchAfficheByIdResult = afficheServiceImpl
                .fetchAfficheById(1L);
        assertTrue(actualFetchAfficheByIdResult.hasBody());
        assertTrue(actualFetchAfficheByIdResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchAfficheByIdResult.getStatusCodeValue());
        CustomResponseEntity<AfficheDTO> body = actualFetchAfficheByIdResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertSame(afficheDTO, body.getData());
        verify(afficheRepository).fetchAfficheById(anyLong());
        verify(afficheDTOMapper).apply(Mockito.<Affiche>any());
    }

    @Test
    void testFetchAfficheById2() {
        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Affiche affiche = new Affiche();
        affiche.setId(1L);
        affiche.setImage(image);
        affiche.setTitle("Dr");
        Optional<Affiche> ofResult = Optional.of(affiche);
        when(afficheRepository.fetchAfficheById(anyLong())).thenReturn(ofResult);
        when(afficheDTOMapper.apply(Mockito.<Affiche>any())).thenThrow(new IllegalArgumentException("foo"));
        assertThrows(IllegalArgumentException.class, () -> afficheServiceImpl.fetchAfficheById(1L));
        verify(afficheRepository).fetchAfficheById(anyLong());
        verify(afficheDTOMapper).apply(Mockito.<Affiche>any());
    }

    @Test
    void testFetchAfficheById3() {
        when(afficheRepository.fetchAfficheById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> afficheServiceImpl.fetchAfficheById(1L));
        verify(afficheRepository).fetchAfficheById(anyLong());
    }

    @Test
    void testSearchAffiche() {
        when(afficheRepository.fetchAllAfficheWithPrefix(Mockito.<String>any())).thenReturn(new ArrayList<>());
        ResponseEntity<CustomResponseEntity<List<AfficheDTO>>> actualSearchAfficheResult = afficheServiceImpl
                .searchAffiche("Prefix");
        assertTrue(actualSearchAfficheResult.hasBody());
        assertTrue(actualSearchAfficheResult.getHeaders().isEmpty());
        assertEquals(200, actualSearchAfficheResult.getStatusCodeValue());
        CustomResponseEntity<List<AfficheDTO>> body = actualSearchAfficheResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertTrue(body.getData().isEmpty());
        verify(afficheRepository).fetchAllAfficheWithPrefix(Mockito.<String>any());
    }


    @Test
    void testFetchAllAffiche() {
        when(afficheRepository.fetchAllAffiche(Mockito.<Pageable>any())).thenReturn(new ArrayList<>());
        when(afficheRepository.getTotalAfficheCount()).thenReturn(3L);
        ResponseEntity<CustomResponseList<AfficheDTO>> actualFetchAllAfficheResult = afficheServiceImpl
                .fetchAllAffiche(1L);
        assertTrue(actualFetchAllAfficheResult.hasBody());
        assertTrue(actualFetchAllAfficheResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchAllAfficheResult.getStatusCodeValue());
        CustomResponseList<AfficheDTO> body = actualFetchAllAfficheResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertTrue(body.getData().isEmpty());
        assertEquals(0L, body.getCurrentLength());
        assertEquals(3L, body.getTotal());
        assertEquals(200L, body.getStatus());
        verify(afficheRepository).fetchAllAffiche(Mockito.<Pageable>any());
        verify(afficheRepository).getTotalAfficheCount();
    }

    @Test
    void testFetchAllAffiche2() {
        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Affiche affiche = new Affiche();
        affiche.setId(1L);
        affiche.setImage(image);
        affiche.setTitle("Dr");

        ArrayList<Affiche> afficheList = new ArrayList<>();
        afficheList.add(affiche);
        when(afficheRepository.fetchAllAffiche(Mockito.<Pageable>any())).thenReturn(afficheList);
        when(afficheRepository.getTotalAfficheCount()).thenReturn(3L);
        when(afficheDTOMapper.apply(Mockito.<Affiche>any())).thenReturn(new AfficheDTO(1L, "Dr", new FileData()));
        ResponseEntity<CustomResponseList<AfficheDTO>> actualFetchAllAfficheResult = afficheServiceImpl
                .fetchAllAffiche(1L);
        assertTrue(actualFetchAllAfficheResult.hasBody());
        assertTrue(actualFetchAllAfficheResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchAllAfficheResult.getStatusCodeValue());
        CustomResponseList<AfficheDTO> body = actualFetchAllAfficheResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(1, body.getData().size());
        assertEquals(1L, body.getCurrentLength());
        assertEquals(3L, body.getTotal());
        assertEquals(200L, body.getStatus());
        verify(afficheRepository).fetchAllAffiche(Mockito.<Pageable>any());
        verify(afficheRepository).getTotalAfficheCount();
        verify(afficheDTOMapper).apply(Mockito.<Affiche>any());
    }


    @Test
    void testDeleteAfficheById() throws IOException {
        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Affiche affiche = new Affiche();
        affiche.setId(1L);
        affiche.setImage(image);
        affiche.setTitle("Dr");
        Optional<Affiche> ofResult = Optional.of(affiche);
        doNothing().when(afficheRepository).deleteAfficheById(anyLong());
        when(afficheRepository.fetchAfficheById(anyLong())).thenReturn(ofResult);
        doNothing().when(fileService).deleteFileFromFileSystem(Mockito.<FileData>any());
        ResponseEntity<CustomResponseEntity<String>> actualDeleteAfficheByIdResult = afficheServiceImpl
                .deleteAfficheById(1L);
        assertTrue(actualDeleteAfficheByIdResult.hasBody());
        assertTrue(actualDeleteAfficheByIdResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteAfficheByIdResult.getStatusCodeValue());
        CustomResponseEntity<String> body = actualDeleteAfficheByIdResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertEquals("The Affiche with ID: 1 has been deleted successfully.", body.getData());
        verify(afficheRepository).fetchAfficheById(anyLong());
        verify(afficheRepository).deleteAfficheById(anyLong());
        verify(fileService).deleteFileFromFileSystem(Mockito.<FileData>any());
    }

    @Test
    void testDeleteAfficheById2() throws IOException {
        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Affiche affiche = new Affiche();
        affiche.setId(1L);
        affiche.setImage(image);
        affiche.setTitle("Dr");
        Optional<Affiche> ofResult = Optional.of(affiche);
        doNothing().when(afficheRepository).deleteAfficheById(anyLong());
        when(afficheRepository.fetchAfficheById(anyLong())).thenReturn(ofResult);
        doThrow(new IllegalArgumentException("The Affiche with ID: %d has been deleted successfully.")).when(fileService)
                .deleteFileFromFileSystem(Mockito.<FileData>any());
        assertThrows(IllegalArgumentException.class, () -> afficheServiceImpl.deleteAfficheById(1L));
        verify(afficheRepository).fetchAfficheById(anyLong());
        verify(afficheRepository).deleteAfficheById(anyLong());
        verify(fileService).deleteFileFromFileSystem(Mockito.<FileData>any());
    }
    @Test
    void testDeleteAfficheById3() throws IOException {
        when(afficheRepository.fetchAfficheById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> afficheServiceImpl.deleteAfficheById(1L));
        verify(afficheRepository).fetchAfficheById(anyLong());
    }
}

