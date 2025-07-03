package com.shisir.webclient.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

public class UserServiceTest {

    private MockWebServer mockWebServer;
    private UserService userService;

    @BeforeEach
    void setup() throws IOException{
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        userService = new UserService((WebClient.Builder) webClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void fetchUsers_suceess() {
        String mockResponseBody = "[{\"id\":1,\"name\":\"Shisir\",\"email\":\"shisir@example.com\"}]";
        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponseBody)
                .addHeader("Content_Type", "application/json"));

        StepVerifier.create(userService.fetchUsers())
                .expectNextMatches(user -> user.getId() == 1 && user.getName().equals("Shisir"))
                .verifyComplete();

    }

    @Test
    public void fetchUserById_notFound(){
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        StepVerifier.create(userService.fetchUserById(99))
                .expectComplete()
                .verify();
    }
}
