package com.snakes.snakes.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameController {

    @GetMapping("/")
    public String menu() {
        return "menu.html";
    }

    @GetMapping("/snakes")
    public String snakes() {
        return "snakes.html";
    }

    @GetMapping("/statistics")
    public String statistics() {
        return "statistics.html";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings.html";
    }
}
