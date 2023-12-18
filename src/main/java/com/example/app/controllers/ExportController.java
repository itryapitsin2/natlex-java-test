package com.example.app.controllers;

import com.example.app.entities.Status;
import com.example.app.services.FileService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
public class ExportController {

    @NonNull
    private FileService fileService;

    @GetMapping(path = "/", produces = APPLICATION_JSON_VALUE)
    public Long exportFile() {
        return fileService.createExportTask();
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Status readStatus(@PathVariable("id") Long id) {
        return fileService.getStatus(id);
    }


    @GetMapping(path = "/{id}/file")
    @ResponseBody
    public ResponseEntity<Resource> readFile(@PathVariable("id") Long id) {
        var filename = String.format("%s.xls", RandomStringUtils.randomAlphabetic(10));
        var resource = fileService.getFileData(id);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(resource);
    }
}
