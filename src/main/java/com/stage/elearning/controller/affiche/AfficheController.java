package com.stage.elearning.controller.affiche;
import com.stage.elearning.dto.affiche.AfficheDTO;
import com.stage.elearning.service.affiche.AfficheService;
import com.stage.elearning.utility.CustomResponseEntity;
import com.stage.elearning.utility.CustomResponseList;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/affiche")
public class AfficheController {

    private final AfficheService afficheService;

    public AfficheController(AfficheService afficheService)
    {
        this.afficheService = afficheService;
    }

    @PostMapping("/admin/create_affiche")
    public ResponseEntity<CustomResponseEntity<AfficheDTO>> createAffiche(
            @RequestParam("image") MultipartFile image,
            @Valid @RequestParam("jsonAffiche") String jsonAffiche) throws IOException {
        return afficheService.createAffiche(image,jsonAffiche);
    }

    @GetMapping("/all/get/prefix/{prefix}")
    public ResponseEntity<CustomResponseEntity<List<AfficheDTO>>> searchAffiche(@PathVariable("prefix") final String prefix){
        return afficheService.searchAffiche(prefix);
    }

    @GetMapping("/all/get_affiche_image/id/{afficheId}")
    public ResponseEntity<byte[]>  fetchAfficheImage(@PathVariable("afficheId") final long afficheId) throws IOException {
        return afficheService.fetchAfficheImage(afficheId);
    }
    @PutMapping("/admin/update/id/{afficheId}")
    public ResponseEntity<CustomResponseEntity<String>> modifyAfficheById(
            @RequestParam("image") final MultipartFile image,
            @PathVariable("afficheId") final long afficheId ,
            @RequestParam("jsonAfficheDetails") final String jsonAfficheDetails) throws IOException {
        return afficheService.modifyAfficheById(image,afficheId,jsonAfficheDetails);
    }

    @GetMapping("/all/get/id/{afficheId}")
    public ResponseEntity<CustomResponseEntity<AfficheDTO>> fetchAfficheById(@PathVariable("afficheId") final long afficheId)
    {
        return afficheService.fetchAfficheById(afficheId);
    }

    @GetMapping("/all/get/all_affiche")
    public ResponseEntity<CustomResponseList<AfficheDTO>>  fetchAllAffiche(
            @RequestParam(value = "pageNumber", required = true) final long pageNumber
    )
    {
        return afficheService.fetchAllAffiche(pageNumber);
    }

    @DeleteMapping("/admin/delete/id/{afficheId}")
    public ResponseEntity<CustomResponseEntity<String>> deleteAfficheById(@PathVariable final long afficheId) throws IOException {
        return afficheService.deleteAfficheById(afficheId);
    }
}
