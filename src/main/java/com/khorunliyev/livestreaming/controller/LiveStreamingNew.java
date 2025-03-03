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

@RestController
@RequestMapping("/online")
public class LiveStreamingNew {
    private Queue<String> playlist = new LinkedList<>();
    private final Sinks.Many<byte[]> sink = Sinks.many().multicast().directAllOrNothing();

    public LiveStreamingNew(){
        playlist.add("oh i try - yaeow.mp3");
        startStreaming();
    }

    private void startStreaming(){
        Executors.newSingleThreadExecutor().execute(() -> {
            while (true){
                if (playlist.isEmpty()) {
                    playlist.add("oh i try - yaeow.mp3");
                }
                try {
                    String currentSong = playlist.poll();
                    InputStream inputStream = new ClassPathResource(currentSong).getInputStream();
                    byte[] buffer = new byte[4096];
                    int byteRead;
                    while ((byteRead=inputStream.read(buffer)) != -1){
                        if (sink.currentSubscriberCount()!=0) { // Send only if listeners exist
                            sink.tryEmitNext(buffer.clone());
                        }
                        //bitrate calculating
                      /*  int bitRate = 128 * 1000; // 128 kbps
                        int bytesPerSecond = bitRate / 8;
                        int sleepTime = (4096 * 1000) / bytesPerSecond; // Calculate sleep time per chunk*/
                        Thread.sleep(100);
                    }
                    inputStream.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
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
