package com.example.app;

import com.example.app.services.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Objects;

@SpringBootTest
class FileServiceTests {


    @Autowired
    FileService fileService;

    @Test
    void createSectionsFromXlsTest() throws IOException {
        var testFile = Objects.requireNonNull(
                        getClass().getResourceAsStream("/test.xls"))
                .readAllBytes();
        var sections = fileService.createSectionsFromXls(testFile);

        Assert.isTrue(sections.size() == 2, "Wrong count of sections");
    }

}
