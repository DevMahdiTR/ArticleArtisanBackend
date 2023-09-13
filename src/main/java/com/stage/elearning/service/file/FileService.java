package com.stage.elearning.service.file;

import com.stage.elearning.model.file.FileData;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    public FileData processUploadedFile(@NotNull final MultipartFile file) throws IOException, IOException;
    public ResponseEntity<byte[]> downloadFile(@NotNull final  FileData fileData) throws IOException;
    public void deleteFileFromFileSystem(@NotNull final FileData fileData) throws IOException ;
    public FileData getFileDataById(long fileDataId);


}
