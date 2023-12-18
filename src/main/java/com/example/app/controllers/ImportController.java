package com.example.app.controllers;

import com.example.app.entities.Status;
import com.example.app.services.FileService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/import")
@RequiredArgsConstructor
public class ImportController {
    @NonNull
    private FileService fileService;

    @PostMapping(path = "/")
    public Long importFile(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        return fileService.createImportTask(multipartFile);
    }

    @GetMapping(path = "/{id}")
    public Status readStatus(@PathVariable("id") Long id) {
        return fileService.getStatus(id);
    }
}
