package com.snakes.snakes.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.snakes.snakes.Services.GameService;

@Controller
public class MainController {

    @GetMapping("/statistics")
    public String statistics() {
        return "statistics.html";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings.html";
    }
}
