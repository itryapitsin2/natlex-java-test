package com.example.app.controllers;

import com.example.app.dto.SectionDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.app.services.SectionService;

import java.util.Optional;

@RestController
@RequestMapping("/sections")
@RequiredArgsConstructor
public class SectionController {

    @NonNull
    private SectionService sectionService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Iterable<SectionDto> read() {
        return sectionService.readAll();
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Optional<SectionDto> read(@PathVariable("id") Long id) {
        return sectionService.read(id);
    }

    @GetMapping(path = "/by-code", produces = APPLICATION_JSON_VALUE)
    public Iterable<SectionDto> readByCode(@RequestParam("code") String code) {
        return sectionService.readByCode(code);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public void create(@RequestBody final SectionDto section) {
        sectionService.createOrUpdate(section);
    }

    @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("id") Long id) {
        sectionService.delete(id);
    }

    @PutMapping(produces = APPLICATION_JSON_VALUE)
    public void update(@RequestBody final SectionDto section) {
        sectionService.createOrUpdate(section);
    }
}
