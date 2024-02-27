package it.cs.unicam.app_valorizzazione_territorio.controllers;

import it.cs.unicam.app_valorizzazione_territorio.handlers.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private FileHandler fileHandler;

    @PostMapping(value="upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            fileHandler.saveFile(file);
            return new ResponseEntity<>("File uploaded", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("File not uploaded. Error: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "view/{filename}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getImage(@PathVariable("filename") String filename) {
        try {
            return ResponseEntity.ok()
                    .contentType(fileHandler.getMediaTypeForFileName(filename))
                    .body(fileHandler.getInputStreamResourceForFile(filename));
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
