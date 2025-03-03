package com.khorunliyev.livestreaming.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/online")
public class LiveStreamingNew {
    private Queue<String> playlist = new LinkedList<>();
    private final Sinks.Many<byte[]> sink = Sinks.many().multicast().directAllOrNothing();
    private volatile int globalBytePosition = 0; // Global playback position
    private volatile InputStream currentStream;

    public LiveStreamingNew(){
        playlist.add("oh i try - yaeow.mp3");
        startStreaming();
    }


    private void startStreaming() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::streamAudioChunk, 0, 100, TimeUnit.MILLISECONDS);
    }

    private void streamAudioChunk() {
        try {
            if (currentStream == null || currentStream.available() == 0) {
                if (!playlist.isEmpty()) {
                    currentStream = new ClassPathResource(playlist.poll()).getInputStream();
                    globalBytePosition = 0;
                } else {
                    return;
                }
            }

            byte[] buffer = new byte[4096];
            int bytesRead = currentStream.read(buffer);

            if (bytesRead != -1) {
                sink.tryEmitNext(buffer.clone()); // Send the same chunk to all users
                globalBytePosition += bytesRead; // Update global position
            } else {
                currentStream.close();
                currentStream = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping(value = "/stream", produces = "audio/mpeg")
    public Flux<byte[]> streamAudio(){
        return sink.asFlux();
    }

    @GetMapping("/next")
    public String nextSong(){
        startStreaming();
        return "Next music is playing";
    }
}
