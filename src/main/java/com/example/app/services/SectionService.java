package com.example.app.services;

import com.example.app.dto.SectionDto;
import com.example.app.repositories.SectionRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionService {

    @NonNull
    private SectionRepository sectionRepository;

    public void createOrUpdate(@NonNull SectionDto sectionDto) {
        var section = sectionDto.toSection();
        sectionRepository.save(section);
    }

    public void delete(Long id) {
        sectionRepository.deleteById(id);
    }

    public Optional<SectionDto> read(Long id) {
        return sectionRepository
                .findById(id)
                .map(SectionDto::fromSection);
    }

    public Iterable<SectionDto> readAll() {
        return sectionRepository.retrieveAll()
                .stream()
                .map(SectionDto::fromSection)
                .collect(Collectors.toList());
    }

    public Iterable<SectionDto> readByCode(String code) {
        return sectionRepository
                .retrieveAllByCode(code)
                .stream()
                .map(SectionDto::fromSection)
                .collect(Collectors.toSet());
    }
}
