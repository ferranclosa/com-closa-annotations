package com.closa.annotations.testing;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerCheck {
    @GetMapping("/")
    public String connected() {
        return "Docker Container";
    }
}
