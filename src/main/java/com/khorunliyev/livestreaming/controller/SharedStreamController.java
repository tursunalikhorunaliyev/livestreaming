package com.khorunliyev.livestreaming.controller;

import jakarta.annotation.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/radio")
public class SharedStreamController {
    private final Flux<DataBuffer> sharedStream;

    public SharedStreamController() {
        FileSystemResource audioFile = new FileSystemResource("/Users/khorunaliyev/IdeaProjects/livestreaming/src/main/resources/oh i try - yaeow.mp3");
        this.sharedStream = DataBufferUtils.read(audioFile, new DefaultDataBufferFactory(), 4096)
                .repeat() // Loop the audio
                .share(); // Ensure all clients get the same stream
    }

    @GetMapping(value = "/stream", produces = "audio/mpeg")
    public Flux<DataBuffer> streamAudio() {
        return sharedStream;
    }

}
