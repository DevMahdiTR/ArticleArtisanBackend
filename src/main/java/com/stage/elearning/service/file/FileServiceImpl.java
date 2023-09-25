package com.stage.elearning.service.file;

import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.model.file.FileData;
import com.stage.elearning.repository.FileDataRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

@Service
public class FileServiceImpl implements  FileService{

    private final FileDataRepository fileDataRepository;

    @Autowired
    public FileServiceImpl(FileDataRepository fileDataRepository)
    {
        this.fileDataRepository = fileDataRepository;
    }

    public FileData save(FileData fileData)
    {
        return fileDataRepository.save(fileData);
    }

    @Transactional
    public void deleteFileById(final long fileId)
    {
        FileData fileToDelete = getFileDataById(fileId);
        fileDataRepository.deleteFileDataById(fileId);
    }

    private final String  FILE_SYSTEM_PATH= Paths.get("").toAbsolutePath().resolve("src").resolve("main").resolve("resources") + "/" ;

    @Override
    public FileData processUploadedFile(@NotNull final MultipartFile file) throws IOException, IOException {
        var originalFileName = file.getOriginalFilename();
        var fileName = originalFileName.substring(0, originalFileName.indexOf('.'));
        var extension = originalFileName.substring(originalFileName.indexOf('.'));
        var filePath = FILE_SYSTEM_PATH + fileName + UUID.randomUUID() + extension;

        FileData fileData = FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath)
                .build();

        file.transferTo(new File(filePath));
        fileDataRepository.save(fileData);
        return fileData;
    }
    public ResponseEntity<byte[]> downloadFile(@NotNull final  FileData fileData) throws IOException {
        final String filePath = fileData.getFilePath();
        byte[] file = Files.readAllBytes(new File(filePath).toPath());
        HttpHeaders headers = new HttpHeaders();
        String contentType = determineContentType(filePath);
        headers.setContentDispositionFormData("attachment", fileData.getFilePath());
        headers.setContentType(MediaType.parseMediaType(contentType));

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }

    @Transactional
    public void deleteFileFromFileSystem(@NotNull final FileData fileData) throws IOException {
        File fileToDelete = new File(fileData.getFilePath());
        if(!fileToDelete.delete())
        {
            throw new IOException(String.format("Failed to delete file with ID : %d",fileData.getId()));
        }
        fileDataRepository.deleteFileDataById(fileData.getId());
    }

    public String determineContentType(@NotNull String filePath) {

        String extension = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();

        HashMap<String, String> extensionToContentTypeMap = new HashMap<>();
        extensionToContentTypeMap.put("png", "image/png");
        extensionToContentTypeMap.put("jpg", "image/jpeg");
        extensionToContentTypeMap.put("jpeg", "image/jpeg");
        extensionToContentTypeMap.put("gif", "image/gif");
        extensionToContentTypeMap.put("bmp", "image/bmp");
        extensionToContentTypeMap.put("ico", "image/vnd.microsoft.icon");
        extensionToContentTypeMap.put("tiff", "image/tiff");
        return extensionToContentTypeMap.getOrDefault(extension, "application/octet-stream");
    }
    public FileData getFileDataById(long fileDataId)
    {
        return fileDataRepository.fetchFileDataById(fileDataId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("The file with ID : %s could not be found.", fileDataId)));
    }
}
