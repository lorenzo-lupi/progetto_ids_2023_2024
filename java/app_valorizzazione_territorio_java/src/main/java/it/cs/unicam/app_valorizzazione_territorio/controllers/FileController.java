package it.cs.unicam.app_valorizzazione_territorio.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("file")
public class FileController {
    @Value("${fileResources.path}")
    private String filePath;

    @RequestMapping(value="upload", method= RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            saveFile(file);
            return new ResponseEntity<>("File uploaded", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("File not uploaded. Error: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void saveFile(MultipartFile file) throws IOException {
        File newFile = new File(filePath + file.getOriginalFilename());
        newFile.createNewFile();
        FileOutputStream fileOut = new FileOutputStream(newFile);
        fileOut.write(file.getBytes());
        fileOut.close();
    }

    @GetMapping(value = "view/{filename}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getImage(@PathVariable("filename") String filename) {
        try {
            InputStream inputStream = new FileInputStream(filePath + filename);
            return ResponseEntity.ok()
                    .contentType(getMediaTypeForFileName(filename))
                    .body(new InputStreamResource(inputStream));
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private MediaType getMediaTypeForFileName(String fileName) {
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
