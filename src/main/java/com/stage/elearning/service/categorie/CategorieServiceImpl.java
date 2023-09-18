package com.stage.elearning.service.categorie;

import com.stage.elearning.dto.categorie.CategorieDTO;
import com.stage.elearning.dto.categorie.CategorieDTOMapper;
import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.model.categories.Categorie;
import com.stage.elearning.repository.ArticleRepository;
import com.stage.elearning.repository.CategorieRepository;
import com.stage.elearning.utility.CustomResponseEntity;
import com.stage.elearning.utility.CustomResponseList;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategorieServiceImpl implements CategorieService{

    private final CategorieRepository categorieRepository;
    private final CategorieDTOMapper categorieDTOMapper;
    private final ArticleRepository articleRepository;
    @Autowired
    public CategorieServiceImpl(CategorieRepository categorieRepository, CategorieDTOMapper categorieDTOMapper, ArticleRepository articleRepository) {
        this.categorieRepository = categorieRepository;
        this.categorieDTOMapper = categorieDTOMapper;
        this.articleRepository = articleRepository;
    }

    @Override
    public ResponseEntity<CustomResponseEntity<CategorieDTO>> createCategorie(@NotNull  final Categorie categorie)
    {
        CategorieDTO categorieDTO = categorieDTOMapper.apply(categorieRepository.save(categorie));
        final CustomResponseEntity<CategorieDTO> customResponseEntity = new CustomResponseEntity<>(HttpStatus.OK, categorieDTO);
        return new ResponseEntity<>(customResponseEntity,HttpStatus.OK);
    }
    @Override
    public ResponseEntity<CustomResponseEntity<CategorieDTO>> fetchCategorieById(final long categorieId)
    {
        final CategorieDTO categorieDTO = categorieDTOMapper.apply(getCategorieById(categorieId));
        final CustomResponseEntity<CategorieDTO> customResponseEntity = new CustomResponseEntity(HttpStatus.OK,categorieDTO);
        return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomResponseList<CategorieDTO>> fetchAllCategories(final long pageNumber) {
        final Pageable pageable = PageRequest.of((int) pageNumber - 1, 10);

        final List<CategorieDTO> categorieDTOFullList = categorieRepository.fetchAllCategories(pageable)
                .stream()
                .map(categorieDTOMapper)
                .toList();

        if (categorieDTOFullList.isEmpty() && pageNumber > 1) {
            return fetchAllCategories(1);
        }

        final CustomResponseList<CategorieDTO> customResponse =
                new CustomResponseList<>(
                        HttpStatus.OK,
                        categorieDTOFullList,
                        categorieDTOFullList.size(),
                        categorieRepository.getTotalCategoriesCount()
                );

        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomResponseEntity<List<CategorieDTO>>> searchCategories(String prefix) {
        final List<CategorieDTO> searchedCategorieDto = categorieRepository.searchCategorie(prefix).stream().map(categorieDTOMapper).toList();

        final CustomResponseEntity<List<CategorieDTO>> customResponse = new CustomResponseEntity<>(HttpStatus.OK,searchedCategorieDto);
        return new ResponseEntity<>(customResponse , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomResponseEntity<String>> modifyCategorieById(final long categorieId, @NotNull  final Categorie categorieDetails)
    {
        final Categorie existingCategorie = getCategorieById(categorieId);

        categorieDetails.setId(existingCategorie.getId());

        categorieRepository.save(categorieDetails);

        final String successResponse = String.format("The Categorie with ID : %d updated successfully.", categorieId);
        final CustomResponseEntity<String> customResponseEntity = new CustomResponseEntity<>(HttpStatus.OK,successResponse);
        return new ResponseEntity<>(customResponseEntity , HttpStatus.OK);
    }
    @Override
    @Transactional
    public ResponseEntity<CustomResponseEntity<String>> deleteCategorieById(final long categorieId)
    {
        final Categorie categorie = getCategorieById(categorieId);
        for(var a : categorie.getArticles())
        {
            articleRepository.deleteArticleById(a.getId());
        }
        categorieRepository.deleteCategorieById(categorieId);
        final String successResponse = String.format("The Categorie with ID : %d deleted successfully.",categorieId);
        final CustomResponseEntity<String> customResponseEntity = new CustomResponseEntity<>(HttpStatus.OK,successResponse);
        return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
    }
    @Override
    public Categorie getCategorieById(final long categorieId)
    {
        return categorieRepository.fetchCategorieById(categorieId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("The categorie with id : %d could not be found.", categorieId)));
    }

    @Override
    public Categorie getCategorieByName(final String categorieName)
    {
        return categorieRepository.fetchCategorieByName(categorieName).orElseThrow(
                () -> new ResourceNotFoundException(String.format("The categorie with Name : %s could not be found.", categorieName)));
    }
}
