package com.shisir.webclient.controller;

import com.shisir.webclient.model.User;
import com.shisir.webclient.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Flux<User> getAllUsers(){
        return userService.fetchUsers();
    }

    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable int id){
        return userService.fetchUserById(id);
    }
}
