package com.example.app.consumers;

import com.example.app.services.FileService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConsumer {

    @NonNull FileService fileService;

    @KafkaListener(topics = "${kafka.topic.export}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenExport(String jobId) {
        System.out.println("Received export message: " + jobId);
        fileService.doExport(Long.parseLong(jobId));
    }

    @KafkaListener(topics = "${kafka.topic.import}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenImport(String jobId) {
        System.out.println("Received import message: " + jobId);
        fileService.doImport(Long.parseLong(jobId));
    }

}