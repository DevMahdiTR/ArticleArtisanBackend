package com.stage.elearning.service.categorie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stage.elearning.dto.categorie.CategorieDTO;
import com.stage.elearning.dto.categorie.CategorieDTOMapper;
import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.model.acticle.Article;
import com.stage.elearning.model.categories.Categorie;
import com.stage.elearning.model.file.FileData;
import com.stage.elearning.repository.ArticleRepository;
import com.stage.elearning.repository.CategorieRepository;
import com.stage.elearning.utility.CustomResponseEntity;
import com.stage.elearning.utility.CustomResponseList;

import java.time.LocalDate;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CategorieServiceImpl.class})
@ExtendWith(SpringExtension.class)
class CategorieServiceImplTest {
    @MockBean
    private ArticleRepository articleRepository;

    @MockBean
    private CategorieDTOMapper categorieDTOMapper;

    @MockBean
    private CategorieRepository categorieRepository;

    @Autowired
    private CategorieServiceImpl categorieServiceImpl;

    @Test
    void testCreateCategorie() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        when(categorieRepository.save(Mockito.<Categorie>any())).thenReturn(categorie);
        CategorieDTO categorieDTO = new CategorieDTO(1L, "Name", "The characteristics of someone or something");

        when(categorieDTOMapper.apply(Mockito.<Categorie>any())).thenReturn(categorieDTO);

        Categorie categorie2 = new Categorie();
        categorie2.setArticles(new ArrayList<>());
        categorie2.setDescription("The characteristics of someone or something");
        categorie2.setId(1L);
        categorie2.setName("Name");
        ResponseEntity<CustomResponseEntity<CategorieDTO>> actualCreateCategorieResult = categorieServiceImpl
                .createCategorie(categorie2);
        assertTrue(actualCreateCategorieResult.hasBody());
        assertTrue(actualCreateCategorieResult.getHeaders().isEmpty());
        assertEquals(200, actualCreateCategorieResult.getStatusCodeValue());
        CustomResponseEntity<CategorieDTO> body = actualCreateCategorieResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertSame(categorieDTO, body.getData());
        verify(categorieRepository).save(Mockito.<Categorie>any());
        verify(categorieDTOMapper).apply(Mockito.<Categorie>any());
    }

    @Test
    void testCreateCategorie2() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        when(categorieRepository.save(Mockito.<Categorie>any())).thenReturn(categorie);
        when(categorieDTOMapper.apply(Mockito.<Categorie>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        Categorie categorie2 = new Categorie();
        categorie2.setArticles(new ArrayList<>());
        categorie2.setDescription("The characteristics of someone or something");
        categorie2.setId(1L);
        categorie2.setName("Name");
        assertThrows(ResourceNotFoundException.class, () -> categorieServiceImpl.createCategorie(categorie2));
        verify(categorieRepository).save(Mockito.<Categorie>any());
        verify(categorieDTOMapper).apply(Mockito.<Categorie>any());
    }

    @Test
    void testFetchCategorieById() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        Optional<Categorie> ofResult = Optional.of(categorie);
        when(categorieRepository.fetchCategorieById(anyLong())).thenReturn(ofResult);
        CategorieDTO categorieDTO = new CategorieDTO(1L, "Name", "The characteristics of someone or something");

        when(categorieDTOMapper.apply(Mockito.<Categorie>any())).thenReturn(categorieDTO);
        ResponseEntity<CustomResponseEntity<CategorieDTO>> actualFetchCategorieByIdResult = categorieServiceImpl
                .fetchCategorieById(1L);
        assertTrue(actualFetchCategorieByIdResult.hasBody());
        assertTrue(actualFetchCategorieByIdResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchCategorieByIdResult.getStatusCodeValue());
        CustomResponseEntity<CategorieDTO> body = actualFetchCategorieByIdResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertSame(categorieDTO, body.getData());
        verify(categorieRepository).fetchCategorieById(anyLong());
        verify(categorieDTOMapper).apply(Mockito.<Categorie>any());
    }

    @Test
    void testFetchCategorieById2() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        Optional<Categorie> ofResult = Optional.of(categorie);
        when(categorieRepository.fetchCategorieById(anyLong())).thenReturn(ofResult);
        when(categorieDTOMapper.apply(Mockito.<Categorie>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> categorieServiceImpl.fetchCategorieById(1L));
        verify(categorieRepository).fetchCategorieById(anyLong());
        verify(categorieDTOMapper).apply(Mockito.<Categorie>any());
    }

    @Test
    void testFetchCategorieById3() {
        when(categorieRepository.fetchCategorieById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> categorieServiceImpl.fetchCategorieById(1L));
        verify(categorieRepository).fetchCategorieById(anyLong());
    }

    @Test
    void testFetchAllCategories() {
        when(categorieRepository.fetchAllCategories(Mockito.<Pageable>any())).thenReturn(new ArrayList<>());
        when(categorieRepository.getTotalCategoriesCount()).thenReturn(3L);
        ResponseEntity<CustomResponseList<CategorieDTO>> actualFetchAllCategoriesResult = categorieServiceImpl
                .fetchAllCategories(1L);
        assertTrue(actualFetchAllCategoriesResult.hasBody());
        assertTrue(actualFetchAllCategoriesResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchAllCategoriesResult.getStatusCodeValue());
        CustomResponseList<CategorieDTO> body = actualFetchAllCategoriesResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertTrue(body.getData().isEmpty());
        assertEquals(0L, body.getCurrentLength());
        assertEquals(3L, body.getTotal());
        assertEquals(200L, body.getStatus());
        verify(categorieRepository).fetchAllCategories(Mockito.<Pageable>any());
        verify(categorieRepository).getTotalCategoriesCount();
    }

    @Test
    void testFetchAllCategories2() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");

        ArrayList<Categorie> categorieList = new ArrayList<>();
        categorieList.add(categorie);
        when(categorieRepository.fetchAllCategories(Mockito.<Pageable>any())).thenReturn(categorieList);
        when(categorieRepository.getTotalCategoriesCount()).thenReturn(3L);
        when(categorieDTOMapper.apply(Mockito.<Categorie>any()))
                .thenReturn(new CategorieDTO(1L, "Name", "The characteristics of someone or something"));
        ResponseEntity<CustomResponseList<CategorieDTO>> actualFetchAllCategoriesResult = categorieServiceImpl
                .fetchAllCategories(1L);
        assertTrue(actualFetchAllCategoriesResult.hasBody());
        assertTrue(actualFetchAllCategoriesResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchAllCategoriesResult.getStatusCodeValue());
        CustomResponseList<CategorieDTO> body = actualFetchAllCategoriesResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(1, body.getData().size());
        assertEquals(1L, body.getCurrentLength());
        assertEquals(3L, body.getTotal());
        assertEquals(200L, body.getStatus());
        verify(categorieRepository).fetchAllCategories(Mockito.<Pageable>any());
        verify(categorieRepository).getTotalCategoriesCount();
        verify(categorieDTOMapper).apply(Mockito.<Categorie>any());
    }


    @Test
    void testSearchCategories() {
        when(categorieRepository.searchCategorie(Mockito.<String>any())).thenReturn(new ArrayList<>());
        ResponseEntity<CustomResponseEntity<List<CategorieDTO>>> actualSearchCategoriesResult = categorieServiceImpl
                .searchCategories("Prefix");
        assertTrue(actualSearchCategoriesResult.hasBody());
        assertTrue(actualSearchCategoriesResult.getHeaders().isEmpty());
        assertEquals(200, actualSearchCategoriesResult.getStatusCodeValue());
        CustomResponseEntity<List<CategorieDTO>> body = actualSearchCategoriesResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertTrue(body.getData().isEmpty());
        verify(categorieRepository).searchCategorie(Mockito.<String>any());
    }

    @Test
    void testModifyCategorieById() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        Optional<Categorie> ofResult = Optional.of(categorie);

        Categorie categorie2 = new Categorie();
        categorie2.setArticles(new ArrayList<>());
        categorie2.setDescription("The characteristics of someone or something");
        categorie2.setId(1L);
        categorie2.setName("Name");
        when(categorieRepository.save(Mockito.<Categorie>any())).thenReturn(categorie2);
        when(categorieRepository.fetchCategorieById(anyLong())).thenReturn(ofResult);

        Categorie categorieDetails = new Categorie();
        categorieDetails.setArticles(new ArrayList<>());
        categorieDetails.setDescription("The characteristics of someone or something");
        categorieDetails.setId(1L);
        categorieDetails.setName("Name");
        ResponseEntity<CustomResponseEntity<String>> actualModifyCategorieByIdResult = categorieServiceImpl
                .modifyCategorieById(1L, categorieDetails);
        assertTrue(actualModifyCategorieByIdResult.hasBody());
        assertTrue(actualModifyCategorieByIdResult.getHeaders().isEmpty());
        assertEquals(200, actualModifyCategorieByIdResult.getStatusCodeValue());
        CustomResponseEntity<String> body = actualModifyCategorieByIdResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertEquals("The Categorie with ID : 1 updated successfully.", body.getData());
        verify(categorieRepository).save(Mockito.<Categorie>any());
        verify(categorieRepository).fetchCategorieById(anyLong());
        assertEquals(1L, categorieDetails.getId());
    }

    @Test
    void testModifyCategorieById2() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        Optional<Categorie> ofResult = Optional.of(categorie);
        when(categorieRepository.save(Mockito.<Categorie>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        when(categorieRepository.fetchCategorieById(anyLong())).thenReturn(ofResult);

        Categorie categorieDetails = new Categorie();
        categorieDetails.setArticles(new ArrayList<>());
        categorieDetails.setDescription("The characteristics of someone or something");
        categorieDetails.setId(1L);
        categorieDetails.setName("Name");
        assertThrows(ResourceNotFoundException.class,
                () -> categorieServiceImpl.modifyCategorieById(1L, categorieDetails));
        verify(categorieRepository).save(Mockito.<Categorie>any());
        verify(categorieRepository).fetchCategorieById(anyLong());
    }

    @Test
    void testModifyCategorieById4() {
        when(categorieRepository.fetchCategorieById(anyLong())).thenReturn(Optional.empty());

        Categorie categorieDetails = new Categorie();
        categorieDetails.setArticles(new ArrayList<>());
        categorieDetails.setDescription("The characteristics of someone or something");
        categorieDetails.setId(1L);
        categorieDetails.setName("Name");
        assertThrows(ResourceNotFoundException.class,
                () -> categorieServiceImpl.modifyCategorieById(1L, categorieDetails));
        verify(categorieRepository).fetchCategorieById(anyLong());
    }

    @Test
    void testDeleteCategorieById() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        Optional<Categorie> ofResult = Optional.of(categorie);
        doNothing().when(categorieRepository).deleteCategorieById(anyLong());
        when(categorieRepository.fetchCategorieById(anyLong())).thenReturn(ofResult);
        ResponseEntity<CustomResponseEntity<String>> actualDeleteCategorieByIdResult = categorieServiceImpl
                .deleteCategorieById(1L);
        assertTrue(actualDeleteCategorieByIdResult.hasBody());
        assertTrue(actualDeleteCategorieByIdResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteCategorieByIdResult.getStatusCodeValue());
        CustomResponseEntity<String> body = actualDeleteCategorieByIdResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertEquals("The Categorie with ID : 1 deleted successfully.", body.getData());
        verify(categorieRepository).fetchCategorieById(anyLong());
        verify(categorieRepository).deleteCategorieById(anyLong());
    }

    @Test
    void testDeleteCategorieById2() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        Optional<Categorie> ofResult = Optional.of(categorie);
        doThrow(new ResourceNotFoundException("An error occurred")).when(categorieRepository)
                .deleteCategorieById(anyLong());
        when(categorieRepository.fetchCategorieById(anyLong())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> categorieServiceImpl.deleteCategorieById(1L));
        verify(categorieRepository).fetchCategorieById(anyLong());
        verify(categorieRepository).deleteCategorieById(anyLong());
    }

    @Test
    void testDeleteCategorieById3() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("The Categorie with ID : %d deleted successfully.");

        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("The Categorie with ID : %d deleted successfully.");
        image.setType("The Categorie with ID : %d deleted successfully.");

        Article article = new Article();
        article.setCategorie(categorie);
        article.setCertification(true);
        article.setEndingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setId(1L);
        article.setImage(image);
        article.setName("The Categorie with ID : %d deleted successfully.");
        article.setPrice(10.0f);
        article.setStartingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setTimePeriod(1);

        ArrayList<Article> articles = new ArrayList<>();
        articles.add(article);

        Categorie categorie2 = new Categorie();
        categorie2.setArticles(articles);
        categorie2.setDescription("The characteristics of someone or something");
        categorie2.setId(1L);
        categorie2.setName("Name");
        Optional<Categorie> ofResult = Optional.of(categorie2);
        doNothing().when(categorieRepository).deleteCategorieById(anyLong());
        when(categorieRepository.fetchCategorieById(anyLong())).thenReturn(ofResult);
        doNothing().when(articleRepository).deleteArticleById(anyLong());
        ResponseEntity<CustomResponseEntity<String>> actualDeleteCategorieByIdResult = categorieServiceImpl
                .deleteCategorieById(1L);
        assertTrue(actualDeleteCategorieByIdResult.hasBody());
        assertTrue(actualDeleteCategorieByIdResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteCategorieByIdResult.getStatusCodeValue());
        CustomResponseEntity<String> body = actualDeleteCategorieByIdResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertEquals("The Categorie with ID : 1 deleted successfully.", body.getData());
        verify(categorieRepository).fetchCategorieById(anyLong());
        verify(categorieRepository).deleteCategorieById(anyLong());
        verify(articleRepository).deleteArticleById(anyLong());
    }

    @Test
    void testDeleteCategorieById4() {
        when(categorieRepository.fetchCategorieById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> categorieServiceImpl.deleteCategorieById(1L));
        verify(categorieRepository).fetchCategorieById(anyLong());
    }

    @Test
    void testDeleteCategorieById5() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("The Categorie with ID : %d deleted successfully.");

        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("The Categorie with ID : %d deleted successfully.");
        image.setType("The Categorie with ID : %d deleted successfully.");

        Article article = new Article();
        article.setCategorie(categorie);
        article.setCertification(true);
        article.setEndingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setId(1L);
        article.setImage(image);
        article.setName("The Categorie with ID : %d deleted successfully.");
        article.setPrice(10.0f);
        article.setStartingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setTimePeriod(1);

        ArrayList<Article> articles = new ArrayList<>();
        articles.add(article);

        Categorie categorie2 = new Categorie();
        categorie2.setArticles(articles);
        categorie2.setDescription("The characteristics of someone or something");
        categorie2.setId(1L);
        categorie2.setName("Name");
        Optional<Categorie> ofResult = Optional.of(categorie2);
        when(categorieRepository.fetchCategorieById(anyLong())).thenReturn(ofResult);
        doThrow(new ResourceNotFoundException("An error occurred")).when(articleRepository).deleteArticleById(anyLong());
        assertThrows(ResourceNotFoundException.class, () -> categorieServiceImpl.deleteCategorieById(1L));
        verify(categorieRepository).fetchCategorieById(anyLong());
        verify(articleRepository).deleteArticleById(anyLong());
    }

    @Test
    void testGetCategorieById() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        Optional<Categorie> ofResult = Optional.of(categorie);
        when(categorieRepository.fetchCategorieById(anyLong())).thenReturn(ofResult);
        assertSame(categorie, categorieServiceImpl.getCategorieById(1L));
        verify(categorieRepository).fetchCategorieById(anyLong());
    }


    @Test
    void testGetCategorieById2() {
        when(categorieRepository.fetchCategorieById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> categorieServiceImpl.getCategorieById(1L));
        verify(categorieRepository).fetchCategorieById(anyLong());
    }


    @Test
    void testGetCategorieById3() {
        when(categorieRepository.fetchCategorieById(anyLong()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> categorieServiceImpl.getCategorieById(1L));
        verify(categorieRepository).fetchCategorieById(anyLong());
    }


    @Test
    void testGetCategorieByName() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        Optional<Categorie> ofResult = Optional.of(categorie);
        when(categorieRepository.fetchCategorieByName(Mockito.<String>any())).thenReturn(ofResult);
        assertSame(categorie, categorieServiceImpl.getCategorieByName("Categorie Name"));
        verify(categorieRepository).fetchCategorieByName(Mockito.<String>any());
    }


    @Test
    void testGetCategorieByName2() {
        when(categorieRepository.fetchCategorieByName(Mockito.<String>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> categorieServiceImpl.getCategorieByName("Categorie Name"));
        verify(categorieRepository).fetchCategorieByName(Mockito.<String>any());
    }

    @Test
    void testGetCategorieByName3() {
        when(categorieRepository.fetchCategorieByName(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> categorieServiceImpl.getCategorieByName("Categorie Name"));
        verify(categorieRepository).fetchCategorieByName(Mockito.<String>any());
    }
}

