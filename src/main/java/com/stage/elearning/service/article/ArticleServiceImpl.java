package com.stage.elearning.service.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stage.elearning.dto.article.ArticleDTO;
import com.stage.elearning.dto.article.ArticleDTOMapper;
import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.model.acticle.Article;
import com.stage.elearning.model.categories.Categorie;
import com.stage.elearning.model.file.FileData;
import com.stage.elearning.model.user.UserEntity;
import com.stage.elearning.repository.ArticleRepository;
import com.stage.elearning.repository.ChapterRepository;
import com.stage.elearning.service.user.UserEntityService;

import com.stage.elearning.service.categorie.CategorieService;
import com.stage.elearning.service.file.FileService;
import com.stage.elearning.utility.CustomResponseEntity;
import com.stage.elearning.utility.CustomResponseList;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleDTOMapper articleDTOMapper;
    private final FileService fileService;
    private final CategorieService categorieService;
    private final UserEntityService userEntityService;
    private final ChapterRepository chapterRepository;
    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, UserEntityService userEntityService, FileService fileService, CategorieService categorieService, ArticleDTOMapper articleDTOMapper, ChapterRepository chapterRepository)
    {
        this.articleRepository = articleRepository;
        this.fileService = fileService;
        this.categorieService = categorieService;
        this.articleDTOMapper = articleDTOMapper;
        this.userEntityService = userEntityService;
        this.chapterRepository = chapterRepository;
    }


    @Override
    public ResponseEntity<CustomResponseEntity<ArticleDTO>> createArticle(@NotNull final MultipartFile image, @NotNull final String jsonArticle , final long categorieId) throws IOException {

        final Article article = new ObjectMapper().readValue(jsonArticle,Article.class);
        for (var chapter: article.getChapters()) {
            chapter.setArticle(article);
        }
        final Categorie currentCategorie = categorieService.getCategorieById(categorieId);
        final FileData fileData = fileService.processUploadedFile(image);
        article.setImage(fileData);
        article.setCategorie(currentCategorie);
        final ArticleDTO savedArticleDTO = articleDTOMapper.apply(articleRepository.save(article));
        final CustomResponseEntity<ArticleDTO> customResponseEntity = new CustomResponseEntity<>(HttpStatus.CREATED,savedArticleDTO);
        return new ResponseEntity<>(customResponseEntity, HttpStatus.CREATED);
    }
    @Override
    public ResponseEntity<byte[]> fetchArticleImage(final long articleId) throws IOException {

        final Article article = getArticleById(articleId);
        final FileData fileData = fileService.getFileDataById(article.getImage().getId());
        return fileService.downloadFile(fileData);
    }
    @Override
    public ResponseEntity<CustomResponseEntity<ArticleDTO>> fetchArticleById(final long articleId)
    {
        final ArticleDTO articleDto = articleDTOMapper.apply(getArticleById(articleId));
        final CustomResponseEntity<ArticleDTO> customResponseEntity = new CustomResponseEntity<>(HttpStatus.OK,articleDto);
        return new ResponseEntity<>(customResponseEntity , HttpStatus.OK);
    }
    @Override
    public ResponseEntity<CustomResponseList<ArticleDTO>> fetchAllArticles(final long pageNumber)
    {
        final Pageable pageable = PageRequest.of((int) pageNumber - 1, 10);
        final List<ArticleDTO> articleDTOFullList = articleRepository.fetchAllArticles(pageable).stream().map(articleDTOMapper).toList();
        if(articleDTOFullList.isEmpty() && pageNumber > 1)
        {
            return fetchAllArticles(1);
        }
        final CustomResponseList<ArticleDTO> customResponse =
                new CustomResponseList<>(
                        HttpStatus.OK,
                        articleDTOFullList,
                        articleDTOFullList.size(),
                        articleRepository.getTotalArticleCount()
                );
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<CustomResponseEntity<List<ArticleDTO>>> searchArticle(  final String name,
                                                                                 final Boolean certified,
                                                                                  final Integer period,
                                                                                 final Integer price,
                                                                                 final String categorie,
                                                                                 final Date startingDate,
                                                                                 final Date endingDate)
    {
        final List<ArticleDTO> searchedArticles = articleRepository.searchArticle(name,certified,period,price,categorie,startingDate,endingDate).stream().map(articleDTOMapper).toList();
        final CustomResponseEntity<List<ArticleDTO>> customResponse =
                new CustomResponseEntity<>(HttpStatus.OK,searchedArticles);
        return new ResponseEntity<>(customResponse,HttpStatus.OK);
    }
    @Transactional
    @Override
    public ResponseEntity<CustomResponseEntity<String>> modifyArticleById(final MultipartFile image, final long articleId, @NotNull final String  jsonArticle, final long categorieId) throws IOException {
        final Article currentArticle = getArticleById(articleId);
        final FileData prevImage = currentArticle.getImage();
        final Article articleDetails = new ObjectMapper().readValue(jsonArticle,Article.class);

        final Categorie currentCategorie = categorieService.getCategorieById(categorieId);
        for(var chapter : articleDetails.getChapters())
        {
            chapter.setArticle(currentArticle);
        }


        for(var chapter : currentArticle.getChapters())
        {
            chapter.setArticle(null);
            chapterRepository.save(chapter);
        }

        articleDetails.setId(currentArticle.getId());
        articleDetails.setCategorie(currentCategorie);
        if(image != null)
        {
            final FileData fileData = fileService.processUploadedFile(image);
            articleDetails.setImage(fileData);
            articleRepository.save(articleDetails);
            fileService.deleteFileFromFileSystem(prevImage);
        }else {
            articleRepository.save(articleDetails);
        }



        final String successResponse = "The Article has been updated successfully.";
        final CustomResponseEntity<String> customResponseEntity = new CustomResponseEntity<>(HttpStatus.OK, successResponse);
        return new ResponseEntity<>(customResponseEntity , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomResponseEntity<List<ArticleDTO>>>fetchArticlesByCategoryById(final long categorieId) {
        final Categorie categorie = categorieService.getCategorieById(categorieId);
        final List<ArticleDTO> articleDTOList = categorie.getArticles().stream().map(articleDTOMapper).toList();
        final CustomResponseEntity<List<ArticleDTO>> customResponseEntityList = new CustomResponseEntity<>(HttpStatus.OK,articleDTOList);
        return new ResponseEntity<>(customResponseEntityList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomResponseEntity<List<ArticleDTO>>> fetchArticlesByCategoryByName(final String categorieName) {
        final Categorie categorie = categorieService.getCategorieByName(categorieName);
        List<ArticleDTO> articleDTOList = categorie.getArticles().stream().map(articleDTOMapper).toList();
        final CustomResponseEntity<List<ArticleDTO>> customResponseEntityList = new CustomResponseEntity<>(HttpStatus.OK,articleDTOList);
        return new ResponseEntity<>(customResponseEntityList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomResponseEntity<String>> subscribeToArticle(@NotNull UserDetails userDetails, long articleId) {
        final UserEntity currentUser = userEntityService.getUserEntityByEmail(userDetails.getUsername());
        final Article currentArticle = getArticleById(articleId);

        if(currentArticle.getSubscribers().contains(currentUser))
        {
            throw new IllegalStateException(String.format("This User is already subscribed to this article with name %s.",currentArticle.getName()));
        }

        currentArticle.getSubscribers().add(currentUser);
        articleRepository.save(currentArticle);

        final String successResponse = String.format("The use successfully subscribed to the article with name %s" , currentArticle.getName());
        final CustomResponseEntity<String> customResponse = new CustomResponseEntity<>(HttpStatus.OK,successResponse);
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<CustomResponseEntity<String>> unsubscribeToArticle(@NotNull UserDetails userDetails, final long articleId)
    {
        final UserEntity currentUser = userEntityService.getUserEntityByEmail(userDetails.getUsername());
        final Article currentArticle = getArticleById(articleId);

        if(!currentArticle.getSubscribers().contains(currentUser))
        {
            throw new IllegalStateException(String.format("This User is already not subscribed  to this article with name %s.",currentArticle.getName()));
        }

        currentArticle.getSubscribers().remove(currentUser);
        articleRepository.save(currentArticle);

        final String successResponse = String.format("The use successfully unsubscribed to the article with name %s" , currentArticle.getName());
        final CustomResponseEntity<String> customResponse = new CustomResponseEntity<>(HttpStatus.OK,successResponse);
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseEntity<String>> deleteArticleById(final long articleId) throws IOException {
        final Article currentArticle = getArticleById(articleId);
        articleRepository.deleteArticleById(articleId);
        fileService.deleteFileFromFileSystem(currentArticle.getImage());
        final String successResponse = "The Article has been deleted successfully.";
        final CustomResponseEntity<String> customResponseEntity = new CustomResponseEntity<>(HttpStatus.OK,successResponse);
        return new ResponseEntity<>(customResponseEntity , HttpStatus.OK);
    }
    @Override
    @Transactional
    public ResponseEntity<CustomResponseEntity<String>> deleteArticleByName(final String articleName) throws IOException {
        final Article currentArticle = getArticleByName(articleName);
        articleRepository.deleteArticleByName(articleName);
        fileService.deleteFileFromFileSystem(currentArticle.getImage());
        final String successResponse = "The Article has been deleted successfully.";
        final CustomResponseEntity<String> customResponseEntity = new CustomResponseEntity<>(HttpStatus.OK,successResponse);
        return new ResponseEntity<>(customResponseEntity , HttpStatus.OK);
    }

    private Article getArticleById(final long articleId)
    {
        return articleRepository.fetchArticleById(articleId).orElseThrow(
                ()-> new ResourceNotFoundException(String.format("The Article with ID : %d could not be found in our system",articleId))
        );
    }
    private Article getArticleByName(final String articleName)
    {
        return articleRepository.fetchArticleByName(articleName).orElseThrow(
                ()-> new ResourceNotFoundException(String.format("The Article with NAME : %s could not be found in our system",articleName))
        );
    }

}
