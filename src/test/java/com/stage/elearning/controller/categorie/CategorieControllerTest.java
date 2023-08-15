package com.stage.elearning.controller.categorie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stage.elearning.dto.categorie.CategorieDTO;
import com.stage.elearning.dto.categorie.CategorieDTOMapper;
import com.stage.elearning.model.acticle.Article;
import com.stage.elearning.model.categories.Categorie;
import com.stage.elearning.model.file.FileData;
import com.stage.elearning.repository.ArticleRepository;
import com.stage.elearning.repository.CategorieRepository;
import com.stage.elearning.service.categorie.CategorieService;
import com.stage.elearning.service.categorie.CategorieServiceImpl;
import com.stage.elearning.utility.CustomResponseEntity;
import com.stage.elearning.utility.CustomResponseList;

import java.time.LocalDate;
import java.time.ZoneOffset;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {CategorieController.class})
@ExtendWith(SpringExtension.class)
class CategorieControllerTest {
    @Autowired
    private CategorieController categorieController;

    @MockBean
    private CategorieService categorieService;

    @Test
    void testCreateCategorie() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        CategorieRepository categorieRepository = mock(CategorieRepository.class);
        when(categorieRepository.save(Mockito.<Categorie>any())).thenReturn(categorie);
        CategorieController categorieController = new CategorieController(
                new CategorieServiceImpl(categorieRepository, new CategorieDTOMapper(), mock(ArticleRepository.class)));

        Categorie categorie2 = new Categorie();
        categorie2.setArticles(new ArrayList<>());
        categorie2.setDescription("The characteristics of someone or something");
        categorie2.setId(1L);
        categorie2.setName("Name");
        ResponseEntity<CustomResponseEntity<CategorieDTO>> actualCreateCategorieResult = categorieController
                .createCategorie(categorie2);
        assertTrue(actualCreateCategorieResult.hasBody());
        assertTrue(actualCreateCategorieResult.getHeaders().isEmpty());
        assertEquals(200, actualCreateCategorieResult.getStatusCodeValue());
        CustomResponseEntity<CategorieDTO> body = actualCreateCategorieResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        CategorieDTO data = body.getData();
        assertEquals("The characteristics of someone or something", data.description());
        assertEquals("Name", data.name());
        assertEquals(1L, data.id());
        verify(categorieRepository).save(Mockito.<Categorie>any());
    }


    @Test
    void testFetchCategorieById() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        CategorieRepository categorieRepository = mock(CategorieRepository.class);
        when(categorieRepository.fetchCategorieById(anyLong())).thenReturn(Optional.of(categorie));
        ResponseEntity<CustomResponseEntity<CategorieDTO>> actualFetchCategorieByIdResult = (new CategorieController(
                new CategorieServiceImpl(categorieRepository, new CategorieDTOMapper(), mock(ArticleRepository.class))))
                .fetchCategorieById(1L);
        assertTrue(actualFetchCategorieByIdResult.hasBody());
        assertTrue(actualFetchCategorieByIdResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchCategorieByIdResult.getStatusCodeValue());
        CustomResponseEntity<CategorieDTO> body = actualFetchCategorieByIdResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        CategorieDTO data = body.getData();
        assertEquals("The characteristics of someone or something", data.description());
        assertEquals("Name", data.name());
        assertEquals(1L, data.id());
        verify(categorieRepository).fetchCategorieById(anyLong());
    }

    @Test
    void testFetchAllCategories() {
        CategorieRepository categorieRepository = mock(CategorieRepository.class);
        when(categorieRepository.fetchAllCategories(Mockito.<Pageable>any())).thenReturn(new ArrayList<>());
        when(categorieRepository.getTotalCategoriesCount()).thenReturn(3L);
        ResponseEntity<CustomResponseList<CategorieDTO>> actualFetchAllCategoriesResult = (new CategorieController(
                new CategorieServiceImpl(categorieRepository, new CategorieDTOMapper(), mock(ArticleRepository.class))))
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
    void testSearchCategories() {
        CategorieRepository categorieRepository = mock(CategorieRepository.class);
        when(categorieRepository.searchCategorie(Mockito.<String>any())).thenReturn(new ArrayList<>());
        ResponseEntity<CustomResponseEntity<List<CategorieDTO>>> actualSearchCategoriesResult = (new CategorieController(
                new CategorieServiceImpl(categorieRepository, new CategorieDTOMapper(), mock(ArticleRepository.class))))
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
    void testDeleteCategorieById() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        Optional<Categorie> ofResult = Optional.of(categorie);
        CategorieRepository categorieRepository = mock(CategorieRepository.class);
        doNothing().when(categorieRepository).deleteCategorieById(anyLong());
        when(categorieRepository.fetchCategorieById(anyLong())).thenReturn(ofResult);
        ResponseEntity<CustomResponseEntity<String>> actualDeleteCategorieByIdResult = (new CategorieController(
                new CategorieServiceImpl(categorieRepository, new CategorieDTOMapper(), mock(ArticleRepository.class))))
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
    void testModifyCategorieById() throws Exception {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        java.sql.Date endingDate = mock(java.sql.Date.class);
        when(endingDate.getTime()).thenReturn(10L);

        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Article article = new Article();
        article.setCategorie(categorie);
        article.setCertification(true);
        article.setEndingDate(endingDate);
        article.setId(1L);
        article.setImage(image);
        article.setName("Name");
        article.setPrice(10.0f);
        article.setStartingDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setTimePeriod(1);

        ArrayList<Article> articles = new ArrayList<>();
        articles.add(article);

        Categorie categorie2 = new Categorie();
        categorie2.setArticles(articles);
        categorie2.setDescription("The characteristics of someone or something");
        categorie2.setId(1L);
        categorie2.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(categorie2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/categorie/admin/update/id/{categorieId}", "Uri Variables", "Uri Variables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(categorieController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}

