package com.stage.elearning.controller.article;

import com.stage.elearning.dto.article.ArticleDTO;
import com.stage.elearning.service.article.ArticleService;
import com.stage.elearning.utility.CustomResponseEntity;
import com.stage.elearning.utility.CustomResponseList;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController (ArticleService articleService)
    {
        this.articleService = articleService;
    }


    @PostMapping("/admin/create_article")
    public ResponseEntity<CustomResponseEntity<ArticleDTO>> createArticle(@RequestParam("image")MultipartFile image, @RequestParam("jsonArticle") final String jsonArticle , @RequestParam("categorieId")final long categorieId) throws IOException {
        return articleService.createArticle(image,jsonArticle,categorieId);
    }

    @GetMapping("/all/get_article_image/id/{articleId}")
    public ResponseEntity<byte[]>  fetchArticleImageById(@PathVariable("articleId") final long articleId) throws IOException {
        return articleService.fetchArticleImage(articleId);
    }

    @GetMapping("/all/get/id/{articleId}")
    public ResponseEntity<CustomResponseEntity<ArticleDTO>> fetchArticleById(@PathVariable("articleId") final long articleId) {
        return articleService.fetchArticleById(articleId);
    }

    @GetMapping("/all/get/all_articles")
    public ResponseEntity<CustomResponseList<ArticleDTO>>   fetchAllArticles(
            @RequestParam(value = "pageNumber" , required = true) final long pageNumber
    )
    {
        return articleService.fetchAllArticles(pageNumber);
    }
    @GetMapping("/all/get/search")
    public ResponseEntity<CustomResponseEntity<List<ArticleDTO>>> searchArticle(  @RequestParam(value = "name" , required = false) final String name,
                                                                                  @RequestParam(value = "certified" , required = false)final Boolean certified,
                                                                                  @RequestParam(value = "period" , required = false) final Integer period,
                                                                                  @RequestParam(value = "price" , required = false) final Integer price,
                                                                                  @RequestParam(value = "categorie" , required = false) final String categorie,
                                                                                  @RequestParam(value = "startingDate" , required = false)@DateTimeFormat(pattern = "yyyy-MM-dd") final Date startingDate,
                                                                                  @RequestParam(value = "endingDate" , required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")final Date endingDate){
        return articleService.searchArticle(name,certified,period,price,categorie,startingDate,endingDate);
    }

    @GetMapping("/all/get/all_articles/categorie_id")
    public ResponseEntity<CustomResponseEntity<List<ArticleDTO>>> fetchArticlesByCategoryById(@RequestParam("categorieId")final  long categorieId)
    {
        return articleService.fetchArticlesByCategoryById(categorieId);
    }

    @GetMapping("/all/get/all_articles/categorie_name")
    public ResponseEntity<CustomResponseEntity<List<ArticleDTO>>> fetchArticlesByCategoryByName(@RequestParam("categorieName")final  String categorieName)
    {
        return articleService.fetchArticlesByCategoryByName(categorieName);
    }
    @PutMapping("/admin/update/id/{articleId}")
    public ResponseEntity<CustomResponseEntity<String>> modifyArticleById(@RequestParam("image") MultipartFile image,@PathVariable("articleId") final long articleId, @RequestParam("jsonArticle") final String jsonArticle , @RequestParam("categorieId")final long categorieId) throws IOException {
        return articleService.modifyArticleById(image , articleId,jsonArticle,categorieId);
    }

    @DeleteMapping("/admin/delete/id/{articleId}")
    public ResponseEntity<CustomResponseEntity<String>> deleteArticleById(@PathVariable("articleId") final long articleId) throws IOException {
        return articleService.deleteArticleById(articleId);
    }

    @DeleteMapping("/admin/delete/name/{articleName}")
    public ResponseEntity<CustomResponseEntity<String>> deleteArticleByName(@PathVariable("articleName") final String articleName) throws IOException {
        return articleService.deleteArticleByName(articleName);
    }


    @PutMapping("/client/update/subscribe/article_id/{articleId}")
    ResponseEntity<CustomResponseEntity<String>> subscribeToArticle(@NotNull  @AuthenticationPrincipal UserDetails userDetails, @PathVariable("articleId") final long articleId)
    {
        return articleService.subscribeToArticle(userDetails,articleId);
    }
    @PutMapping("/client/update/unsubscribe/article_id/{articleId}")
    ResponseEntity<CustomResponseEntity<String>> unsubscribeToArticle(@NotNull  @AuthenticationPrincipal UserDetails userDetails, @PathVariable("articleId") final long articleId)
    {
        return articleService.unsubscribeToArticle(userDetails,articleId);
    }

}
