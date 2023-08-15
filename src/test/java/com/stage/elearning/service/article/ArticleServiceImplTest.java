package com.stage.elearning.service.article;

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

import com.stage.elearning.dto.article.ArticleDTO;
import com.stage.elearning.dto.article.ArticleDTOMapper;
import com.stage.elearning.dto.categorie.CategorieDTO;
import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.model.acticle.Article;
import com.stage.elearning.model.categories.Categorie;
import com.stage.elearning.model.file.FileData;
import com.stage.elearning.repository.ArticleRepository;
import com.stage.elearning.service.categorie.CategorieService;
import com.stage.elearning.service.file.FileService;
import com.stage.elearning.utility.CustomResponseEntity;
import com.stage.elearning.utility.CustomResponseList;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {ArticleServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ArticleServiceImplTest {
    @MockBean
    private ArticleDTOMapper articleDTOMapper;

    @MockBean
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleServiceImpl articleServiceImpl;

    @MockBean
    private CategorieService categorieService;

    @MockBean
    private FileService fileService;


    @Test
    void testCreateArticle4() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> articleServiceImpl.createArticle(null, "Json Article", 1L));
    }
    @Test
    void testCreateArticle5() throws IOException {
        DataInputStream contentStream = mock(DataInputStream.class);
        when(contentStream.readAllBytes()).thenReturn("AXAXAXAX".getBytes("UTF-8"));
        doNothing().when(contentStream).close();
        assertThrows(IllegalArgumentException.class,
                () -> articleServiceImpl.createArticle(new MockMultipartFile("Name", contentStream), null, 1L));
        verify(contentStream).readAllBytes();
        verify(contentStream).close();
    }



    @Test
    void testFetchArticleImage() throws IOException {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");

        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Article article = new Article();
        article.setCategorie(categorie);
        article.setCertification(true);
        article.setEndingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setId(1L);
        article.setImage(image);
        article.setName("Name");
        article.setPrice(10.0f);
        article.setStartingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setTimePeriod(1);
        Optional<Article> ofResult = Optional.of(article);
        when(articleRepository.fetchArticleById(anyLong())).thenReturn(ofResult);
        when(fileService.getFileDataById(anyLong())).thenThrow(new IllegalArgumentException("foo"));
        assertThrows(IllegalArgumentException.class, () -> articleServiceImpl.fetchArticleImage(1L));
        verify(articleRepository).fetchArticleById(anyLong());
        verify(fileService).getFileDataById(anyLong());
    }


    @Test
    void testFetchArticleImage2() throws IOException {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");

        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Article article = new Article();
        article.setCategorie(categorie);
        article.setCertification(true);
        article.setEndingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setId(1L);
        article.setImage(image);
        article.setName("Name");
        article.setPrice(10.0f);
        article.setStartingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setTimePeriod(1);
        Optional<Article> ofResult = Optional.of(article);
        when(articleRepository.fetchArticleById(anyLong())).thenReturn(ofResult);

        FileData fileData = new FileData();
        fileData.setFilePath("/directory/foo.txt");
        fileData.setId(1L);
        fileData.setName("Name");
        fileData.setType("Type");
        when(fileService.getFileDataById(anyLong())).thenReturn(fileData);
        when(fileService.downloadFile(Mockito.<FileData>any())).thenReturn(null);
        assertNull(articleServiceImpl.fetchArticleImage(1L));
        verify(articleRepository).fetchArticleById(anyLong());
        verify(fileService).getFileDataById(anyLong());
        verify(fileService).downloadFile(Mockito.<FileData>any());
    }

    @Test
    void testFetchArticleById() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");

        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Article article = new Article();
        article.setCategorie(categorie);
        article.setCertification(true);
        article.setEndingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setId(1L);
        article.setImage(image);
        article.setName("Name");
        article.setPrice(10.0f);
        article.setStartingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setTimePeriod(1);
        Optional<Article> ofResult = Optional.of(article);
        when(articleRepository.fetchArticleById(anyLong())).thenReturn(ofResult);
        Date statingDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Date endingDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        CategorieDTO categorieDTO = new CategorieDTO(1L, "Name", "The characteristics of someone or something");

        ArticleDTO articleDTO = new ArticleDTO(1L, "Name", statingDate, endingDate, true, 1, 10.0f, categorieDTO,
                new FileData());

        when(articleDTOMapper.apply(Mockito.<Article>any())).thenReturn(articleDTO);
        ResponseEntity<CustomResponseEntity<ArticleDTO>> actualFetchArticleByIdResult = articleServiceImpl
                .fetchArticleById(1L);
        assertTrue(actualFetchArticleByIdResult.hasBody());
        assertTrue(actualFetchArticleByIdResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchArticleByIdResult.getStatusCodeValue());
        CustomResponseEntity<ArticleDTO> body = actualFetchArticleByIdResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertSame(articleDTO, body.getData());
        verify(articleRepository).fetchArticleById(anyLong());
        verify(articleDTOMapper).apply(Mockito.<Article>any());
    }

    @Test
    void testFetchArticleById2() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");

        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Article article = new Article();
        article.setCategorie(categorie);
        article.setCertification(true);
        article.setEndingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setId(1L);
        article.setImage(image);
        article.setName("Name");
        article.setPrice(10.0f);
        article.setStartingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setTimePeriod(1);
        Optional<Article> ofResult = Optional.of(article);
        when(articleRepository.fetchArticleById(anyLong())).thenReturn(ofResult);
        when(articleDTOMapper.apply(Mockito.<Article>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> articleServiceImpl.fetchArticleById(1L));
        verify(articleRepository).fetchArticleById(anyLong());
        verify(articleDTOMapper).apply(Mockito.<Article>any());
    }

    @Test
    void testFetchArticleById3() {
        when(articleRepository.fetchArticleById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> articleServiceImpl.fetchArticleById(1L));
        verify(articleRepository).fetchArticleById(anyLong());
    }

    @Test
    void testFetchAllArticles() {
        when(articleRepository.fetchAllArticles(Mockito.<Pageable>any())).thenReturn(new ArrayList<>());
        when(articleRepository.getTotalArticleCount()).thenReturn(3L);
        ResponseEntity<CustomResponseList<ArticleDTO>> actualFetchAllArticlesResult = articleServiceImpl
                .fetchAllArticles(1L);
        assertTrue(actualFetchAllArticlesResult.hasBody());
        assertTrue(actualFetchAllArticlesResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchAllArticlesResult.getStatusCodeValue());
        CustomResponseList<ArticleDTO> body = actualFetchAllArticlesResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertTrue(body.getData().isEmpty());
        assertEquals(0L, body.getCurrentLength());
        assertEquals(3L, body.getTotal());
        assertEquals(200L, body.getStatus());
        verify(articleRepository).fetchAllArticles(Mockito.<Pageable>any());
        verify(articleRepository).getTotalArticleCount();
    }

    @Test
    void testFetchAllArticles2() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");

        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Article article = new Article();
        article.setCategorie(categorie);
        article.setCertification(true);
        article.setEndingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setId(1L);
        article.setImage(image);
        article.setName("Name");
        article.setPrice(10.0f);
        article.setStartingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setTimePeriod(1);

        ArrayList<Article> articleList = new ArrayList<>();
        articleList.add(article);
        when(articleRepository.fetchAllArticles(Mockito.<Pageable>any())).thenReturn(articleList);
        when(articleRepository.getTotalArticleCount()).thenReturn(3L);
        Date statingDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Date endingDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        CategorieDTO categorieDTO = new CategorieDTO(1L, "Name", "The characteristics of someone or something");

        when(articleDTOMapper.apply(Mockito.<Article>any())).thenReturn(
                new ArticleDTO(1L, "Name", statingDate, endingDate, true, 1, 10.0f, categorieDTO, new FileData()));
        ResponseEntity<CustomResponseList<ArticleDTO>> actualFetchAllArticlesResult = articleServiceImpl
                .fetchAllArticles(1L);
        assertTrue(actualFetchAllArticlesResult.hasBody());
        assertTrue(actualFetchAllArticlesResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchAllArticlesResult.getStatusCodeValue());
        CustomResponseList<ArticleDTO> body = actualFetchAllArticlesResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(1, body.getData().size());
        assertEquals(1L, body.getCurrentLength());
        assertEquals(3L, body.getTotal());
        assertEquals(200L, body.getStatus());
        verify(articleRepository).fetchAllArticles(Mockito.<Pageable>any());
        verify(articleRepository).getTotalArticleCount();
        verify(articleDTOMapper).apply(Mockito.<Article>any());
    }


    @Test
    void testSearchArticle() {
        when(articleRepository.searchArticle(Mockito.<String>any(), Mockito.<Boolean>any(), Mockito.<Integer>any(),
                Mockito.<Integer>any(), Mockito.<String>any(), Mockito.<Date>any(), Mockito.<Date>any()))
                .thenReturn(new ArrayList<>());
        Date startingDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        ResponseEntity<CustomResponseEntity<List<ArticleDTO>>> actualSearchArticleResult = articleServiceImpl
                .searchArticle("Name", true, 1, 1, "Categorie", startingDate,
                        Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assertTrue(actualSearchArticleResult.hasBody());
        assertTrue(actualSearchArticleResult.getHeaders().isEmpty());
        assertEquals(200, actualSearchArticleResult.getStatusCodeValue());
        CustomResponseEntity<List<ArticleDTO>> body = actualSearchArticleResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertTrue(body.getData().isEmpty());
        verify(articleRepository).searchArticle(Mockito.<String>any(), Mockito.<Boolean>any(), Mockito.<Integer>any(),
                Mockito.<Integer>any(), Mockito.<String>any(), Mockito.<Date>any(), Mockito.<Date>any());
    }


    @Test
    void testModifyArticleById3() throws IOException {
        when(articleRepository.fetchArticleById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> articleServiceImpl.modifyArticleById(
                        new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))), 1L, "Json Article",
                        1L));
        verify(articleRepository).fetchArticleById(anyLong());
    }

    @Test
    void testModifyArticleById4() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> articleServiceImpl.modifyArticleById(
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))), 1L, null, 1L));
    }


    @Test
    void testFetchArticlesByCategoryById() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        when(categorieService.getCategorieById(anyLong())).thenReturn(categorie);
        ResponseEntity<CustomResponseEntity<List<ArticleDTO>>> actualFetchArticlesByCategoryByIdResult = articleServiceImpl
                .fetchArticlesByCategoryById(1L);
        assertTrue(actualFetchArticlesByCategoryByIdResult.hasBody());
        assertTrue(actualFetchArticlesByCategoryByIdResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchArticlesByCategoryByIdResult.getStatusCodeValue());
        CustomResponseEntity<List<ArticleDTO>> body = actualFetchArticlesByCategoryByIdResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertTrue(body.getData().isEmpty());
        verify(categorieService).getCategorieById(anyLong());
    }


    @Test
    void testFetchArticlesByCategoryByName() {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");
        when(categorieService.getCategorieByName(Mockito.<String>any())).thenReturn(categorie);
        ResponseEntity<CustomResponseEntity<List<ArticleDTO>>> actualFetchArticlesByCategoryByNameResult = articleServiceImpl
                .fetchArticlesByCategoryByName("Categorie Name");
        assertTrue(actualFetchArticlesByCategoryByNameResult.hasBody());
        assertTrue(actualFetchArticlesByCategoryByNameResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchArticlesByCategoryByNameResult.getStatusCodeValue());
        CustomResponseEntity<List<ArticleDTO>> body = actualFetchArticlesByCategoryByNameResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertTrue(body.getData().isEmpty());
        verify(categorieService).getCategorieByName(Mockito.<String>any());
    }


    @Test
    void testDeleteArticleById() throws IOException {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");

        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Article article = new Article();
        article.setCategorie(categorie);
        article.setCertification(true);
        article.setEndingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setId(1L);
        article.setImage(image);
        article.setName("Name");
        article.setPrice(10.0f);
        article.setStartingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setTimePeriod(1);
        Optional<Article> ofResult = Optional.of(article);
        doNothing().when(articleRepository).deleteArticleById(anyLong());
        when(articleRepository.fetchArticleById(anyLong())).thenReturn(ofResult);
        doNothing().when(fileService).deleteFileFromFileSystem(Mockito.<FileData>any());
        ResponseEntity<CustomResponseEntity<String>> actualDeleteArticleByIdResult = articleServiceImpl
                .deleteArticleById(1L);
        assertTrue(actualDeleteArticleByIdResult.hasBody());
        assertTrue(actualDeleteArticleByIdResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteArticleByIdResult.getStatusCodeValue());
        CustomResponseEntity<String> body = actualDeleteArticleByIdResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertEquals("The Article has been deleted successfully.", body.getData());
        verify(articleRepository).fetchArticleById(anyLong());
        verify(articleRepository).deleteArticleById(anyLong());
        verify(fileService).deleteFileFromFileSystem(Mockito.<FileData>any());
    }

    @Test
    void testDeleteArticleById2() throws IOException {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");

        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Article article = new Article();
        article.setCategorie(categorie);
        article.setCertification(true);
        article.setEndingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setId(1L);
        article.setImage(image);
        article.setName("Name");
        article.setPrice(10.0f);
        article.setStartingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setTimePeriod(1);
        Optional<Article> ofResult = Optional.of(article);
        doNothing().when(articleRepository).deleteArticleById(anyLong());
        when(articleRepository.fetchArticleById(anyLong())).thenReturn(ofResult);
        doThrow(new ResourceNotFoundException("An error occurred")).when(fileService)
                .deleteFileFromFileSystem(Mockito.<FileData>any());
        assertThrows(ResourceNotFoundException.class, () -> articleServiceImpl.deleteArticleById(1L));
        verify(articleRepository).fetchArticleById(anyLong());
        verify(articleRepository).deleteArticleById(anyLong());
        verify(fileService).deleteFileFromFileSystem(Mockito.<FileData>any());
    }

    @Test
    void testDeleteArticleById3() throws IOException {
        when(articleRepository.fetchArticleById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> articleServiceImpl.deleteArticleById(1L));
        verify(articleRepository).fetchArticleById(anyLong());
    }


    @Test
    void testDeleteArticleByName() throws IOException {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");

        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Article article = new Article();
        article.setCategorie(categorie);
        article.setCertification(true);
        article.setEndingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setId(1L);
        article.setImage(image);
        article.setName("Name");
        article.setPrice(10.0f);
        article.setStartingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setTimePeriod(1);
        Optional<Article> ofResult = Optional.of(article);
        doNothing().when(articleRepository).deleteArticleByName(Mockito.<String>any());
        when(articleRepository.fetchArticleByName(Mockito.<String>any())).thenReturn(ofResult);
        doNothing().when(fileService).deleteFileFromFileSystem(Mockito.<FileData>any());
        ResponseEntity<CustomResponseEntity<String>> actualDeleteArticleByNameResult = articleServiceImpl
                .deleteArticleByName("Article Name");
        assertTrue(actualDeleteArticleByNameResult.hasBody());
        assertTrue(actualDeleteArticleByNameResult.getHeaders().isEmpty());
        assertEquals(200, actualDeleteArticleByNameResult.getStatusCodeValue());
        CustomResponseEntity<String> body = actualDeleteArticleByNameResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertEquals("The Article has been deleted successfully.", body.getData());
        verify(articleRepository).fetchArticleByName(Mockito.<String>any());
        verify(articleRepository).deleteArticleByName(Mockito.<String>any());
        verify(fileService).deleteFileFromFileSystem(Mockito.<FileData>any());
    }

    @Test
    void testDeleteArticleByName2() throws IOException {
        Categorie categorie = new Categorie();
        categorie.setArticles(new ArrayList<>());
        categorie.setDescription("The characteristics of someone or something");
        categorie.setId(1L);
        categorie.setName("Name");

        FileData image = new FileData();
        image.setFilePath("/directory/foo.txt");
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Article article = new Article();
        article.setCategorie(categorie);
        article.setCertification(true);
        article.setEndingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setId(1L);
        article.setImage(image);
        article.setName("Name");
        article.setPrice(10.0f);
        article.setStartingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        article.setTimePeriod(1);
        Optional<Article> ofResult = Optional.of(article);
        doNothing().when(articleRepository).deleteArticleByName(Mockito.<String>any());
        when(articleRepository.fetchArticleByName(Mockito.<String>any())).thenReturn(ofResult);
        doThrow(new ResourceNotFoundException("An error occurred")).when(fileService)
                .deleteFileFromFileSystem(Mockito.<FileData>any());
        assertThrows(ResourceNotFoundException.class, () -> articleServiceImpl.deleteArticleByName("Article Name"));
        verify(articleRepository).fetchArticleByName(Mockito.<String>any());
        verify(articleRepository).deleteArticleByName(Mockito.<String>any());
        verify(fileService).deleteFileFromFileSystem(Mockito.<FileData>any());
    }

    @Test
    void testDeleteArticleByName3() throws IOException {
        when(articleRepository.fetchArticleByName(Mockito.<String>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> articleServiceImpl.deleteArticleByName("Article Name"));
        verify(articleRepository).fetchArticleByName(Mockito.<String>any());
    }
}

