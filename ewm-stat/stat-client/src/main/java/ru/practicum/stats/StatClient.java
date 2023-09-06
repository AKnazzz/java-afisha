package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatClient {

    private final WebClient webclient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void saveHit(EndpointHitDto endpointHitDto) {
        webclient.post()
                .uri("/hit")
                .acceptCharset(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(endpointHitDto))
                .exchangeToMono(clientResponse -> clientResponse.statusCode().equals(HttpStatus.CREATED) ?
                        clientResponse.bodyToMono(Object.class)
                                .map(body -> ResponseEntity.status(HttpStatus.CREATED).body(body)) :
                        clientResponse.createException().flatMap(Mono::error))
                .block();
    }

    public List<ViewStatsDto> getAllStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        return webclient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("end", end.format(formatter))
                        .queryParam("start", start.format(formatter))
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return clientResponse.bodyToMono(new ParameterizedTypeReference<List<ViewStatsDto>>() {
                        });
                    } else {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new RuntimeException(errorMessage)));
                    }
                })
                .block();
    }

}
