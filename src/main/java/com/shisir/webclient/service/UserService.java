package com.shisir.webclient.service;

import com.shisir.webclient.model.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
public class UserService {
    private final WebClient webClient;

    public UserService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    public Flux<User> fetchUsers() {
        return webClient.get()
                .uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> {
                    return Mono.error(new RuntimeException("Client error: " + response.statusCode()));
                })
                .onStatus(status -> status.is5xxServerError(), response -> {
                    return Mono.error(new RuntimeException("Server error: " + response.statusCode()));
                })
                .bodyToFlux(User.class)
                .timeout(Duration.ofSeconds(5))//handle slow responses
                .onErrorResume(ex -> {
                    System.err.println("Error fetching users: " + ex.getMessage());
                    return Flux.empty();
                });

    }
    public Mono<User> fetchUserById(int id) {
        return webClient.get()
                .uri("/users/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response ->
                    Mono.error(new RuntimeException("User not found"))
                )
                .onStatus(status -> status.is5xxServerError(), response ->
                        Mono.error(new RuntimeException("API Server error!"))
                )
                .bodyToMono(User.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(ex -> {
                    System.err.println("Error fetching user: " + ex.getMessage());
                    return Mono.empty();
                });
    }
}
