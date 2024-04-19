/*
package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.controller;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.services.GoogleloginService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/employer")
@AllArgsConstructor
public class GoogleLoginController {

    private final GoogleloginService googleloginService;

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        Optional<Map<String, Object>> user = googleloginService.getUser(principal);
        return user.orElseGet(() -> Collections.singletonMap("error", "User not found"));
    }
}*/
