package com.shisir.webclient.controller;

import com.shisir.webclient.model.User;
import com.shisir.webclient.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(UserController.class)
@Import(UserControllerTest.MockConfig.class)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

//    @org.springframework.beans.factory.annotation.Autowired
//    private WebTestClient webTestClient;

    static class MockConfig {
        @Bean
        public UserService userService(){
            return Mockito.mock(UserService.class);
        }
    }

    @Test
    public void getAllUsers_shouldReturnList(){
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setName("Jane Doe");
        mockUser.setUsername("jane");
        mockUser.setEmail("jane@example.com");

        Mockito.when(userService.fetchUsers()).thenReturn(Flux.just(mockUser));

        webTestClient.get().uri("/api/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .hasSize(1)
                .contains(mockUser);
    }

    @Test
    public void getUserById_shouldReturnUser(){
        User mockUser = new User();
        mockUser.setId(2);
        mockUser.setName("Shisir");
        mockUser.setUsername("shisir");
        mockUser.setEmail("Shisir@gmail.com");

        Mockito.when(userService.fetchUserById(2)).thenReturn(Mono.just(mockUser));

        webTestClient.get().uri("/api/users/2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> {
                    assert user.getId() == 2;
                    assert user.getName().equals("Bob");
                });
    }
}
