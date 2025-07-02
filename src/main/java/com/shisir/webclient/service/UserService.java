package com.shisir.webclient.service;

import com.shisir.webclient.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class UserService {
    private final WebClient webClient;

    public UserService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://jsonplaceholder.tyicode.com").build();
    }

    public List<User> fetchUsers() {
        return webClient.get()
                .uri("/users")
                .retrieve()
                .bodyToFlux(User.class)
                .collectList()
                .block();   //blocking for simplicity
    }
    public User fetchUserById(int id) {
        return webClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .bodyToMono(User.class)
                .block();
    }
}
