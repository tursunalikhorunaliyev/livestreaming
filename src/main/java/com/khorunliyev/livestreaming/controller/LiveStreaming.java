package com.khorunliyev.livestreaming.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class LiveStreaming {
    private final List<String> songPath = new ArrayList<>();

    private static final AtomicReference<Instant> startTime = new AtomicReference<>(Instant.now());

    private static final long SONG_DURATION = 180; //3 minut


//    @PostMapping("/set-resource")
//    public void setMusicResources(){
//        songPath.add("oh i try - yaeow.mp3");
//        songPath.add("oh i try - yaeow.mp3");
//        songPath.add("oh i try - yaeow.mp3");
//        songPath.add("oh i try - yaeow.mp3");
//    }
//
//    @GetMapping(value = "/stream-music", produces = "audio/mpeg")
//    public Flux<DataBuffer> streamMusic() throws IOException {
//        Resource resource = new ClassPathResource("oh i try - yaeow.mp3");
//        Path path = resource.getFile().toPath();
//
//        long elapsedTime = Duration.between(startTime.get(), Instant.now()).getSeconds();
//        if (elapsedTime > SONG_DURATION) elapsedTime = SONG_DURATION;
//
//
//        long fileSize = Files.size(path);
//        long skipBytes = (fileSize / SONG_DURATION) * elapsedTime;
//
//        return Flux.using(() -> Files.newInputStream(path), inputStream -> {
//            try {
//                inputStream.skipNBytes(skipBytes);
//                return Flux.generate(sink -> {
//                    try{
//                        byte[] buffer = new byte[4096];
//                        int bytesRead = inputStream.read(buffer);
//                        if(bytesRead == -1){
//                            sink.complete();
//                        }
//                        else{
//                            sink.next(new DefaultDataBufferFactory().wrap(buffer));
//                        }
//
//                    }
//                    catch (IOException e){
//                        sink.error(e);
//                    }
//                });
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//    }

}
