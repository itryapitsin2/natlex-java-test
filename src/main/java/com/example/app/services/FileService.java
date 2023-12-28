package com.example.app.services;

import com.example.app.entities.*;
import com.example.app.producers.MessageProducer;
import com.example.app.repositories.FileRepository;
import com.example.app.repositories.SectionRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class FileService {

    @NonNull
    FileRepository fileRepository;

    @NonNull
    SectionRepository sectionRepository;

    @Value("${kafka.topic.import}")
    String kafkaImportTopic;

    @Value("${kafka.topic.export}")
    String kafkaExportTopic;

    @NonNull
    private MessageProducer messageProducer;

    public Status getStatus(Long jobId) {
        return fileRepository.findStatusById(jobId);
    }

    public Long createImportTask(MultipartFile multipartFile) throws IOException {
        var file = File
                .builder()
                .status(Status.INPROGRESS)
                .taskType(TaskType.IMPORT)
                .data(
                        Base64
                                .getEncoder()
                                .encodeToString(multipartFile.getBytes())
                )
                .build();
        var jobId = fileRepository.save(file).getId();
        messageProducer.sendMessage(kafkaImportTopic, jobId.toString());
        return jobId;
    }

    public Long createExportTask() {
        var file = File
                .builder()
                .status(Status.INPROGRESS)
                .taskType(TaskType.EXPORT)
                .build();
        var jobId = fileRepository.save(file).getId();
        messageProducer.sendMessage(kafkaExportTopic, jobId.toString());

        return jobId;
    }

    // Добавить транзакцию
    public void doExport(Long jobId) {
        var file = fileRepository.findById(jobId).orElseThrow();
        try {
            var sections = sectionRepository.findAll();
            file.setData(
                    createXlsFromSections(sections)
            );
            file.setStatus(Status.DONE);
            fileRepository.save(file);
        } catch (Exception e) {
            file.setStatus(Status.ERROR);
            fileRepository.save(file);
            // Добавить логгирование
        }
    }

    // Добавить транзакцию
    public void doImport(Long jobId) {
        var file = fileRepository.findById(jobId).orElseThrow();
        try {

            var sections = createSectionsFromXls(Base64.getDecoder().decode(file.getData()));
            sectionRepository.saveAll(sections);

            file.setStatus(Status.DONE);
            fileRepository.save(file);
        } catch (Exception e) {
            file.setStatus(Status.ERROR);
            fileRepository.save(file);
            // Добавить логгирование
        }
    }

    public InputStreamResource getFileData(Long jobId) {

        var data = fileRepository.findById(jobId).orElseThrow().getData();

        return new InputStreamResource(
                new ByteArrayInputStream(
                        Base64.getDecoder().decode(data)
                )
        );
    }

    String createXlsFromSections(@NonNull Iterable<Section> sections) throws IOException {
        var byteOutputStream = new ByteArrayOutputStream();
        try (var workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("Sections");
            var header = sheet.createRow(0);

            var headerCell = header.createCell(0);
            headerCell.setCellValue("Section name");

            sections.forEach(withCounter((idx, section) -> {
                var sectionRow = sheet.createRow(idx + 1);
                var sectionRowCell = sectionRow.createCell(0);
                sectionRowCell.setCellValue(section.getName());

                var cellIdx = 1;
                for (var geologicalClass : section.getGeologicalClasses()) {

                    var geologicalClassNameHeaderCell = header.createCell(cellIdx);
                    geologicalClassNameHeaderCell.setCellValue("Class " + cellIdx + " name");

                    var classNameCell = sectionRow.createCell(cellIdx);
                    classNameCell.setCellValue(geologicalClass.name);

                    cellIdx++;

                    var geologicalClassCodeHeaderCell = header.createCell(cellIdx);
                    geologicalClassCodeHeaderCell.setCellValue("Class " + cellIdx + " code");

                    var classCodeCell = sectionRow.createCell(cellIdx);
                    classCodeCell.setCellValue(geologicalClass.code);

                    cellIdx++;
                }
            }));

            workbook.write(byteOutputStream);
        }
        return Base64.getEncoder().encodeToString(byteOutputStream.toByteArray());
    }

    public List<Section> createSectionsFromXls(byte[] data) throws IOException {
        var byteInputStream = new ByteArrayInputStream(data);
        var sections = new ArrayList<Section>();
        try (var workbook = new XSSFWorkbook(byteInputStream)) {
            var sheet = workbook.getSheetAt(0);
            sheet.forEach(withCounter((rowIdx, row) -> {
                if (rowIdx == 0) {
                    return;
                }
                var section = Section
                        .builder()
                        .name(row.getCell(0).getStringCellValue());

                var geologicalClasses = new HashSet<GeologicalClass>();


                for (int cellIdx = 1; cellIdx < row.getLastCellNum(); cellIdx += 2) {

                    var geologicalClass = GeologicalClass
                            .builder()
                            .name(row.getCell(cellIdx).getStringCellValue())
                            .code(row.getCell(cellIdx + 1).getStringCellValue())
                            .build();

                    geologicalClasses.add(geologicalClass);
                }

                section.geologicalClasses(geologicalClasses);

                sections.add(
                        section.build()
                );
            }));
        }
        return sections;
    }

    public static <T> Consumer<T> withCounter(BiConsumer<Integer, T> consumer) {
        var counter = new AtomicInteger(0);
        return item -> consumer.accept(counter.getAndIncrement(), item);
    }
}
