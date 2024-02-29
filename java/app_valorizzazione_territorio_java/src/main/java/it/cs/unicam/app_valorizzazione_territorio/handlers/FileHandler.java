package it.cs.unicam.app_valorizzazione_territorio.handlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileHandler {

    @Value("${fileResources.path}")
    private String filePath;

    public void saveFile(MultipartFile file) throws IOException {
        File newFile = new File(filePath + file.getOriginalFilename());
        newFile.createNewFile();
        FileOutputStream fileOut = new FileOutputStream(newFile);
        fileOut.write(file.getBytes());
        fileOut.close();
    }

    public InputStreamResource getInputStreamResourceForFile(String filename) throws IOException {
        return new InputStreamResource(new FileInputStream(filePath + filename));
    }

    public MediaType getMediaTypeForFileName(String fileName) {
        String[] arr = fileName.split("\\.");
        String type = arr[arr.length-1];
        return switch (type) {
            case "pdf" ->           MediaType.APPLICATION_PDF;
            case "png" ->           MediaType.IMAGE_PNG;
            case "jpeg", "jpg" ->   MediaType.IMAGE_JPEG;
            default ->              MediaType.APPLICATION_OCTET_STREAM;
        };
    }
}
